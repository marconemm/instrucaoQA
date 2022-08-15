package pagesObjects;

import org.openqa.selenium.WebElement;
import utils.annotations.FindBy;

import java.util.List;

public class PageObjects extends BasePageObjects {

    public PageObjects() {
    }

    @FindBy(id = "input-agencia")
    private WebElement inputAg;

    @FindBy(cssSelector = "#idsidebarbtn > i")
    private WebElement btnHideShowPainel;

    @FindBy(xPath = "//span[contains(text(),'Conta')]/../input")
    private WebElement inputCc;

    @FindBy(xPath = "//span[contains(text(),'Variação')]/../input")
    private WebElement inputVar;

    @FindBy(xPath = "//input[@inputmode='decimal']")
    private WebElement inputValor;

    @FindBy(xPath = "//label[text()='Data Futura']/../p-calendar/span/input")
    private WebElement inputFutureDate;

    @FindBy(xPath = "//p-radiobutton/*/*/input")
    private List<WebElement> rbList;

    @FindBy(xPath = "//input[@type='radio']")
    private List<WebElement> inputRbList;

    @FindBy(xPath = "//tbody/tr/td")
    private List<WebElement> tdList;

    @FindBy(xPath = "//input[@type='checkbox']")
    private List<WebElement> checkBoxList;

    public WebElement getInputAg() {
        if (inputAg == null)
            inputAg = setElement("inputAg");

        return inputAg;
    }

    public WebElement getBtnHideShowPainel() {
        if (btnHideShowPainel == null)
            btnHideShowPainel = setElement("btnHideShowPainel");

        return btnHideShowPainel;
    }

    public WebElement getInputCc() {
        if (inputCc == null)
            inputCc = setElement("inputCc");

        return inputCc;
    }

    public WebElement getInputVar() {
        if (inputVar == null)
            inputVar = setElement("inputVar");

        return inputVar;
    }

    public WebElement getInputValue() {
        if (inputValor == null)
            inputValor = setElement("inputValor");

        return inputValor;
    }

    public WebElement getInputFutureDate() {
        if (inputFutureDate == null)
            inputFutureDate = setElement("inputFutureDate");

        return inputFutureDate;
    }

    public List<WebElement> getRbList() {
        if (rbList == null)
            rbList = setElementList("rbList");

        return rbList;
    }

    public List<WebElement> getInputRbList() {
        if (inputRbList == null)
            inputRbList = setElementList("inputRbList");

        return inputRbList;
    }

    public List<WebElement> getTdList() {
        if (tdList == null)
            tdList = setElementList("tdList");

        return tdList;
    }

    public List<WebElement> getCheckBoxList() {
        if (checkBoxList == null)
            checkBoxList = setElementList("checkBoxList");

        return checkBoxList;
    }

    public void reset() {
        inputRbList = null;
        tdList = null;
    }
}