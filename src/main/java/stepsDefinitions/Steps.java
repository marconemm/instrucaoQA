package stepsDefinitions;

import br.com.bb.ath.ftabb.exceptions.ElementoNaoLocalizadoException;
import br.com.bb.ath.ftabb.gaw.Plataforma;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Então;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import pagesObjects.AreaTransacional;
import utils.Constants;
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
}
