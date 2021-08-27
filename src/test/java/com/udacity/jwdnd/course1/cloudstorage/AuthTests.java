package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.core.Router;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private Router router;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		router = Router.getInstance(driver, port);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getUnauthorizedPage() {
		router.homePage();
		String defaultRedirectUrl = "http://localhost:" + this.port + "/login";
		Assertions.assertEquals(defaultRedirectUrl, driver.getCurrentUrl());
	}

	@Test
	public void getLoginPage() {
		LoginPage loginPage = router.loginPage();
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertEquals("Login", loginPage.mainHeading.getText());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = router.signupPage();
		Assertions.assertEquals("Sign Up", driver.getTitle());
		Assertions.assertEquals("Sign Up", signupPage.mainHeading.getText());
	}

	@Test
	public void SignupLoginLogoutUnauthorizedAccess() {
		getSignupPage();
		SignupPage signupPage = router.signupPage();
		User user = new User(null, "test_account", null,
				"testPa$$w0rd", "Mostafa", "Elsheikh");
		signupPage.signup(
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getPassword()
		);
		Assertions.assertTrue(signupPage.signupStatus(), signupPage.errorAlert.getText());
		LoginPage loginPage = router.loginPage();
		loginPage.login(
				user.getUsername(),
				user.getPassword()
		);
		HomePage homePage = router.homePage();
		homePage.logout();

		String defaultRedirectUrl = "http://localhost:" + this.port + "/login";
		Assertions.assertEquals(defaultRedirectUrl, driver.getCurrentUrl());
	}

}
