package pagesObjects;

import br.com.bb.ath.ftabb.Pagina;
import br.com.bb.ath.ftabb.anotacoes.ElementoMae;
import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.ElementoInput;
import br.com.bb.ath.ftabb.gaw.AreaComumPlataforma;
import br.com.bb.ath.ftabb.gaw.AreaModalPlataforma;
import br.com.bb.ath.ftabb.gaw.IFrameEmulator;
import br.com.bb.ath.ftabb.gaw.PaginaPlataforma;

@AreaModalPlataforma
public class AreaTransacional {

    public AreaTransacional (){
        super();
    }

    @ElementoMae(value = "WIDGET_ID_3")
    @MapearElementoWeb(id = "cpfTit")
    public ElementoInput cpf;
}
