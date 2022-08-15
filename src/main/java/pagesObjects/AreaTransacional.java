package pagesObjects;

import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.Elemento;
import br.com.bb.ath.ftabb.gaw.AreaComumPlataforma;
import br.com.bb.ath.ftabb.gaw.AreaModalPlataforma;

@AreaComumPlataforma
public class AreaTransacional {

    public AreaTransacional (){
        super();
    }

    @MapearElementoWeb(xPath = "//th[@psortablecolumn='conta']")

    public Elemento ordenar;
}
