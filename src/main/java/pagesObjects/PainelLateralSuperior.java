package pagesObjects;

import br.com.bb.ath.ftabb.Pagina;
import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.Elemento;
import br.com.bb.ath.ftabb.elementos.ElementoBotao;
import br.com.bb.ath.ftabb.gaw.AreaLateralSuperiorPlataforma;

@AreaLateralSuperiorPlataforma
public class PainelLateralSuperior extends Pagina {

    @MapearElementoWeb(xPath = "//span[text()='CPF']/..")
    public Elemento labelCPF;

    @MapearElementoWeb(xPath = "//aside//button/i")
    public Elemento btnShowEndSec;

    @MapearElementoWeb(xPath = "//span[text()='Encerrar Consulta']")
    public ElementoBotao btnEndSec;

}
