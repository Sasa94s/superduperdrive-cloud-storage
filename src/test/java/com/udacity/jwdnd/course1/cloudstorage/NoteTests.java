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
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests {

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
	public void addNote() {
		login();
		WebElement notesTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-notes-tab\"]")));
		notesTab.click();

		WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"nav-notes\"]/button")));
		btnAdd.click();

		WebElement inTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"note-title\"]")));
		WebElement inDescription = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"note-description\"]"))));
		String title = "Note Example 1";
		String description = "Note Description 1";
		inTitle.sendKeys(title);
		inDescription.sendKeys(description);

		WebElement btnSave = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
		btnSave.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		notesTab = driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]"));
		notesTab.click();

		WebElement lastTitle = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"noteTable\"]/tbody/tr/th)[last()]"))));
		WebElement lastDescription = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"noteTable\"]/tbody/tr/td[2])[last()]"))));
		Assertions.assertEquals(title, lastTitle.getText());
		Assertions.assertEquals(description, lastDescription.getText());
	}

	@Test
	public void editNote() {
		login();
		WebElement notesTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-notes-tab\"]")));
		notesTab.click();

		WebElement btnEditLast = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"noteTable\"]/tbody/tr/td[1]/button)[last()]"))));
		btnEditLast.click();

		WebElement inTitle = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"note-title\"]"))));
		WebElement inDescription = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"note-description\"]"))));

		String title = "Note Edit #1";
		String description = "Note Edit #2";
		inTitle.clear();
		inTitle.sendKeys(title);
		inDescription.clear();
		inDescription.sendKeys(description);

		WebElement btnSave = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
		btnSave.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		notesTab = driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]"));
		notesTab.click();

		WebElement lastTitle = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"noteTable\"]/tbody/tr/th)[last()]"))));
		WebElement lastDescription = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//*[@id=\"noteTable\"]/tbody/tr/td[2])[last()]"))));
		Assertions.assertEquals(title, lastTitle.getText());
		Assertions.assertEquals(description, lastDescription.getText());
	}

	@Test
	public void deleteNote() {
		login();
		WebElement notesTab = driver.findElement(wait.until(f -> By.xpath("//*[@id=\"nav-notes-tab\"]")));
		notesTab.click();

		List<WebElement> btnDelete = driver.findElements(By.xpath("//*[@id=\"noteTable\"]/tbody/tr/td[1]/form/button"));
		int lastIdx = btnDelete.size() - 1;
		WebElement btnDeleteLast = wait.until(ExpectedConditions.elementToBeClickable(btnDelete.get(lastIdx)));
		btnDeleteLast.click();

		WebElement linkBack = driver.findElement(By.xpath("/html/body/div/div[2]/div/span/a"));
		linkBack.click();

		notesTab = driver.findElement(By.xpath("//*[@id=\"nav-notes-tab\"]"));
		notesTab.click();

		btnDelete = driver.findElements(By.xpath("//*[@id=\"noteTable\"]/tbody/tr/td[1]/form/button"));
		Assertions.assertEquals(btnDelete.size(), lastIdx);
	}

}
