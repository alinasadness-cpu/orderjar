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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='name'] .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Поле обязательно для заполнения"));
    }
    

    @Test
    @DisplayName("Should show validation error for empty phone")
    public void shouldShowErrorForEmptyPhone() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='phone'] .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Поле обязательно для заполнения"));
    }
   
    @Test
    @DisplayName("Should show validation error for invalid phone")
    public void shouldShowErrorForInvalidPhone() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("+790000000");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='phone'] .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
    
    
    @Test
    @DisplayName("Should successfully submit application and show success message")
    public void shouldSuccessfullySubmitApplication() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name']")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[name='name']"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("input[name='phone']"));
        phoneInput.sendKeys("+79261234567");
        
      
        try {
            WebElement checkboxLabel = driver.findElement(By.xpath("//span[contains(text(), 'Я соглашаюсь с условиями')]"));
            checkboxLabel.click();
        } catch (Exception e) {
            WebElement checkbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
            checkbox.click();
        }
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='order-success']")));
        
        assertTrue(successMessage.isDisplayed());
        assertTrue(successMessage.getText().contains("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }
}
