package com.udacity.jwdnd.course1.cloudstorage.controllers;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HomeControllerTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private final String HOST = "http://localhost:";
    @BeforeEach
    public void setUp() {
        // Set up WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(HOST + port+"/signup");

        // Fill in signup form
        WebElement usernameInput = driver.findElement(By.id("inputUsername"));
        WebElement passwordInput = driver.findElement(By.id("inputPassword"));
        WebElement firstnameInput = driver.findElement(By.id("inputFirstName"));
        WebElement lastnameInput = driver.findElement(By.id("inputLastName"));
        firstnameInput.sendKeys("Le");
        lastnameInput.sendKeys("Hoang Long");
        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin");

        // Submit signup form
        WebElement signupButton = driver.findElement(By.id("buttonSignUp"));
        signupButton.click();

        // Navigate to the login page
        driver.get(HOST + port+"/login");

        // Fill in login form
        usernameInput = driver.findElement(By.id("inputUsername"));
        passwordInput = driver.findElement(By.id("inputPassword"));
        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin");

        // Submit login form
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        // Verify successful login and home page accessibility
        String homePageTitle = driver.getTitle();
        assertEquals("Home", homePageTitle);

    }

    @AfterEach
    public void tearDown() {
        // Quit WebDriver
        driver.quit();
    }

    @Test
    @Order(1)
    public void testCreateNote() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-notes-tab")).click();

        final String expectedTitle = "Test Note";
        final String expectedDescription = "Test Note Description";
        // Create note and submit
        WebElement btnAddNote = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("button-add-note"))));
        btnAddNote.click();
        WebElement inputTitle = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-title"))));
        inputTitle.sendKeys(expectedTitle);
        driver.findElement(By.id("note-description")).sendKeys(expectedDescription);
        WebElement noteSubmit = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn-note-submit"))));
        noteSubmit.click();

        // Verify result is displayed
        driver.findElement(By.id("nav-notes-tab")).click();

        // Find the table element
        WebElement table = driver.findElement(By.id("nav-notes"));

        // Find all the note rows within the table body
        List<WebElement> noteRows = table.findElements(By.xpath("//tbody/tr"));

        // Iterate over each note row and verify the title and description
        int endIndex = noteRows.size()-2;
        WebElement noteTitleElement = noteRows.get(endIndex).findElement(By.xpath("th[1]"));
        WebElement noteDescriptionElement = noteRows.get(endIndex).findElement(By.xpath("td[2]"));

        Thread.sleep(1000);
        String noteTitle = noteTitleElement.getText();
        String noteDescription = noteDescriptionElement.getText();

        // Verify the note title and description for each row
        assertEquals(expectedTitle, noteTitle);
        assertEquals(expectedDescription, noteDescription);
    }

    @Test
    @Order(2)
    public void testEditNote() throws InterruptedException {
        // Edit note and submit
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-notes-tab")).click();

        // Find the table element
        WebElement table = driver.findElement(By.id("nav-notes"));

        // Find all the note rows within the table body
        List<WebElement> noteRows = table.findElements(By.xpath("//tbody/tr"));

        noteRows.get(0).findElement(By.tagName("button")).click();

        final String expectedTitle = "Edit Note";
        final String expectedDescription = "Edit Note Description";

        // Edit note and submit
        WebElement inputTitle = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-title"))));
        inputTitle.clear();
        inputTitle.sendKeys(expectedTitle);
        WebElement inputDescription = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("note-description"))));
        inputDescription.clear();
        inputDescription.sendKeys(expectedDescription);
        WebElement noteSubmit = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn-note-submit"))));
        noteSubmit.click();

        // Verify result is displayed
        driver.findElement(By.id("nav-notes-tab")).click();

        // Find all the note rows within the table body
        table = driver.findElement(By.id("nav-notes"));
        noteRows = table.findElements(By.xpath("//tbody/tr"));

        // Iterate over each note row and verify the title and description
        WebElement noteTitleElement = noteRows.get(0).findElement(By.xpath("th[1]"));
        WebElement noteDescriptionElement = noteRows.get(0).findElement(By.xpath("td[2]"));
        Thread.sleep(1000);

        String noteTitle = noteTitleElement.getText();
        String noteDescription = noteDescriptionElement.getText();
        // Verify the note title and description for each row
        assertEquals(expectedTitle, noteTitle);
        assertEquals(expectedDescription, noteDescription);
    }

    @Test
    @Order(3)
    public void testDeleteNote() {
        // Delete note and submit
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-notes-tab")).click();

        // Find the table element
        WebElement table = driver.findElement(By.id("nav-notes"));

        // Find all the note rows within the table body
        List<WebElement> noteRows = table.findElements(By.xpath("//tbody/tr"));

        int currentSize = noteRows.size();
        noteRows.get(0).findElement(By.tagName("a")).click();

        // Verify result is displayed
        driver.findElement(By.id("nav-notes-tab")).click();

        // Find all the note rows within the table body
        table = driver.findElement(By.id("nav-notes"));
        noteRows = table.findElements(By.xpath("//tbody/tr"));
        int deletedSize = noteRows.size();

        assertEquals(currentSize-1, deletedSize);
    }

    @Test
    @Order(4)
    public void testCreateCredential() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-credentials-tab")).click();

        final String expectedUrl = "http://localhost";
        final String expectedUsername = "test";
        final String expectedPassword = "password";
        // Create credential and submit
        WebElement btnAddCredential = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("button-add-credential"))));
        btnAddCredential.click();
        WebElement inputUrl = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-url"))));
        inputUrl.sendKeys(expectedUrl);
        WebElement inputUsername = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-username"))));
        inputUsername.sendKeys(expectedUsername);
        WebElement inputPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-password"))));
        inputPassword.sendKeys(expectedPassword);
        WebElement credentialSubmit = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn-credential-submit"))));
        credentialSubmit.click();

        // Verify result is displayed
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Find the table element
        WebElement table = driver.findElement(By.id("nav-credentials"));

        // Find all the credential rows within the table body
        List<WebElement> credentialRows = table.findElements(By.xpath("//tbody/tr"));

        // Iterate over each note row and verify the title and description
        int endIndex = credentialRows.size()-1;
        WebElement credentialURLElement = credentialRows.get(endIndex).findElement(By.xpath("th[1]"));
        WebElement credentialUsernameElement = credentialRows.get(endIndex).findElement(By.xpath("td[2]"));
        WebElement credentialPasswordElement = credentialRows.get(endIndex).findElement(By.xpath("td[3]"));

        Thread.sleep(1000);
        String url = credentialURLElement.getText();
        String username = credentialUsernameElement.getText();
        String password = credentialPasswordElement.getText();

        // Verify the note title and description for each row
        assertEquals(expectedUrl, url);
        assertEquals(expectedUsername, username);
        assertNotEquals(expectedPassword, password);
    }

    @Test
    @Order(5)
    public void testEditCredential() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-credentials-tab")).click();

        final String expectedUrl = "http://localhost1";
        final String expectedPassword = "password";

        WebElement table = driver.findElement(By.id("nav-credentials"));

        // Find all the note rows within the table body
        List<WebElement> credentialRows = table.findElements(By.xpath("//tbody/tr"));
        int size = credentialRows.size();

        WebElement btnEdit = wait.until(ExpectedConditions.elementToBeClickable(credentialRows.get(size-1).findElement(By.tagName("button"))));
        btnEdit.click();

        // Edit credential and submit
        WebElement inputUrl = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-url"))));
        inputUrl.clear();
        inputUrl.sendKeys(expectedUrl);
        WebElement inputPassword = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credential-password"))));
        // Verify inital password is displayed
        Thread.sleep(1000);
        String actualPassword = inputPassword.getAttribute("value");

        assertEquals(expectedPassword, actualPassword);
        WebElement credentialSubmit = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn-credential-submit"))));
        credentialSubmit.click();

        // Verify result is displayed
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Find the table element
        table = driver.findElement(By.id("nav-credentials"));

        // Find all the credential rows within the table body
        credentialRows = table.findElements(By.xpath("//tbody/tr"));

        //verify
        WebElement credentialURLElement = credentialRows.get(size-1).findElement(By.xpath("th[1]"));

        Thread.sleep(1000);
        String url = credentialURLElement.getText();

        // Verify the note title and description for each row
        assertEquals(expectedUrl, url);
    }

    @Test
    @Order(6)
    public void testDeleteCredential() {
        // Delete credential and submit
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Find the table element
        WebElement table = driver.findElement(By.id("nav-credentials"));

        // Find all the note rows within the table body
        List<WebElement> credentialRows = table.findElements(By.xpath("//tbody/tr"));

        int currentSize = credentialRows.size();
        WebElement deletedBtn = wait.until(ExpectedConditions.elementToBeClickable(credentialRows.get(currentSize-1).findElement(By.tagName("a"))));
        deletedBtn.click();

        // Verify result is displayed
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Find all the note rows within the table body
        table = driver.findElement(By.id("nav-credentials"));
        credentialRows = table.findElements(By.xpath("//tbody/tr"));
        int deletedSize = credentialRows.size();

        assertEquals(currentSize-1, deletedSize);
    }
}