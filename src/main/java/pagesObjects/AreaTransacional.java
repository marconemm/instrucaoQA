package pagesObjects;

import br.com.bb.ath.ftabb.Pagina;
import br.com.bb.ath.ftabb.anotacoes.MapearElementoWeb;
import br.com.bb.ath.ftabb.elementos.ElementoInput;
import br.com.bb.ath.ftabb.gaw.PaginaPlataforma;


public class AreaTransacional extends PaginaPlataforma {

    public AreaTransacional (){

    }

    @MapearElementoWeb(id = "cpfTit")
    public ElementoInput cpf;


}
