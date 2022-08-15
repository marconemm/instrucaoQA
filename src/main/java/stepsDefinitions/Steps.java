package stepsDefinitions;

import br.com.bb.ath.ftabb.exceptions.ElementoNaoLocalizadoException;
import br.com.bb.ath.ftabb.gaw.Plataforma;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import pagesObjects.AreaTransacional;
import utils.Constants;
import utils.JavaScript;
import utils.Utils;
import utils.enums.LogTypes;
import utils.enums.TimesAndReasons;

import java.util.Dictionary;
import java.util.List;

import static org.junit.Assert.*;

public class Steps {

	private Utils utils;
	private short count;
	private AreaTransacional areaTransacional;

	public Steps() {
		utils = new Utils();
		count = 0;
		areaTransacional = new AreaTransacional();
	}

	@Dado("^que a Plataforma esteja fechada, abra a Plataforma$")
	public void que_a_Plataforma_esteja_fechada_abra_a_Plataforma() {
		System.setProperty(Constants.IS_LOGGED, String.valueOf(Plataforma.estaLogado()));
		final boolean isLogged = Boolean.parseBoolean(System.getProperty(Constants.IS_LOGGED));

		if (isLogged)
			utils.log("A Plataforma esta aberta.", LogTypes.INFO);
		else
			Plataforma.abrirPlataforma();

	}

	@Dado("^se nao estiver logado, realiza o login no Sistema SOL com a chave (\\d+)$")
	public void realizeOLoginNoSistemaSOL(int key) {
		try {
			final Constants.CHAVES cKey = getKey(key);
			final Dictionary<String, String> datapool = utils.getDatapool(cKey);
			boolean isLogged = Boolean.parseBoolean(System.getProperty(Constants.IS_LOGGED));

			if (isLogged)
				utils.log("Usuário " + datapool.get("chave") + " esta logado.", LogTypes.INFO);

			else {
				final String xpath = "//input[@id='idToken1']";
				WebElement elem = utils.waitElementToRender(By.xpath(xpath), TimesAndReasons.LOAD_LOGIN);
				elem.sendKeys(datapool.get("chave"));

				elem = utils.findElement(By.xpath("//input[@id='idToken2']"));
				elem.sendKeys(datapool.get("senha"));

				clickOnBtnLogin();

				try {
					elem = utils.waitElementToRender(By.xpath("//input[@id='idToken3']"), TimesAndReasons.TO_RENDER);
					if (elem != null)
						elem.sendKeys("111111");

					clickOnBtnLogin();
				} catch (TimeoutException toe) {
					// do nothing.
				}

				try {
					utils.waitURLUpdate();
					count = 0;

				} catch (TimeoutException toe) {
					if (count++ < Constants.MAX_SEARCH_ATTEMPTS) {
						final String msg = "O login não foi concluído com sucesso!";
						utils.log(msg, LogTypes.ERROR);

						utils.getDriver().navigate().refresh();
						realizeOLoginNoSistemaSOL(key);
					}
				}

				while (!isLogged) {
					utils.wait(TimesAndReasons.LOAD_PLAT);

					if (++count == Constants.MAX_SEARCH_ATTEMPTS) {
						final String msg = "Não foi possível carregar a Plataforma após o login.";
						assertNotEquals(msg, ++count, Constants.MAX_SEARCH_ATTEMPTS);

						Plataforma.fecharPlataforma();
						System.exit(0);
					}

					System.setProperty(Constants.IS_LOGGED, String.valueOf(Plataforma.estaLogado()));
					isLogged = Boolean.parseBoolean(System.getProperty(Constants.IS_LOGGED));
				}

				utils.log("Login realizado com a chave: " + datapool.get("chave"), LogTypes.INFO);
				count = 0;
			}
		} catch (TimeoutException toe) {
			if (++count <= Constants.MAX_SEARCH_ATTEMPTS) {
				utils.log("Recarregando a página antes de uma nova tentativa de login.", LogTypes.ERROR);
				utils.getDriver().navigate().refresh();
				realizeOLoginNoSistemaSOL(key);
			}
		}
	}

	@Então("^logar o título da aplicação aberta$")
	public void logarOTítuloDaAplicaçãoAberta() throws ElementoNaoLocalizadoException {
		utils.log(Plataforma.recuperarTituloPagina(), LogTypes.INFO);
	}


	@Dado("^verificar se o nome do titular é \"([^\"]*)\"$")
	public void verificarSeONomeDoTitularÉ(String arg1) {
		final WebElement nome = utils.findElement(By.xpath("//*[@id=\"p-panel-1-content\"]/div/div/div[3]/span[2]"));
		assertEquals(arg1, nome.getText());


	}

	@E("^feche o modal inicial$")
	public void fecheOModalInicial() {
		final boolean isInContext = System.getProperty(Constants.CLIENT_MCI) != null;
		if (!isInContext) {
			try {
				final By by = By.cssSelector("div.modal__close");
				utils.waitElementToRender(by, TimesAndReasons.PAGE_LOAD).click();
				count = 0;

			} catch (TimeoutException toe) {
				if (count++ < Constants.MAX_SEARCH_ATTEMPTS)
					fecheOModalInicial();

				fail(toe.getMessage());
			}
		}
	}


