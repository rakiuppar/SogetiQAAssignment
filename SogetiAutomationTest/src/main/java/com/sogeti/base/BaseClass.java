/**
 * 
 */
package com.sogeti.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sogeti.util.TestUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Rakesh Mustoor
 */
public class BaseClass {

	public static WebDriver driver;
	public static Properties prop;

	public BaseClass() {
		try {
			prop = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java/com/sogeti/config/config.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void acceptCookies() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement acceptCookeiesButton = driver.findElement(By.className("acceptCookie"));
		wait.until(ExpectedConditions.elementToBeClickable(acceptCookeiesButton));
		acceptCookeiesButton.click();
	}

	public static void initialization() {
		String browserName = prop.getProperty("browser");
		if (browserName.equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestUtil.IMPLICIT_WAIT));
		driver.get(prop.getProperty("url"));
		acceptCookies();
		driver.manage().window().maximize();
	}

}
