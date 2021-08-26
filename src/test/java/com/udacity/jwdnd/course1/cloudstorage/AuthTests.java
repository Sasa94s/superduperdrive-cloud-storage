package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getUnauthorizedPage() {
		driver.get("http://localhost:" + this.port + "/home");
		String defaultRedirectUrl = "http://localhost:" + this.port + "/login";
		Assertions.assertEquals(defaultRedirectUrl, driver.getCurrentUrl());
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		String header = driver.findElement(By.xpath("/html/body/div/h1")).getText();
		Assertions.assertEquals("Login", header);
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		String header = driver.findElement(By.xpath("/html/body/div/h1")).getText();
		Assertions.assertEquals("Sign Up", header);
	}

	private void enterSignupInfo(User user) {
		WebElement inFirstName = driver.findElement(By.xpath("//*[@id=\"inputFirstName\"]"));
		WebElement inLastName = driver.findElement(By.xpath("//*[@id=\"inputLastName\"]"));
		WebElement inUsername = driver.findElement(By.xpath("//*[@id=\"inputUsername\"]"));
		WebElement inPassword = driver.findElement(By.xpath("//*[@id=\"inputPassword\"]"));
		WebElement btnSignup = driver.findElement(By.xpath("/html/body/div/form/button"));
		inFirstName.sendKeys(user.getFirstName());
		inLastName.sendKeys(user.getLastName());
		inUsername.sendKeys(user.getUsername());
		inPassword.sendKeys(user.getPassword());
		btnSignup.click();

		WebElement message = driver.findElement(By.xpath("/html/body/div/form/div[1]"));
		Assertions.assertTrue(message.getText().contains("You successfully signed up!"));
	}

	private void enterLoginInfo(User user) {
		WebElement inUsername = driver.findElement(By.xpath("//*[@id=\"inputUsername\"]"));
		WebElement inPassword = driver.findElement(By.xpath("//*[@id=\"inputPassword\"]"));
		WebElement btnLogin = driver.findElement(By.xpath("/html/body/div/form/button"));
		inUsername.sendKeys(user.getUsername());
		inPassword.sendKeys(user.getPassword());
		btnLogin.click();
		Assertions.assertEquals(driver.getCurrentUrl(), "http://localhost:" + this.port + "/home");
	}

	@Test
	public void SignupLoginLogoutUnauthorizedAccess() {
		getSignupPage();
		User user = new User(null, "test_account", null, "testPa$$w0rd", "Mostafa", "Elsheikh");
		enterSignupInfo(user);
		getLoginPage();
		enterLoginInfo(user);

		WebElement btnLogout = driver.findElement(By.xpath("//*[@id=\"logoutDiv\"]/form/button"));
		btnLogout.click();

		getUnauthorizedPage();
	}

}
