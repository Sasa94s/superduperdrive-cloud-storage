package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.pages.NotePage;
import com.udacity.jwdnd.course1.cloudstorage.core.Router;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests {

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
		this.router = Router.getInstance(driver, port);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void addNote() {
		router.defaultLogin(true);
		NotePage notePage = router.notePage();
		Note note = notePage.generateNote();
		notePage.addNote(note);
		Assertions.assertTrue(notePage.resultStatus(), notePage.resultBody.getText());
		notePage = router.notePage();
		Note newNote = notePage.getLastNote();

		Assertions.assertEquals(note.getNoteTitle(), newNote.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), newNote.getNoteDescription());
	}

	@Test
	public void editNote() {
		router.defaultLogin(true);
		NotePage notePage = router.notePage();
		Note note = notePage.generateNote();
		notePage.editNote(note);
		Assertions.assertTrue(notePage.resultStatus(), notePage.resultBody.getText());
		notePage = router.notePage();
		Note newNote = notePage.getLastNote();

		Assertions.assertEquals(note.getNoteTitle(), newNote.getNoteTitle());
		Assertions.assertEquals(note.getNoteDescription(), newNote.getNoteDescription());
	}

	@Test
	public void deleteNote() {
		router.defaultLogin(true);
		NotePage notePage = router.notePage();
		int deletedIdx = notePage.deleteLastNote();
		Assertions.assertTrue(notePage.resultStatus(), notePage.resultBody.getText());

		notePage = router.notePage();
		int lastIdx = notePage.getLastNoteIdx();
		Assertions.assertEquals(deletedIdx - 1, lastIdx, "Notes count is not changed");
	}

}
