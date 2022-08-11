package utils;

import br.com.bb.ath.ftabb.FTABBContext;
import br.com.bb.ath.ftabb.utilitarios.FTABBUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import utils.enums.LogTypes;
import utils.enums.TimesAndReasons;

import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

public abstract class BaseUtils extends FTABBUtils {

    protected final String SEPARATOR;
    protected final List<String> iframesList;
    private final String MAIN_FRAME;
    protected short searchAttempt, iframesCount;
    private final TreeMap<String, String> dataMap;

    public BaseUtils() {
        searchAttempt = 1;
        iframesCount = 0;
        iframesList = new ArrayList<>();
        MAIN_FRAME = "MAIN_FRAME";
        SEPARATOR = "=>>";
        dataMap = new TreeMap<>();
        dataMap.put("Contas", "39050");
        dataMap.put("Cadastramentos e Autorizações", "39274");
        dataMap.put("Deferimentos e Pendências", "39052");
        dataMap.put("Cadastramentos e Autorizações p/ Movimentações", "39330");
        dataMap.put("Pagamentos", "39277");
        dataMap.put("Transferências Entre Contas", "39278");
        dataMap.put("Deferimentos (novo)", "39347");
        dataMap.put("PagamentosGPS", "39340");
        dataMap.put("Relatórios", "39055");
        dataMap.put("Autorizações Para Débito de Malote", "39395");
        dataMap.put("Autorizações Para Captura de Cheques", "39394");
        dataMap.put("Segurança", "39056");
        dataMap.put("Personalização - PLT3", "43087");
        dataMap.put("Personalização de Limites de Movimentação - PLT3", "43095");
    }

    public void setSearchAttempt(int searchAttempt) {
        this.searchAttempt = (short) searchAttempt;
    }

    public void resetSearchParams(int searchAttempt) {
        this.searchAttempt = (short) searchAttempt;
        iframesCount = 0;
        iframesList.clear();

        getDriver().switchTo().defaultContent();
    }

    public WebDriver getDriver() {
        return (WebDriver) FTABBContext.getContext().getContextBrowserDriver().getDriver();
    }

    public void log(String msg, LogTypes type) {
        if (msg == null) {
            if (type == LogTypes.START) System.out.println("\n    --- Log START ---");
            else System.out.println("    --- Log END ---\n");

        } else if (type == LogTypes.INFO) System.out.println("\n    INFO - " + msg);
        else if (type == LogTypes.ERROR) System.err.println("\n    ERRO - " + msg);
        else if (type == LogTypes.TABLE) System.out.println("    # - " + msg);
    }

    public void wait(TimesAndReasons tar) {
        if (tar.getReason().intern().equals("")) sleep(tar.getDelay());

        else if (tar.getDelay() > 0) {
            waitLog(tar);
            sleep(tar.getDelay());
        }
    }

    public void silentWait(int seconds) {
        long time = Long.parseLong(Integer.toString(seconds));
        sleep(time);
    }

    public void capturaTela() {
        wait(TimesAndReasons.CAP_SCRN);

        capturarTela();
        allureCapturarTela();

        log("Tela capturada.", LogTypes.INFO);
    }

    public void treatIAE(String message) {
        try {
            throw new InvalidAttributeValueException(message);

        } catch (InvalidAttributeValueException iae) {
            log(iae.getMessage(), LogTypes.ERROR);
            fail(iae.getMessage());
        }
    }

    public void pressKey(Keys key) {
        new Actions(getDriver()).sendKeys(key).build().perform();
    }

    public void switchTo(String iframeId) {
        getDriver().switchTo().frame(iframeId);
    }

    public void tearDown() {
        System.clearProperty(Constants.IS_READY);
    }

    protected void errorLog(Exception enl) {
        log(enl.getMessage() + "\n", LogTypes.ERROR);
        fail(enl.getMessage());
    }

    protected void waitLog(TimesAndReasons tar) {
        final String plural = (tar.getDelay() > 1) ? "(s)" : "";
        final StringBuilder txt = new StringBuilder("Aguardando ");

        if (!tar.isCompulsory()) txt.append("até ");

        txt.append(tar.getDelay());
        txt.append(" segundo");
        txt.append(plural);
        txt.append(" para ");
        txt.append(tar.getReason());
        txt.append("...");

        log(txt.toString(), LogTypes.INFO);
    }

    protected void allureCapturarTela() {
        final Object driver = FTABBContext.getContext().getContextBrowserDriver().getDriver();
        final ByteArrayInputStream byteArrInputStream = new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        final String uuid = UUID.randomUUID().toString().substring(0, 8);

        Allure.addAttachment("Print_" + uuid + ".png", byteArrInputStream);
    }

    protected String getMenuXpath(String key) {
        final StringBuilder xpath = new StringBuilder("//*[@data-item-id='");

        try {
            if (!dataMap.containsKey(key))
                throw new AttributeNotFoundException("O menu \"" + key + "\" ainda não foi configurado.");

            xpath.append(dataMap.get(key)).append("']");
        } catch (AttributeNotFoundException anfe) {
            errorLog(anfe);
        }
        return xpath.toString();
    }

