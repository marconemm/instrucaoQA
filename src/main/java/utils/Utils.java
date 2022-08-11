package utils;

import br.com.bb.ath.ftabb.exceptions.DataPoolException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.enums.LogTypes;
import utils.enums.TimesAndReasons;

import javax.management.AttributeNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class Utils extends BaseUtils {
    private final JavaScript js;

    public Utils() {
        js = new JavaScript();
    }

    public WebElement waitElementToRender(By by, TimesAndReasons tar) throws TimeoutException {
        try {
            if (by == null) throw new AttributeNotFoundException("\"by\" attribute mustn't be \"null\".");

            final WebDriverWait wait = new WebDriverWait(getDriver(), tar.getDelay());

            waitLog(tar);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));

            return findElement(by);

        } catch (AttributeNotFoundException anfe) {
            errorLog(anfe);
            return null;
        }
    }

    public void waitElementToDesapear(WebElement elem) throws TimeoutException {
        if (elem != null) {
            final WebDriverWait wait = new WebDriverWait(getDriver(), TimesAndReasons.PAGE_LOAD.getDelay());

            waitLog(TimesAndReasons.PAGE_LOAD);
            wait.until(ExpectedConditions.invisibilityOf(elem));
        }
    }

    public String waitPageLoad(By by, int startWaitAttempt) {
        String iframeId = "";
        String errMsg;

        if (startWaitAttempt < Constants.MAX_SEARCH_ATTEMPTS) {
            try {
                if (findElement(by) != null) {
                    iframeId = js.getCurrentIframeId();
                    getDriver().switchTo().defaultContent();
                    getDriver().switchTo().frame(iframeId);
                    waitElementToRender(by, TimesAndReasons.PAGE_LOAD);
                    getDriver().switchTo().defaultContent();
                    setSearchAttempt(0);

                } else {
                    wait(TimesAndReasons.PAGE_LOAD);
                    waitPageLoad(by, ++startWaitAttempt);
                }
            } catch (TimeoutException toe) {
                if (searchAttempt++ < Constants.MAX_SEARCH_ATTEMPTS)
                    waitPageLoad(by, startWaitAttempt);

                errMsg = "Houve algum erro no carregamento da página.";

                capturaTela();
                fail(String.format("%s %s", errMsg, toe.getMessage()));
            }
        } else if (startWaitAttempt < Constants.MAX_SEARCH_ATTEMPTS + 1) {
        //    rollback();
            waitPageLoad(by, 0);

        } else {
            final int delay = (int) (Constants.MAX_SEARCH_ATTEMPTS * TimesAndReasons.PAGE_LOAD.getDelay());
            errMsg = String.format("Um elemento não foi localizado mesmo depois de %d segundos de espera.", delay);
            capturaTela();
            fail(errMsg);
        }

        return iframeId;
    }

    public void waitURLUpdate() throws TimeoutException {
        final TimesAndReasons tar = TimesAndReasons.UPDATE_URL;
        final WebDriverWait wait = new WebDriverWait(getDriver(), tar.getDelay());

        waitLog(tar);
        wait.until(ExpectedConditions.urlToBe("https://plataforma.hm.bb.com.br/gaw/v3/"));
    }

    public void oTituloEIgual(String title) {
        try {
            final String xPath = String.format("//h1[contains(text(),'%s')]", title);
            final WebElement h1El = findElement(By.xpath(xPath));
            final String msg;

            assertNotNull(Constants.getNullMsg(xPath), h1El);

            if (!h1El.getText().intern().equals(title.intern())) {
                msg = String.format("O título buscado é: \"%s\", mas o título recuperado foi: \"%s\"", title, h1El.getText());
                throw new NoSuchElementException(msg);
            }

            capturaTela();

        } catch (NoSuchElementException nsee) {
            treatEX(nsee);
        }
    }

    public void sendKeysTo(WebElement input, String txt, boolean isDateInput) {
        boolean isEmpty = input.getText().length() == 0;

        try {
            if (isEmpty)
                isEmpty = input.getAttribute("value").length() == 0;

        } catch (Exception e) {
            log(e.getMessage(), LogTypes.ERROR);
        }

        try {
            if (isEmpty)
                isEmpty = !input.getAttribute("class").contains("p-filled");

        } catch (Exception e) {
            log(e.getMessage(), LogTypes.ERROR);
        }

        if (isDateInput && !isEmpty) {
            input.click();

            final String xPath = "//span[text()='Limpar']/..";
            final WebElement clearEl = waitElementToRender(By.xpath(xPath), TimesAndReasons.OPEN_CALENDAR);
            js.scrollTo(clearEl);
            clearEl.click();

        } else if (!isEmpty)
            input.clear();

        if (txt != null && txt.length() > 0) {
            final String[] split = txt.split("");

            for (String digit : split)
                input.sendKeys(digit);
        }
    }

    public Dictionary<String, String> getDatapool(Constants.CHAVES key) {
        try {
            final Dictionary<String, String> result = new Hashtable<>();
            String keyStr = "chave";

            result.put(keyStr, $(getDataPoolPath(key, keyStr)));

            keyStr = "senha";
            result.put(keyStr, $(getDataPoolPath(key, keyStr)));

            return result;

        } catch (DataPoolException dpe) {
            errorLog(dpe);
            return null;
        }
    }

    public void inputAceitaApenasNumeros(WebElement input, String label, int maxLength) {
        final String inputmode = input.getAttribute("inputmode");
        String message;
        int length;

        assertTrue(input.isDisplayed());

        sendKeysTo(input, "TexTo", false);
        message = String.format("O input \"%s\" não deveria aceitar texto.", label);
        length = input.getAttribute("value").length();
        assertEquals(message, 0, length);

        sendKeysTo(input, "!%@#!", false);
        message = String.format("O input \"%s\" não deveria aceitar caracteres especiais.", label);
        length = input.getAttribute("value").length();
        assertEquals(message, 0, length);

        sendKeysTo(input, "1234", false);
        if (inputmode != null && inputmode.intern().equals("decimal")) {
            message = String.format("O input \"%s\" não está aceitando números.", label);
            length = input.getAttribute("value").length();
            assertEquals(message, 8, length);

        } else {
            final boolean condition = input.getAttribute("value").length() <= maxLength;

            message = String.format("O input \"%s\" não está aceitando números.", label);
            assertTrue(message, condition);
        }

        sendKeysTo(input, "12345141251451323142341312412512434124234", false);
        message = String.format("O input \"%s\" aceita mais digitos do que deveria.", label);
        length = input.getAttribute("value").length();
        assertEquals(message, maxLength, length);

        sendKeysTo(input, "", false);
        length = input.getAttribute("value").length();
        assertEquals(String.format("O input \"%s\" não pôde ser limpo.", label), 0, length);
    }

    public void inputAceitaApenasDatas(WebElement input, String label, boolean isDateInput) {
        try {
            if (input == null) throw new AttributeNotFoundException("The attribute mustn't be [null]");

            final String data = "01/01/1999";
            final String xPath = "//span[text()='Limpar']/..";

            sendKeysTo(input, "TexTo", isDateInput);
            assertEquals("O input \"" + label + "\" não deveria aceitar texto.", 0, input.getAttribute("value").length());

            sendKeysTo(input, "!%@#!", isDateInput);
            assertEquals("O input \"" + label + "\" não deveria caracteres especiais.", 0, input.getAttribute("value").length());

            sendKeysTo(input, data, isDateInput);
            assertSame("O input \"" + label + "\" não foi preenchido corretamente.", input.getAttribute("value").intern(), data);

            capturarTela();
            input.click();
            final WebElement btnClear = findElement(By.xpath(xPath));

            assertNotNull(Constants.getNullMsg(xPath), btnClear);
            js.scrollTo(btnClear);
            btnClear.click();
            js.scrollToPageTop();

        } catch (AttributeNotFoundException anfe) {
            log(anfe.getMessage(), LogTypes.ERROR);
            fail(anfe.getMessage());
            anfe.printStackTrace();
        }
    }

    public WebElement findElement(By by) {
        try {
            if (iframesList.size() == 0)
                setIframesList();

            try {
                return getDriver().findElement(by);

            } catch (NoSuchElementException nsee) {
                iframesLogs(nsee);
                switchToNextIframe(iframesList.get(iframesCount++).split(SEPARATOR.intern()));

                return findElement(by);
            }
        } catch (WebDriverException | IndexOutOfBoundsException ex) {
            treatEX(ex);

            if (searchAttempt <= Constants.MAX_SEARCH_ATTEMPTS)
                return findElement(by);

            return null;
        }
    }

    public List<WebElement> findElements(By by) {
        try {
            if (iframesList.size() == 0)
                setIframesList();

            final List<WebElement> resultList = getDriver().findElements(by);
            final StringBuilder txt = new StringBuilder(String.valueOf(resultList.size()));
            boolean isResultListEmpty = resultList.size() == 0;

            try {
                if (isResultListEmpty) {
                    final String message = "Element info: " + by + "Element info: ";
                    throw new NoSuchElementException(message);
                }

                if (resultList.size() > 1) txt.append(" elementos foram encontrados ");
                else txt.append(" elemento foi encontrado ");

                txt.append("com o localizador \"");
                txt.append(by);
                txt.append("\" no iframe \"");

                if (iframesList.size() > 0) txt.append(iframesList.get(iframesCount));
                else txt.append("PRINCIPAL_MAIN");

                txt.append("\".");

                log(txt.toString(), LogTypes.INFO);

                return resultList;

            } catch (NoSuchElementException nsee) {
                iframesLogs(nsee);
                switchToNextIframe(iframesList.get(iframesCount++).split(SEPARATOR.intern()));

                if (searchAttempt <= Constants.MAX_SEARCH_ATTEMPTS)
                    return findElements(by);

                return null;
            }
        } catch (WebDriverException | IndexOutOfBoundsException ex) {
            treatEX(ex);

            return findElements(by);
        }
    }

    public void openMenu(String menu1, String menu2) {
        js.scrollToPageTop();
        silentWait(1);
        String xPath = getMenuXpath(menu1);
        js.scrollTo(findElement(By.xpath(xPath)));
        moveMouseOver(findElement(By.xpath(xPath)));
        silentWait(1);

        xPath = getMenuXpath(menu2);
        final WebElement menuEl = findElement(By.xpath(xPath));
        js.clickWithJS(menuEl);
        silentWait(1);
    }

    public void openSubMenu(String menu, String subMenu) {
        String xPath = String.format("//ul/span/li/a/span[contains(text(),'%s')]", menu);
        WebElement menuEl = findElement(By.xpath(xPath));

        js.scrollTo(menuEl);
        silentWait(1);
        moveMouseOver(menuEl);
        silentWait(1);

        xPath = String.format("//ul[@role='menu']/li/a/span[text()='%s']", subMenu);
        menuEl = findElement(By.xpath(xPath));

        assertNotNull(Constants.getNullMsg(xPath), menuEl);
        moveMouseOver(menuEl);
        silentWait(1);
        menuEl.click();

        wait(TimesAndReasons.PAGE_LOAD);
    }

    public void marcarRadioButton(WebElement rb, String label) {
        String msg;
        if (rb.isSelected()) {
            msg = String.format("Radio button \"%s\" já estava marcado.", label);
            log(msg, LogTypes.INFO);

        } else {
            final JavascriptExecutor jse = (JavascriptExecutor) getDriver();

            jse.executeScript("arguments[0].click();", rb);

            final boolean isChecked = Boolean.parseBoolean(rb.getAttribute("aria-checked")) || rb.isSelected();
            msg = String.format("O elemento \"rb%s\" nao foi marcado corretamente.", label);
            assertTrue(msg, isChecked);

            msg = String.format("Radio button \"%s\" marcado.", label);
            log(msg, LogTypes.INFO);
        }
    }

    public void logOut(boolean hasModalToClose) {
        try {
            String selector = "//div[@class='modal__close']";
            if (hasModalToClose) {
                final WebElement elemCloseModal = waitElementToRender(By.xpath(selector), TimesAndReasons.TO_RENDER);
                elemCloseModal.click();
            }

            selector = "#perfil > a > div > figure > i";
            final WebElement btnProfile = findElement(By.cssSelector(selector));

            assertNotNull(Constants.getNullMsg(selector), btnProfile);
            js.scrollTo(btnProfile);
            btnProfile.click();


            selector = "//span[text()='Sair']";
            final WebElement txtExit = findElement(By.xpath(selector));

            assertNotNull(Constants.getNullMsg(selector), txtExit);
            txtExit.click();

            selector = "//input[@id='loginButton_0']";
            waitElementToRender(By.xpath(selector), TimesAndReasons.END_SEC);

            System.setProperty(Constants.IS_LOGGED, String.valueOf(false));
            System.clearProperty(Constants.CLIENT_MCI);
        } catch (TimeoutException toe) {
            new Utils().log("Não há modal para ser fechado antes do log out.", LogTypes.INFO);

            logOut(false);
        }
    }

    public void moveMouseOver(WebElement elem) {
        final Actions act = new Actions(getDriver());

        if (elem != null)
            act.moveToElement(elem).build().perform();

        else {
            final Dimension d = getDriver().manage().window().getSize();
            final int delta = 100;
            final int multiplier = (d.getHeight() > 800) ? 7 : 3;
            final int xOffset = d.getWidth() - delta;
            final int yOffset = d.getHeight() - (delta * multiplier);

            act.moveByOffset(xOffset, yOffset).build().perform();
        }

        silentWait(1);
    }

    public void moveMouseTo(Point point) {
        final Actions act = new Actions(getDriver());
        act.moveByOffset(point.getX(), point.getY());
        silentWait(1);
    }

    public void checkToastMsg(String msg) {
        final String xPath = "//span[contains(text(),'" + msg + "')]";
        final String message = "A mensagem contendo \"" + msg + "\" não foi exibida corretamente.";
        final String iframeID = waitPageLoad(By.xpath(xPath), 0);
        getDriver().switchTo().frame(iframeID);
        WebElement elem = findElement(By.xpath(xPath));
        pressKey(Keys.HOME);
        assertTrue(message, elem.isDisplayed());

        elem = findElement(By.cssSelector("#idsidebarbtn > i"));
        elem.click();
        elem.click();
        capturaTela();
    }

    public void checkToastMsgAndCloseIt(String msg) {
        checkToastMsg(msg);

        final String xPath = "//span[contains(text(),'" + msg + "')]/../button";
        final String message = "A mensagem \"" + msg + " não informada.\" não foi removida corretamente.";
        final WebElement elem = findElement(By.xpath(xPath));
        boolean isDisplayed;

        assertNotNull(Constants.getNullMsg(xPath), elem);
        elem.click();

        try {
            assertNotNull(Constants.getNullMsg(xPath), elem);
            isDisplayed = elem.isDisplayed();

        } catch (StaleElementReferenceException serE) {
            isDisplayed = false;
        }

        assertFalse(message, isDisplayed);

        capturaTela();
    }

    public void clearInput(WebElement inputCc) {
        final String message = "Um input não foi limpo corretamente";
        if (inputCc.getAttribute("value").length() > 0)
            inputCc.clear();

        final int actual = inputCc.getAttribute("value").length();

        assertEquals(message, 0, actual);
    }

    public void waitAnPrevTest() {
        final String msg = "Um teste anterior necessário para o sucesso dos demais não foi concluído com sucesso.";
        boolean isReady = System.getProperty(Constants.IS_READY) != null;
        isReady = isReady && Boolean.parseBoolean(System.getProperty(Constants.IS_READY));
        short maxWait = 0;

        while (!isReady && maxWait < Constants.MAX_WAIT_ATTEMPTS) {
            wait(TimesAndReasons.PREV_TEST);
            isReady = System.getProperty(Constants.IS_READY) != null;
            isReady = isReady && Boolean.parseBoolean(System.getProperty(Constants.IS_READY));
            maxWait++;
        }

        assertTrue(msg, isReady);
    }
}
