package utils;

import org.openqa.selenium.WebElement;

public class JavaScript extends BaseUtils {
    public JavaScript() {
    }

    public void scrollTo(WebElement elem) {
        jsExecuteScript("arguments[0].scrollIntoView();", elem);
        silentWait(1);
    }

    public void clickWithJS(WebElement elem) {
        jsExecuteScript("arguments[0].click();", elem);
    }

    public void focusOn(WebElement elem) {
        jsExecuteScript("arguments[0].focus();", elem);
    }

    public String getCurrentIframeId() {
        return jsExecuteScript("return self.name");
    }

    public void scrollToPageTop() {
        if (getCurrentIframeId().length() > 0)
            getDriver().switchTo().defaultContent();

        jsExecuteScript("window.scrollTo(0, 0);");
        silentWait(1);
    }

    public void scrollToPageBottom() {
        jsExecuteScript("window.scrollTo(0, document.body.scrollHeight);");
        silentWait(1);
    }
}
