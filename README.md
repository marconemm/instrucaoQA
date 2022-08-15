# Nome do Projeto

## Projeto de automação E2E, utilizando o FTABB para a [Plataforma BB](https://plataforma.hm.bb.com.br/gaw/v3/)

### Links úteis:
 - [Documentação FTABB](https://ftabb.intranet.bb.com.br/#/)

**Para mais detalhes sobre o versionamento, consultar o [CHANGELOG](https://fontes.intranet.bb.com.br/sol/nome-do-projeto-testesautomatizados/-/blob/master/CHANGELOG.md).**

___
___
## Rodando o teste [localmente](https://fontes.intranet.bb.com.br/sol/nome-do-projeto-testesautomatizados/-/tree/master#rodar-este-teste-localmente) e/ou via [qTeste](https://fontes.intranet.bb.com.br/sol/nome-do-projeto-testesautomatizados/-/tree/master#rodar-este-teste-via-qteste):

### Rodar este teste localmente:
1. Possuir o Java JDK 8 instalado. Seguir orientações de instalação conforme
   o [site da Oracle](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html) (
   requer [criação de conta na Oracle](https://profile.oracle.com/myprofile/account/create-account.jspx) para a
   realização do download); É importante configurar as variáveis de ambiente (**JAVA_HOME** e **CLASSPATH**) para o
   Java;]

- #### Setando as variáveis de ambiente:
	- ##### Linux:
		- Abra o terminal e digite `sudo nano ~/.bashrc`;
		- No final do arquivo em edição, insira o trecho:
		```
        #Java 8 SDK:
		JAVA_HOME=/usr/lib/jvm/jdk1.8.0_202-oracle (ou o caminho para o diretório raiz onde Você instalou o JDK 8)
		export JAVA_HOME
		export PATH=$PATH:$JAVA_HOME
        ```
		- Salve e feche o arquivo com `Ctrl + S e Ctrl + X`;
		- Execute o comando `source ~/.bashrc`; e
		- Verificar se o **Java** foi **corretamente instalado**, execute o seguinte comando no seu terminal: `java - version`.
    
    - ##### Windows:
		- Em pesquisar do Windows, procure e selecione: Editar as variáveis de ambiente do sistema
		- Clique em **Variáveis de Ambiente**. Na seção **Variáveis de usuário**, clique em **Novo**;
		- Na janela **Nova Variável de Usuário**, especifique os campos:
			- Nome da variável: `JAVA_HOME`
			- Valor da variável: `C:\Program Files\Java\jdk1.8.0_311` *(ou o caminho para o diretório raiz onde Você instalou o JDK 8)*
		- Clique em OK para salvar;
		- Na seção **Variáveis do sistema** localize a variável de ambiente **Path** e selecione-a. Clique em **Editar**.
		- Na janela **Editar a variável de ambiente** clique em **Novo**; e
		- Digite: `%JAVA_HOME%\bin`
		- Clique em OK nas próximas duas janelas
		- Verificar se o **Java** foi **corretamente instalado**, execute o seguinte comando no seu terminal: `java -version`.

#### Drivers (Chrome e Firefox):
- Acessar: [Ambiente Web FTABB](http://ftabb.intranet.bb.com.br/#/configurando-ambiente/ambiente-web), e seguir orientações da documentação para a instalação dos drivers.

#### Firefox 68:
- Baixá-lo diretamente do repositório oficial [Firefox 68 ESR](https://ftp.mozilla.org/pub/firefox/releases/68.0.2esr/), a versão correta para o seu Sistema Operacional.

#### Firefox 67:
- Baixá-lo diretamente do repositório oficial [Firefox 67.0.3](https://ftp.mozilla.org/pub/firefox/releases/67.0.3/), a versão correta para o seu Sistema Operacional.

### Executando o teste:
    1. #### Obtendo o .jar:
        - Acesse o [ATF Artifacotry](http://atf.intranet.bb.com.br/artifactory/webapp/#/home) e faça o seu login;
        - Clique
          neste [link](http://atf.intranet.bb.com.br/artifactory/webapp/#/artifacts/browse/tree/General/bb-maven-local/br/com/bb/sol/nome-do-projeto-testesautomatizados)
          para acessar o repositório deste projeto; e
        - Baixe a versão desejada do _"*.jar"_.
    2. #### Rodando a Automação:
        - Abra seu terminal e digite a seguinte linha de comando:
        - `java -cp .../<caminho relativo>/<nome-do-projeto-jar-baixado-anteriormente>.jar org.junit.runner.JUnitCore <pacote da runner>.<ClasseRunner>` (
          sem o _".java"_ no nome das classes);
        - Pressione "Enter"; e
        - Aguarde a mágica terminar.
___

#### Relatórios Allure na ARQ3 do BB:
- Caso esteja utilizando **VPN**, aplicar a seguinte configuração *"extra"* no **Eclipse**:
	- Com a IDE aberta, acesse os menus:
		- **Run => Run configurations... => Junit =>** Busque pela Classe Runner responsável por rodar a automação => Clique na aba *"Arguments"* => No campo *"VM Arguments"*, insira o seguinte código: `-ea -Dhttp.proxyHost=170.66.49.180 -Dhttp.proxyPort=3128` => Clique em *"Apply"* e depois em *"Close"*

### Rodar este teste via [qTeste](https://qteste.intranet.bb.com.br):

- Acessar os [Testes Selenium](https://qteste.intranet.bb.com.br/selenium/testes);
- Clique em **Selecione Componente da Aplicação**;
- Clique em **Outros Componentes**;
- Digite **nome-do-projeto-testesautomatizados** no campo de pesquisa e clique em **SELECIONAR**;
- Clique em **Selecione o Projeto de Teste**;
- Escolha a opção disponível. Geralmente algo como *"AAAA.0.X"*;
- Selecione a versão mais recente do Script de Teste;
- Selecione a versão do navegador que preferir;
- Na aba **Runners**, escolha o cenário de Teste que deseja executar;
- Clique em **EXECUTAR**; e
- Aguarde o resultado.

___
### Documentações gerais:

- ### Allure:
  - a Sigla SOL possui dois microsserviços para envio de evidências
  1. O [sol-container-docker-allure-testesautomatizados](https://fontes.intranet.bb.com.br/sol/sol-container-docker-allure-testesautomatizados),
  mais detalhes na [História 378841](https://genti.intranet.bb.com.br/ccm/web/projects/GECAP%202%20%28Change%20Management%29#action=com.ibm.team.workitem.viewWorkItem&id=374841); e
  2. O [sol-allure-sol-evidenciasindividuais](https://fontes.intranet.bb.com.br/sol/sol-allure-sol-evidenciasindividuais),
  mais detalhes na [História 527676](https://genti.intranet.bb.com.br/ccm/web/projects/GECAP%202%20%28Change%20Management%29#action=com.ibm.team.workitem.viewWorkItem&id=527676)