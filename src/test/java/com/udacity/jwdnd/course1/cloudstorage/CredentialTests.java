package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inUsername = driver.findElement(By.xpath("//*[@id=\"inputUsername\"]"));
		WebElement inPassword = driver.findElement(By.xpath("//*[@id=\"inputPassword\"]"));
		WebElement btnLogin = driver.findElement(By.xpath("/html/body/div/form/button"));
		inUsername.sendKeys("test_account");
		inPassword.sendKeys("testPa$$w0rd");
		btnLogin.click();
		Assertions.assertEquals(driver.getCurrentUrl(), "http://localhost:" + this.port + "/home");
	}

	@Test
	public void addCredential() {
		login();
		WebElement credentialsTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		credentialsTab.click();

		WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"nav-credentials\"]/button")));
		btnAdd.click();

		WebElement inUrl = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credential-url\"]")));
		WebElement inUsername = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credential-username\"]")));
		WebElement inPassword = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"credential-password\"]")));

		String oldUrl = inUrl.getText();
		String oldUsername = inUsername.getText();
		String oldPassword = inPassword.getText();
		oldPassword = oldPassword.isEmpty() ? inPassword.getAttribute("value") : oldPassword;

		String url = "www.super.com";
		String username = "duper";
		String password = "drive";
		inUrl.sendKeys(url);
		inUsername.sendKeys(username);
		inPassword.sendKeys(password);

		WebElement btnSave = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
		btnSave.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		credentialsTab = driver.findElement(By.xpath("//*[@id=\"nav-credentials-tab\"]"));
		credentialsTab.click();

		WebElement lastUrl = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/th)[last()]"))));
		WebElement lastUsername = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[2])[last()]"))));
		WebElement lastPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[2])[last()]"))));

		String newUrl = lastUrl.getText();
		String newUsername = lastUsername.getText();
		String newEncryptedPassword = lastPassword.getText();
		Assertions.assertEquals(url, newUrl);
		Assertions.assertEquals(username, newUsername);
		Assertions.assertNotEquals(password, newEncryptedPassword);

		WebElement btnEditLast = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button)[last()]"))));
		btnEditLast.click();

		inPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("#credential-password"))));
		String newPassword = inPassword.getText();
		newPassword = newPassword.isEmpty() ? inPassword.getAttribute("value") : newPassword;
		Assertions.assertEquals(password, newPassword);
		Assertions.assertNotEquals(oldUrl, newUrl);
		Assertions.assertNotEquals(oldUsername, newUsername);
		Assertions.assertNotEquals(oldPassword, newPassword);
	}

	@Test
	public void editCredential() {
		login();
		WebElement credentialsTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		credentialsTab.click();

		WebElement btnEditLast = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button)[last()]"))));
		btnEditLast.click();

		WebElement inUrl = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credential-url\"]"))));
		WebElement inUsername = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credential-username\"]"))));
		WebElement inPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/div/div/div[2]/form/div[3]/input"))));

		String oldUrl = inUrl.getText();
		String oldUsername = inUsername.getText();
		String oldPassword = inPassword.getText();
		oldPassword = oldPassword.isEmpty() ? inPassword.getAttribute("value") : oldPassword;

		String url = String.format("modify%d.duper.com", new Random().nextInt(101));
		String username = "super" + new Random().nextInt(101);
		String password = "drive" + new Random().nextInt(101);
		inUrl.clear();
		inUrl.sendKeys(url);
		inUsername.clear();
		inUsername.sendKeys(username);
		inPassword.clear();
		inPassword.sendKeys(password);

		WebElement btnSave = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
		btnSave.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		credentialsTab = driver.findElement(By.xpath("//*[@id=\"nav-credentials-tab\"]"));
		credentialsTab.click();

		WebElement lastUrl = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/th)[last()]"))));
		WebElement lastUsername = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[2])[last()]"))));
		WebElement lastPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[3])[last()]"))));
		String newUrl = lastUrl.getText();
		String newUsername = lastUsername.getText();
		String newEncryptedPassword = lastPassword.getText();
		Assertions.assertEquals(url, newUrl);
		Assertions.assertEquals(username, newUsername);
		Assertions.assertNotEquals(password, newEncryptedPassword);

		btnEditLast = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button)[last()]"))));
		btnEditLast.click();

		inPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("#credential-password"))));
		String newPassword = inPassword.getText();
		newPassword = newPassword.isEmpty() ? inPassword.getAttribute("value") : newPassword;
		Assertions.assertEquals(password, newPassword);
		Assertions.assertNotEquals(oldUrl, newUrl);
		Assertions.assertNotEquals(oldUsername, newUsername);
		Assertions.assertNotEquals(oldPassword, newPassword);
	}

	@Test
	public void deleteNote() {
		login();
		WebElement credentialsTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-credentials-tab\"]")));
		credentialsTab.click();

		List<WebElement> btnDelete = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button"));
		int lastIdx = btnDelete.size() - 1;
		WebElement btnDeleteLast = wait.until(ExpectedConditions.elementToBeClickable(btnDelete.get(lastIdx)));
		btnDeleteLast.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		credentialsTab = driver.findElement(By.xpath("//*[@id=\"nav-credentials-tab\"]"));
		credentialsTab.click();

		btnDelete = driver.findElements(By.xpath("(//*[@id=\"credentialTable\"]/tbody/tr/td[1]/form/button)[last()]"));
		Assertions.assertEquals(btnDelete.size(), lastIdx);
	}

}
