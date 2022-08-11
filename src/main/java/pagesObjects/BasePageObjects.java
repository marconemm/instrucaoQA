package pagesObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.Utils;
import utils.annotations.FindBy;
import utils.enums.LocatorType;

import java.lang.reflect.Field;
import java.util.List;

public abstract class BasePageObjects {

    private final Utils utils;
    private LocatorType locType;

    public BasePageObjects() {
        utils = new Utils();
    }


    protected WebElement setElement(String fieldName) {
        setLocatorType(fieldName);
        return findElementBy(locType, fieldName);
    }

    protected List<WebElement> setElementList(String fieldName) {
        setLocatorType(fieldName);
        return findElementListBy(locType, fieldName);
    }


    private void setLocatorType(String fieldName) {
        for (Field field : this.getClass().getDeclaredFields()) {

            if (isTheFieldAnnotated(field, fieldName)) {
                final FindBy findBy = field.getAnnotation(FindBy.class);

                if (findBy.id().length() != 0) {
                    assertEmpty(new String[]{findBy.xPath(), findBy.cssSelector()});
                    locType = LocatorType.ID;

                } else if (findBy.xPath().length() != 0) {
                    assertEmpty(new String[]{findBy.id(), findBy.cssSelector()});
                    locType = LocatorType.X_PATH;

                } else if (findBy.cssSelector().length() != 0) {
                    assertEmpty(new String[]{findBy.xPath(), findBy.id()});
                    locType = LocatorType.CSS_SELECTOR;

                } else
                    throw new RuntimeException("Please inform a valid [ LocatorTypes ] enum");
            }
        }
    }

    private void assertEmpty(String  [] args) {
        for (String arg : args)
            if (!arg.intern().equals(""))
                throw new RuntimeException("Please inform an unique locator [ id, xPath or cssSelector ] by field");
    }

    private  WebElement findElementBy( LocatorType locType, String fieldName) {
        for (Field field : this.getClass().getDeclaredFields()) {

            if (isTheFieldAnnotated(field, fieldName)) {
                final FindBy findBy = field.getAnnotation(FindBy.class);

                if (locType.equals(LocatorType.X_PATH))
                    return utils.findElement(By.xpath(findBy.xPath()));

                if (locType.equals(LocatorType.CSS_SELECTOR))
                    return utils.findElement(By.cssSelector(findBy.cssSelector()));

                if (locType.equals(LocatorType.ID))
                    return utils.findElement(By.id(findBy.id()));
            }
        }

        return null;
    }

    private  List<WebElement> findElementListBy(LocatorType locType, String fieldName) {
        for (Field field : this.getClass().getDeclaredFields()) {

            if (isTheFieldAnnotated(field, fieldName)) {
                final FindBy findBy = field.getAnnotation(FindBy.class);
                final String locator = findBy.xPath();

                if (locType.equals(LocatorType.X_PATH))
                    return utils.findElements(By.xpath(locator));

                if (locType.equals(LocatorType.CSS_SELECTOR))
                    return utils.findElements(By.cssSelector(locator));

                if (locType.equals(LocatorType.ID))
                    return utils.findElements(By.id(locator));
            }
        }

        return null;
    }

    private boolean isTheFieldAnnotated( Field field, String fieldName) {
        final boolean isTheField = field.getName().intern().equals(fieldName);
        final boolean isAnnotated = field.isAnnotationPresent(FindBy.class);

        return isTheField && isAnnotated;
    }
}
