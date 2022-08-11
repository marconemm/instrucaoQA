@Library(['aic-jenkins-sharedlib']) _

mavenBuildPipeline {
    nomePod                         = 'jdk8' // OPCIONAL identifica qual jdk o projeto utiliza, há opção de execução com o valor 'jdk11'
    habilitarValidacaoEstatica      = true // habilita a validação estática do código fonte
    habilitarPublicacao             = true // habilita a publicação do pacote no repositório corporativo
    habilitarConstrucao             = true // habilita a construção da aplicação
    habilitarSonar                  = true // habilita a execução do SonarQube Scanner
    habilitarEmpacotamento          = true // habilita o empacotamento da aplicação
    
    habilitarTestesIntegracao       = false // habilita a execução dos testes de integração
    habilitarTestesUnidade          = false // habilita a execução dos testes de unidade

    habilitarTestesFumaca           = false // habilita a execução dos testes de fumaça
    habilitarValidacaoSeguranca     = false // habilita a validação de segurança do código fonte
    habilitarEmpacotamentoDocker    = false // habilita o empacotamento da imagem docker e posterior publicação no repositório corporativo
    habilitarDebug                  = false // habilita debug
}