	@E("^seleciona ag (\\d+) e cc (\\d+)$")
	public void selecionaAgECc(int ag, int cc) {
		final String locator = "//tbody/tr/td";

		final List<WebElement> tdElList = utils.findElements(By.xpath(locator));
		final short STEP_INCREMENT = 5;

		utils.capturaTela();
		assertNotNull(tdElList);

		for (int tdIndex = 0; tdIndex < tdElList.size(); tdIndex += STEP_INCREMENT) {
			WebElement tdEl = tdElList.get(tdIndex);
			String debug = tdEl.getText();
			final int agFound = Integer.parseInt(debug);
			final boolean isOnLastTableRow = (tdIndex >= tdElList.size() - STEP_INCREMENT);

			if (agFound == ag) {
				tdEl = tdElList.get(tdIndex + 1);
				final int ccFound = Integer.parseInt(tdEl.getText());

				if (ccFound == cc) {
					tdEl.click();
					break;
				}
			}

			if (isOnLastTableRow) {
				final String message = "A \"agência " + ag + " e conta " + cc + "\" não foi localizada na tabela de " +
						"resultados.";
				utils.log(message, LogTypes.ERROR);
				fail(message);
			}
		}
	}

	private Constants.CHAVES getKey(int key) {
		switch (key) {
			case 1:
				return Constants.CHAVES.F_1;
			case 2:
				return Constants.CHAVES.F_2;
			default:
				return Constants.CHAVES.F_3;
		}
	  }

	private void clickOnBtnLogin() {
		try {
			final WebElement elem = utils.waitElementToRender(By.xpath("//input[@id='loginButton_0']"), TimesAndReasons.PAGE_LOAD);
			elem.click();
			count = 0;

		} catch (TimeoutException toe) {
			if (++count <= Constants.MAX_SEARCH_ATTEMPTS)
				clickOnBtnLogin();
			else
				fail("O botão \"Login\" não foi carregado corretamente");
		}
	}

	@E("^abrir aplicacao \"([^\"]*)\" \"([^\"]*)\"$")
	public void abrirAplicacao(String arg0, String arg1) throws ElementoNaoLocalizadoException {
		Plataforma.abrirMenu(arg0, arg1);
	}

	@E("^verifica se o breadcrumb exibido é \"([^\"]*)\"$")
	public void verificaSeOBreadcrumbExibidoE(String arg0) {
		final String xPath = String.format("//span[@class='head__context']/span[contains(text(),'%s')]", arg0);
		final WebElement elem = utils.findElement(By.xpath(xPath));

		assertNotNull(String.format("Não possível localizar um elemento utilizando o xPath \"%s\"", xPath), elem);
		utils.capturaTela();
	}

	@E("^abrir submenu \"([^\"]*)\" \"([^\"]*)\"$")
	public void abrirSubmenu(String arg0, String arg1) {
		utils.openSubMenu(arg0, arg1);
	}

	@Então("^contextualize a ag \"([^\"]*)\" e cc \"([^\"]*)\"$")
	public void contextualizeAAgECc(String arg0, String arg1) throws ElementoNaoLocalizadoException {
		Plataforma.pesquisarModalClientePorAgenciaConta(arg0, arg1);
	}

	@Quando("^ordenar ag e cc$")
	public void ordenarAgECc() {
		utils.silentWait(2);
		final WebElement elem = utils.findElement(By.xpath("//*[@id=\"wrapper\"]/div/div/sol-contas-cliente/div/p-table/div/div/table/thead/tr/th[2]"));
		elem.click();
		utils.capturaTela();
	}

	@E("^clicar no radio button Todos$")
	public void clicarNoRadioButton() {
		utils.silentWait(2);
		final WebElement elem = utils.findElement(By.xpath("(//p-radiobutton)[1]"));
		JavaScript click_botao = new JavaScript();
		click_botao.clickWithJS(elem);
	}

	@E("^verifica se o botão \"([^\"]*)\" inicia \"([^\"]*)\"$")
	public void verificaSeOBotaoInicia(String arg0, String arg1) {
		String xpath = String.format("//button[@label='%s']", arg0);
		final WebElement elem = utils.findElement(By.xpath(xpath));
		if (arg1.intern().equals("ativado")) {
			assertTrue(elem.isEnabled());
		}
		else {
			assertFalse(elem.isEnabled());
		}
	}

	@Então("^logar os registros encontrados$")
	public void logarOsRegistrosEncontrados() {
		final String locator = "//tbody/tr/td";

		final List<WebElement> tdElList = utils.findElements(By.xpath(locator));
		final short STEP_INCREMENT = 5;

		utils.capturaTela();
		assertNotNull("Não foi encontrado nenhum dado na tabela", tdElList);

		utils.log(null, LogTypes.START);
		for (int tdIndex = 0; tdIndex < tdElList.size(); tdIndex += STEP_INCREMENT) {

			WebElement tdEl = tdElList.get(tdIndex);
			String texto = String.format("Renavam: %s", tdEl.getText());
			utils.log(texto, LogTypes.TABLE);

			tdEl = tdElList.get(tdIndex + 1);
			texto = String.format("Placa: %s", tdEl.getText());
			utils.log(texto, LogTypes.TABLE);

			tdEl = tdElList.get(tdIndex + 2);
			texto = String.format("Cadastramento: %s", tdEl.getText());
			utils.log(texto, LogTypes.TABLE);

			tdEl = tdElList.get(tdIndex + 3);
			texto = String.format("Situação: %s", tdEl.getText());
			utils.log(texto, LogTypes.TABLE);

			tdEl = tdElList.get(tdIndex + 4);
			texto = String.format("Data da Situação: %s", tdEl.getText());
			utils.log(texto, LogTypes.TABLE);
		}
		utils.log(null, LogTypes.END);
	}

}
