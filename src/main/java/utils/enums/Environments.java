package utils.enums;

public enum Environments {

    DESENV("http://allure-sol-evidenciasindividuais.sol.desenv.bb.com.br"),
    HM("http://allure-sol-evidenciasindividuais.sol.hm.bb.com.br"),
    INTRANET("http://allure-sol-evidenciasindividuais.sol.intranet.bb.com.br");

    private final String baseUrl;
    private final String prop;

    Environments(String baseUrl) {
        this.baseUrl = baseUrl;

        if (this.baseUrl.contains("desenv"))
            prop = "allure.sol-desenv.url";
        else if (this.baseUrl.contains("hm"))
            prop = "allure.sol-hm.url";
        else
            prop = "allure.sol-intranet.url";
    }

    public String get(Property prop) {
        if (prop.equals(Property.SERVER))
            return this.prop;
        else
            return baseUrl;

    }
}
