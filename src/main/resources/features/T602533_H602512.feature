#language: pt
#encoding: utf-8
Funcionalidade: T602533 - H602512 | instrucaoQA

  @PhilipBB
  Cenario: CT001 - Cenario de teste automatizado pelo Philip
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

  @EduardoBB
  Cenario: CT002 - Cenario de teste automatizado pelo Eduardo
    Dado que a Plataforma esteja fechada, abra a Plataforma
    E se nao estiver logado, realiza o login no Sistema SOL com a chave 1
    E abrir aplicacao "Contas" "Cadastramentos e Autorizações"
    E feche o modal inicial
    E logar o breadcrumb da aplicação aberta
    E abrir submenu "Cadastramentos" "Renavam"
    Então contextualize a ag "18" e cc "1020"
    Quando ordenar ag e cc
    E seleciona ag 18 e cc 1020
    E clicar no radio button "Todos"
    E verifica se o botão "Excluir" inicia "desativado"
    E verifica se o botão "Reativar" inicia "desativado"
    E verifica se o botão "Incluir" inicia "ativado"
    Então logar os registros encontrados




