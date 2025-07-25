import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class simuladorRelogios {

    static final int NUM_PROCESSOS = 3;
    @SuppressWarnings("unchecked")
    static final BlockingQueue<Message>[] queues = new LinkedBlockingQueue[NUM_PROCESSOS];

    static class Message {
        final String content;
        final int[] vectorClock;
        final int lamportClock;

        public Message(String content, int lamportClock) {
            this.content = content;
            this.lamportClock = lamportClock;
            this.vectorClock = new int[NUM_PROCESSOS];
        }

        public Message(String content, int[] vectorClock) {
            this.content = content;
            this.vectorClock = Arrays.copyOf(vectorClock, vectorClock.length);
            this.lamportClock = 0;
        }
    }

    static class Processo extends Thread {
        private final int id;
        private final int labPart;
        private int lamportClock = 0;
        private final int[] vectorClock = new int[NUM_PROCESSOS];
        private final Random random = new Random();

        public Processo(int id, int labPart) {
            this.id = id;
            this.labPart = labPart;
            Arrays.fill(vectorClock, 0);
        }
        
        @Override
        public void run() {
            log("iniciado.");
            for (int i = 0; i < 4; i++) {
                try {
                    Thread.sleep(random.nextInt(1000) + 500);

                    if (random.nextBoolean()) {
                        int destinationId = random.nextInt(NUM_PROCESSOS);
                        while (destinationId == this.id) {
                             destinationId = random.nextInt(NUM_PROCESSOS);
                        }
                        
                        updateClockForSend();
                        String msgContent = "Ola do processo " + this.id;
                        Message msg;

                        if (labPart == 2) {
                             msg = new Message(msgContent, this.lamportClock);
                        } else if (labPart == 3) {
                             msg = new Message(msgContent, this.vectorClock);
                        } else {
                             msg = new Message(msgContent, 0);
                        }
                        
                        queues[destinationId].add(msg);
                        log(String.format("enviou mensagem para o processo %d.", destinationId));

                    } else {
                       updateClockForInternalEvent();
                       log("executou um evento interno.");
                    }

                    Message receivedMsg = queues[id].poll();
                    if (receivedMsg != null) {
                        updateClockForReceive(receivedMsg);
                        log(String.format("recebeu: '%s'", receivedMsg.content));
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
             log("finalizado.");
        }
        
        private void updateClockForSend() {
            if (labPart == 2) {
                lamportClock++;
            } else if (labPart == 3) {
                vectorClock[id]++;
            }
        }
        
        private void updateClockForInternalEvent() {
             if (labPart == 2) {
                lamportClock++;
            } else if (labPart == 3) {
                vectorClock[id]++;
            }
        }
        
        private void updateClockForReceive(Message msg) {
            if (labPart == 2) {
                lamportClock = Math.max(lamportClock, msg.lamportClock) + 1;
            } else if (labPart == 3) {
                for (int i = 0; i < NUM_PROCESSOS; i++) {
                    vectorClock[i] = Math.max(vectorClock[i], msg.vectorClock[i]);
                }
                vectorClock[id]++;
            }
        }

        private void log(String eventDescription) {
            long physicalTime = System.currentTimeMillis();
            String clockInfo = "";
            switch (labPart) {
                case 1:
                    break;
                case 2:
                    clockInfo = String.format(" | Lamport: %d", lamportClock);
                    break;
                case 3:
                    clockInfo = String.format(" | Vetor: %s", Arrays.toString(vectorClock));
                    break;
            }
            
            System.out.printf("[Fisico: %d] [Processo %d] %s%s%n",
                physicalTime, this.id, eventDescription, clockInfo);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < NUM_PROCESSOS; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }

        System.out.println("--- PARTE 1: RELOGIO FISICO ---");
        runSimulation(1);

        System.out.println("\n--- PARTE 2: RELOGIO LOGICO DE LAMPORT ---");
        runSimulation(2);

        System.out.println("\n--- PARTE 3: RELOGIOS VETORIAIS ---");
        runSimulation(3);
    }
    
    private static void runSimulation(int labPart) throws InterruptedException {
        Processo[] processos = new Processo[NUM_PROCESSOS];
        for (int i = 0; i < NUM_PROCESSOS; i++) {
            processos[i] = new Processo(i, labPart);
            processos[i].start();
        }

        for (Processo p : processos) {
            p.join();
        }
    }
}