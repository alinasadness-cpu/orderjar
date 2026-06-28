package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private static WebDriver driver;
    private WebDriverWait wait;
    
    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Should show validation error for empty name")
    public void shouldShowErrorForEmptyName() {
        driver.get("http://localhost:9999");
        
      
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='phone']")));
        
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("+79261234567");
        
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
       
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("input[name='name'] + .input__sub, .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed(), "Сообщение об ошибке не отображается");
    }
    
    @Test
    @DisplayName("Should show validation error for invalid phone")
    public void shouldShowErrorForInvalidPhone() {
        driver.get("http://localhost:9999");
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("123");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("input[name='phone'] + .input__sub, .input_invalid")));
        
        assertTrue(errorMessage.isDisplayed(), "Сообщение об ошибке не отображается");
    }
    
    @Test
    @DisplayName("Should successfully fill form and proceed")
    public void shouldFillFormAndProceed() {
        driver.get("http://localhost:9999");
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        
        assertTrue(driver.getCurrentUrl().contains("localhost"), "Страница не загрузилась");
    }
}
