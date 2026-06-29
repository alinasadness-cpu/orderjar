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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='name'].input_invalid .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Поле обязательно для заполнения"));
    }
    
    @Test
    @DisplayName("Should show validation error for invalid name")
    public void shouldShowErrorForInvalidName() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван123 Петров@#$");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='name'].input_invalid .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    
 
    @Test
    @DisplayName("Should show validation error for empty phone")
    public void shouldShowErrorForEmptyPhone() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")));
        
        assertTrue(errorMessage.getText().contains("Поле обязательно для заполнения"));
    }
    
    
    @Test
    @DisplayName("Should show validation error for invalid phone")
    public void shouldShowErrorForInvalidPhone() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("123");
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")));
        
        assertTrue(errorMessage.getText().contains("На указанный номер моб. тел.") ||
                   errorMessage.getText().contains("Телефон указан неверно") ||
                   errorMessage.getText().contains("Должно быть 11 цифр"));
    }
    

    @Test
    @DisplayName("Should show validation error for unchecked agreement")
    public void shouldShowErrorForUncheckedAgreement() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
  
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='agreement']")));
        assertTrue(checkbox.getAttribute("class").contains("input_invalid"));
    }
    
    
    @Test
    @DisplayName("Should successfully submit application and show success message")
    public void shouldSuccessfullySubmitApplication() {
        driver.get("http://localhost:9999");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='name'] input")));
        
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameInput.sendKeys("Иван Петров");
        
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneInput.sendKeys("+79261234567");
        
        
        WebElement checkboxLabel = driver.findElement(By.xpath("//span[contains(text(), 'Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй')]"));
        checkboxLabel.click();
        
        WebElement continueButton = driver.findElement(By.cssSelector(".button__text"));
        continueButton.click();
        
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id='order-success']")));
        
        assertTrue(successMessage.isDisplayed());
        assertTrue(successMessage.getText().contains("Ваша заявка успешно отправлена"));
    }
}
