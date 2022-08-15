package pagesObjects;

import br.com.bb.ath.ftabb.Pagina;
import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.Elemento;
import br.com.bb.ath.ftabb.gaw.AreaLateralSuperiorPlataforma;

@AreaLateralSuperiorPlataforma
public class PainelLateralDireito extends Pagina {

    @MapearElementoWeb(xPath = "//span[text()='CPF']/..")
    public Elemento labelCPF;
}
