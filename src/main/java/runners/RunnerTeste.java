package runners;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import br.com.bb.ath.ftabb.gaw.Plataforma;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import utils.Utils;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "io.qameta.allure.cucumber2jvm.AllureCucumber2Jvm"},
        features = "classpath:features/", glue = "classpath:stepsDefinitions",
        snippets = SnippetType.CAMELCASE, tags = {""}, monochrome = true)
public class RunnerTeste {

    @AfterClass
    public static void finish() {
        logOutAndClose();
    }

    private static void logOutAndClose() {
        final Utils utils = new Utils();

       utils.logOut(true);
        Plataforma.fecharPlataforma();
    }
}
