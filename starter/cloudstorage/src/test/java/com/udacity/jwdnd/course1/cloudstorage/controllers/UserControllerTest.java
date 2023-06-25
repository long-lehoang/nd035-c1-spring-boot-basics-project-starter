package com.udacity.jwdnd.course1.cloudstorage.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set up WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        // Quit WebDriver
        driver.quit();
    }

    @Test
    public void testUnauthorizedAccess() {
        // Navigate to home page
        driver.get("http://localhost:"+port+"/home");
        // Verify that login page is displayed
        WebElement loginForm = driver.findElement(By.id("login-button"));
        assertEquals("Login", loginForm.getText());

        // Navigate to home page
        driver.get("http://localhost:"+port+"/signup");
        // Verify that login page is displayed
        String signup = driver.getTitle();
        assertEquals("Sign Up", signup);
    }

    @Test
    public void testSignupAndLogin() {
        // Navigate to signup page
        driver.get("http://localhost:"+port+"/signup");

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


        // Navigate to login page
        driver.get("http://localhost:"+port+"/login");

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

        // Logout
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();

        // Verify successful logout and home page inaccessibility
        String loginForm = driver.getTitle();
        assertEquals("Login", loginForm);

        // Navigate to home page
        driver.get("http://localhost:"+port+"/home");
        // Verify that login page is displayed
        String loginTitle = driver.getTitle();
        assertEquals("Login", loginTitle);
    }
}