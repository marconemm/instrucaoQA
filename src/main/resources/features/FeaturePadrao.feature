#language: pt
#encoding: utf-8
Funcionalidade: T602533 - H602512 | primeiroBB

  @primeiroBB
  Cenario: CT001 - Descrição do cenário
    Dado que a Plataforma esteja fechada, abra a Plataforma
    E se nao estiver logado, realiza o login no Sistema SOL com a chave 1
    E abrir aplicacao "Contas" "Abertura de Conta PF"      
    E informar o titular "423.063.878-77"
    E verificar se o nome do titular é "PAULO GARRIDO ZANETTI"
