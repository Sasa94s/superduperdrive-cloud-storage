package com.udacity.jwdnd.course1.cloudstorage.core;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class Router {

    private static Router instance;
    private int port;

    private WebDriver driver;
    private final WebDriverWait wait;

    private String loginUrl;
    private String signupUrl;
    private String homeUrl;

    public static Router getInstance(WebDriver driver, int port) {
        if (instance == null) {
            instance = new Router(driver, port);
        }
        instance.driver = driver;
        instance.port = port;

        return instance;
    }

    private Router(WebDriver driver, int port) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        this.port = port;
        initUrls();
    }

    private void initUrls() {
        String baseUrl = "http://localhost:" + this.port;
        this.loginUrl = baseUrl + "/login";
        this.signupUrl = baseUrl + "/signup";
        this.homeUrl = baseUrl + "/home";
    }

    private String getCleanUrl(String url) {
        try {
            URI uri = new URI(url);
            return new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null,
                    null
            ).toString();
        } catch (URISyntaxException e) {
            return url;
        }
    }

    private boolean hasSameCurrentUrl(String url) {
        return getCurrentCleanUrl().equals(url);
    }

    public String getCurrentCleanUrl() {
        return getCleanUrl(driver.getCurrentUrl());
    }

    public LoginPage loginPage() {
        if (!hasSameCurrentUrl(loginUrl)) {
            driver.get(loginUrl);
        }

        return new LoginPage(driver);
    }

    public SignupPage signupPage() {
        driver.get(signupUrl);

        return new SignupPage(driver);
    }

    public HomePage homePage() {
        if (!hasSameCurrentUrl(homeUrl)) {
            driver.get(homeUrl);
        }

        return new HomePage(driver);
    }

    public NotePage notePage() {
        if (!hasSameCurrentUrl(homeUrl)) {
            driver.get(homeUrl);
        }
        WebElement notesTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-notes-tab\"]")));
        notesTab.click();

        return new NotePage(driver);
    }

    public CredentialPage credentialPage() {
        if (!hasSameCurrentUrl(homeUrl)) {
            driver.get(homeUrl);
        }
        WebElement credentialsTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-credentials-tab\"]")));
        credentialsTab.click();

        return new CredentialPage(driver);
    }

    public void defaultLogin(boolean doAssert) {
        LoginPage loginPage = loginPage();
        loginPage.login("test_account", "testPa$$w0rd");
        if (doAssert) {
            Assertions.assertEquals(driver.getCurrentUrl(), homeUrl);
        }
    }
}
