<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Cliente PHP - Calculadora</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .container { max-width: 400px; margin: auto; }
    input, select, button { margin: 5px 0; padding: 8px; width: 100%; }
    button { background-color: #4CAF50; color: white; border: none; cursor: pointer; }
    button:hover { background-color: #45a049; }
  </style>
</head>
<body>
  <div class="container">
    <h2>Calculadora via API Spring Boot</h2>
    <form action="calcular.php" method="GET">
      <input type="number" name="num1" placeholder="Número 1" required>
      <input type="number" name="num2" placeholder="Número 2" required>
      <select name="operacao">
        <option value="somar">Somar</option>
        <option value="subtrair">Subtrair</option>
        <option value="multiplicar">Multiplicar</option>
        <option value="dividir">Dividir</option>
      </select>
      <button type="submit">Calcular</button>
    </form>
  </div>
</body>
</html>
