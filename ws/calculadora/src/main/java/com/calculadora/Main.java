package com.calculadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class Main {

    @GetMapping("/somar/{a}/{b}")
    public String somar(@PathVariable double a, @PathVariable double b) {
        return "Resultado: " + (a + b);
    }

    @GetMapping("/subtrair/{a}/{b}")
    public String subtrair(@PathVariable double a, @PathVariable double b) {
        return "Resultado: " + (a - b);
    }

    @GetMapping("/multiplicar/{a}/{b}")
    public String multiplicar(@PathVariable double a, @PathVariable double b) {
        return "Resultado: " + (a * b);
    }

    @GetMapping("/dividir/{a}/{b}")
    public String dividir(@PathVariable double a, @PathVariable double b) {
        if (b == 0) {
            return "Erro: Divisão por zero!";
        }
        return "Resultado: " + (a / b);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
