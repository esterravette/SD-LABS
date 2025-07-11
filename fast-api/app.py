from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import xml.etree.ElementTree as ET
import os
import configparser

# Carregando o nome do arquivo XML a partir do config.ini
config = configparser.ConfigParser()
config.read("config.ini")
ARQUIVO_XML = config["DEFAULT"]["arquivo"]

# Inicializa a aplicação FastAPI
app = FastAPI()

# Modelo de dados para aparelhos
class Aparelho(BaseModel):
    id: int
    nome: str
    marca: str
    preco: float
    categoria: str
    deposito: str

# Função auxiliar para criar o arquivo XML caso não exista
def criar_xml_se_nao_existir():
    if not os.path.exists(ARQUIVO_XML):
        estoque = ET.Element("estoque")
        tree = ET.ElementTree(estoque)
        tree.write(ARQUIVO_XML)

# Função para carregar todos os aparelhos do XML
def carregar_aparelhos():
    criar_xml_se_nao_existir()
    tree = ET.parse(ARQUIVO_XML)
    root = tree.getroot()
    aparelhos = []
    for elem in root.findall("aparelho"):
        aparelho = Aparelho(
            id=int(elem.find("id").text),
            nome=elem.find("nome").text,
            marca=elem.find("marca").text,
            preco=float(elem.find("preco").text),
            categoria=elem.find("categoria").text,
            deposito=elem.find("deposito").text
        )
        aparelhos.append(aparelho)
    return aparelhos

# Função para salvar aparelhos no XML
def salvar_aparelhos(aparelhos):
    root = ET.Element("estoque")
    for ap in aparelhos:
        ap_elem = ET.SubElement(root, "aparelho")
        ET.SubElement(ap_elem, "id").text = str(ap.id)
        ET.SubElement(ap_elem, "nome").text = ap.nome
        ET.SubElement(ap_elem, "marca").text = ap.marca
        ET.SubElement(ap_elem, "preco").text = str(ap.preco)
        ET.SubElement(ap_elem, "categoria").text = ap.categoria
        ET.SubElement(ap_elem, "deposito").text = ap.deposito
    tree = ET.ElementTree(root)
    tree.write(ARQUIVO_XML)

# Endpoint para listar todos os aparelhos
@app.get("/aparelhos")
def listar_aparelhos():
    return carregar_aparelhos()

# Endpoint para buscar aparelho por ID
@app.get("/aparelhos/{aparelho_id}")
def obter_aparelho(aparelho_id: int):
    aparelhos = carregar_aparelhos()
    for ap in aparelhos:
        if ap.id == aparelho_id:
            return ap
    raise HTTPException(status_code=404, detail="Aparelho não encontrado")

# Endpoint para adicionar novo aparelho
@app.post("/aparelhos")
def adicionar_aparelho(aparelho: Aparelho):
    aparelhos = carregar_aparelhos()
    if any(ap.id == aparelho.id for ap in aparelhos):
        raise HTTPException(status_code=400, detail="ID já existe")
    aparelhos.append(aparelho)
    salvar_aparelhos(aparelhos)
    return {"mensagem": "Aparelho adicionado com sucesso"}

# Endpoint para atualizar aparelho existente
@app.put("/aparelhos/{aparelho_id}")
def atualizar_aparelho(aparelho_id: int, novo_aparelho: Aparelho):
    aparelhos = carregar_aparelhos()
    for i, ap in enumerate(aparelhos):
        if ap.id == aparelho_id:
            aparelhos[i] = novo_aparelho
            salvar_aparelhos(aparelhos)
            return {"mensagem": "Aparelho atualizado com sucesso"}
    raise HTTPException(status_code=404, detail="Aparelho não encontrado")

# Endpoint para deletar aparelho
@app.delete("/aparelhos/{aparelho_id}")
def deletar_aparelho(aparelho_id: int):
    aparelhos = carregar_aparelhos()
    for ap in aparelhos:
        if ap.id == aparelho_id:
            aparelhos.remove(ap)
            salvar_aparelhos(aparelhos)
            return {"mensagem": "Aparelho deletado com sucesso"}
    raise HTTPException(status_code=404, detail="Aparelho não encontrado")

# Endpoint para transferir aparelho de depósito
@app.put("/aparelhos/{aparelho_id}/transferir/{novo_deposito}")
def transferir_aparelho(aparelho_id: int, novo_deposito: str):
    aparelhos = carregar_aparelhos()
    for ap in aparelhos:
        if ap.id == aparelho_id:
            ap.deposito = novo_deposito
            salvar_aparelhos(aparelhos)
            return {"mensagem": "Aparelho transferido com sucesso"}
    raise HTTPException(status_code=404, detail="Aparelho não encontrado")
