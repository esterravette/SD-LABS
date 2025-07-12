<?php
$num1 = $_GET['num1'] ?? 0;
$num2 = $_GET['num2'] ?? 0;
$operacao = $_GET['operacao'] ?? 'somar';

$url = "http://localhost:8080/$operacao/$num1/$num2";

// Inicializa o cURL
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Executa a requisição
$resposta = curl_exec($ch);

// Verifica erros
if (curl_errno($ch)) {
    die("Erro na requisição: " . curl_error($ch));
}
curl_close($ch);

// Redireciona com o resultado
header("Location: resultado.php?resposta=" . urlencode($resposta));
?>
