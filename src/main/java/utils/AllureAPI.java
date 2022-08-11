package utils;

import ath_allure_arq3.main.AllureARQ3;
import utils.enums.Environments;
import utils.enums.LogTypes;
import utils.enums.Property;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AllureAPI extends BaseUtils {
    private final String baseUrl;
    private final Environments env;

    public AllureAPI(Environments env) {
        this.env = env;
        baseUrl = env.get(Property.BASE_URL);
    }

    public void cleanResults(boolean isBeforeClass) {
        final String endPoint = "/clean-results";
        checkResponseCode(endPoint, get(endPoint), isBeforeClass);
        generateReport(isBeforeClass);
    }

    public void sendResults(AllureARQ3 allureARQ3, boolean isBeforeClass) {
        allureARQ3.enviarRelatorio(env.get(Property.SERVER));
        generateReport(isBeforeClass);
    }

    private int get(String endPoint) {
        try {
            final HttpURLConnection con = (HttpURLConnection) getURL(endPoint).openConnection();
            con.setRequestMethod("GET");
            final int code = con.getResponseCode();
            con.disconnect();

            return code;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private URL getURL(String endPoint) {
        try {
            final String spec = String.format("%s/allure-docker-service%s", baseUrl, endPoint);

            return new URL(spec);

        } catch (MalformedURLException mfue) {
            log(mfue.getMessage(), LogTypes.ERROR);
            return null;
        }
    }

    private void checkResponseCode(String endPoint, int responseCode, boolean isBeforeClass) {
        if (!isBeforeClass && responseCode != 200) {
            final String errMsg = String.format("A requisição para o endpoint \"%s\" falhou e retornou o código %d", endPoint, responseCode);
            log(errMsg, LogTypes.ERROR);
        }
    }

    private void generateReport(boolean isBeforeClass) {
        final String endPoint = "/generate-report";
        final int responseCode = get(endPoint);

        if (!isBeforeClass && responseCode == 200) {
            final String url = getURL("/latest-report").toString();
            final String msg = String.format("Favor, checar o relatório em %s", url);
            log(msg, LogTypes.INFO);

        } else
            checkResponseCode(endPoint, responseCode, isBeforeClass);
    }
}
