package pagesObjects;

import br.com.bb.ath.ftabb.Pagina;
import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.Elemento;
import br.com.bb.ath.ftabb.gaw.AreaLateralInferiorPlataforma;

@AreaLateralInferiorPlataforma
public class PainelLateralInferior extends Pagina {

    @MapearElementoWeb(xPath = "//header/h3")
    public Elemento tituloAtendimentos;

}
