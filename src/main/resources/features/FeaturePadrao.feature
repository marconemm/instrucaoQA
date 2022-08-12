#language: pt
#encoding: utf-8
Funcionalidade: T602533 - H602512 | primeiroBB

  @primeiroBB
  Cenario: CT001 - Descrição do cenário
    Dado que a Plataforma esteja fechada, abra a Plataforma
    E se nao estiver logado, realiza o login no Sistema SOL com a chave 1
    E abrir aplicacao "Contas" "Cadastramentos e Autorizações"
    E feche o modal inicial
    Então logar o título da aplicação aberta
    Quando abrir submenu "Cadastramentos" "CPF/CNPJ"
    E contextualize a ag "18" e cc "1020"
    E ordenar ag e cc
    E seleciona ag 18 e cc 1020
    E verificar se o nome do titular é "Epitacio Da Costa Magalhaes"
