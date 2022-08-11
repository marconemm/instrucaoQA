package stepsDefinitions;

import br.com.bb.ath.ftabb.exceptions.ElementoNaoLocalizadoException;
import br.com.bb.ath.ftabb.gaw.Plataforma;
import cucumber.api.java.After;

public class Hooks {

	public Hooks() {

	}

	@After
	public void tearDown() {
		try {
			final boolean estaLogado = Plataforma.estaLogado();

			if (estaLogado) {
				if (!Plataforma.recuperarTituloPagina().intern().equals("Home (hm)")) {
					Plataforma.fecharPaginaAtual();
				}
			}
		} catch (ElementoNaoLocalizadoException enl) {
			System.err.println("Um elemento não foi localizado.");
			System.err.println("Mensagem: " + enl.getMessage());
			enl.printStackTrace();
		}
	}

}