    protected String getDataPoolPath(Constants.CHAVES key, String sufix) {
        final StringBuilder result = new StringBuilder("login_plataforma");

        try {
            if (key.equals(Constants.CHAVES.F_1)) {
                result.append(".chaveF_1.");
                result.append(sufix);

            } else if (key.equals(Constants.CHAVES.F_2)) {
                result.append(".chaveF_2.");
                result.append(sufix);

            } else if (key.equals(Constants.CHAVES.F_3)) {
                result.append(".chaveF_3.");
                result.append(sufix);

            } else if (key.equals(Constants.CHAVES.F_4)) {
                result.append(".chaveF_4.");
                result.append(sufix);
            } else throw new AttributeNotFoundException("Please inform an \"Constant\" valid attribute");


        } catch (AttributeNotFoundException anfe) {
            errorLog(anfe);
        }

        return result.toString();
    }

    protected void setIframesList() {
        getDriver().findElements(By.xpath("//iframe")).forEach(elem -> iframesList.add(elem.getAttribute("id")));

        seekIframesId(iframesList.size());

        if (iframesList.size() > 0) {
            final String msg = "Voltando para o \"MAIN_FRAME\"...";

            log(msg, LogTypes.INFO);
        }
    }

    protected void jsExecuteScript(String script, WebElement elem) {
        try {
            if (elem != null) {
                final JavascriptExecutor jse = (JavascriptExecutor) getDriver();
                jse.executeScript(script, elem);

            } else throw new InvalidAttributeValueException("Please, inform a valid \"WebElement\" attribute.");

        } catch (InvalidAttributeValueException iave) {
            errorLog(iave);
        }
    }

    protected String jsExecuteScript(String script) {
        final JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        return (String) jse.executeScript(script);
    }

    protected void iframesLogs(NoSuchElementException nsee) {
        final String elemInfo = nsee.getMessage().split("Element info: ")[1];
        final StringBuilder logTxt = new StringBuilder("A busca com as informações \"");
        logTxt.append(elemInfo).append("\"");

        if (searchAttempt <= Constants.MAX_SEARCH_ATTEMPTS) {
            logTxt.append(", não encontrou resultado(s) no iframe \"");

            if (iframesCount == 0)
                logTxt.append(MAIN_FRAME);
            else if (iframesCount == iframesList.size())
                logTxt.append(iframesList.get(iframesCount - 1)).append(" (último iframe)");
            else
                logTxt.append(iframesList.get(iframesCount - 1));

            logTxt.append("\"");
            log(logTxt.toString(), LogTypes.INFO);

            if (iframesCount < iframesList.size()) {
                logTxt.delete(0, logTxt.length());
                logTxt.append("Buscando o elemento com o(s) parâmetro(s) acima no iframe \"");
                logTxt.append(iframesList.get(iframesCount)).append("\"...");

                log(logTxt.toString(), LogTypes.INFO);
            }

        } else {
            logTxt.append(", foi realizada ").append(searchAttempt).append(" vezes e não encontrou resultado(s) válido(s).");

            log(logTxt.toString(), LogTypes.ERROR);
        }
    }

    protected void switchToNextIframe(String[] iframesIdsList) {
        getDriver().switchTo().defaultContent();

        for (String iframeId : iframesIdsList)
            getDriver().switchTo().frame(iframeId);
    }

    protected void treatEX(Exception ex) {
        if (searchAttempt < Constants.MAX_SEARCH_ATTEMPTS) {
            if (ex.getClass().equals(NoSuchFrameException.class))
                log("O mapeamento não iniciou no iframe inicial.", LogTypes.INFO);
            else if (ex.getClass().equals(WebDriverException.class))
                log("O mapeamento tentou mapear um elemento inválido.", LogTypes.INFO);
            else if (ex.getClass().equals(NoSuchElementException.class))
                log(ex.getMessage(), LogTypes.INFO);

        } else
            log("O limite de buscas foi atingido sem encontrar resultados.", LogTypes.ERROR);

        resetSearchParams(++searchAttempt);
    }

   
  

    private void seekIframesId(int initialValue) {
        final AtomicInteger prevSize = new AtomicInteger();
        final AtomicInteger currSize = new AtomicInteger(initialValue);

        while (prevSize.get() != currSize.get())
            for (int iframeIndex = 0; iframeIndex < iframesList.size(); iframeIndex++) {
                final String iframeId = iframesList.get(iframeIndex);
                prevSize.set(iframesList.size());

                if (iframeId.contains(SEPARATOR))
                    switchToNextIframe(iframeId.split(SEPARATOR.intern()));
                else
                    getDriver().switchTo().frame(iframeId);

                getDriver().findElements(By.xpath("//iframe")).forEach(elem -> {
                    String iframeIds = iframeId + SEPARATOR + elem.getAttribute("id");
                    iframesList.add(iframeIds);
                });

                currSize.set(iframesList.size());
                getDriver().switchTo().defaultContent();
            }
    }
}
