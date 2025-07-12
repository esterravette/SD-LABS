<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Resultado</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .container { max-width: 400px; margin: auto; }
    a { color: #4CAF50; text-decoration: none; }
  </style>
</head>
<body>
  <div class="container">
    <h2>Resultado da Operação</h2>
    <p><?php echo htmlspecialchars($_GET['resposta'] ?? 'Nenhum resultado encontrado.'); ?></p>
    <a href="index.php">← Voltar</a>
  </div>
</body>
</html>
