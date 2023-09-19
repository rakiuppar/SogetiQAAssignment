/**
 * Rakesh Mustoor
 */

package com.sogeti.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.sogeti.base.BaseClass;

public class CommonPage extends BaseClass {

	@FindBy(xpath = "(//*[@id='main-menu']//child::span[contains(text(),'Services')])[1]/../..")
	WebElement servicesDropList;

	@FindBy(xpath = "//a[contains(@class,'subMenuLink') and (@href='https://www.sogeti.com/services/automation/')]/..")
	WebElement automationOption;

	@FindBy(xpath = "//div[@class='page-heading']//child::*[contains(text(),'Automation')]")
	WebElement automationLabel;

	@FindBy(xpath = "//label[contains(text(),'First Name')]//following::input[1]")
	WebElement firstNameTextBox;

	@FindBy(xpath = "//label[contains(text(),'Last Name')]//following::input[1]")
	WebElement lastNameTextBox;

	@FindBy(xpath = "//label[contains(text(),'Email')]//following::input[1]")
	WebElement emailTextBox;

	@FindBy(xpath = "//label[contains(text(),'Phone')]//following::input[1]")
	WebElement phoneTextBox;

	@FindBy(xpath = "//label[contains(text(),'Company')]//following::input[1]")
	WebElement companyTextBox;

	@FindBy(xpath = "//label[contains(text(),'Country')]//following::select[1]")
	WebElement countryDropDown;

	@FindBy(xpath = "//label[contains(text(),'Message')]//following::textarea[1]")
	WebElement messageTextArea;

	@FindBy(xpath = "//input[@value='I agree']")
	WebElement iAgreeCheckBox;

	@FindBy(name = "submit")
	WebElement submitButton;

	@FindBy(xpath = "//div[@aria-controls='country-list-id']")
	WebElement worldwideDropdown;

	@FindBy(className = "country-list")
	private WebElement countriesList;

	SoftAssert softAssert = new SoftAssert();

	public CommonPage() {
		PageFactory.initElements(driver, this);
	}

	public void loadUrl(String url) {
		driver.get(url);
	}

	public void testCase1() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(servicesDropList));
		Actions act = new Actions(driver);
		act.moveToElement(servicesDropList).perform();
		act.moveToElement(automationOption).click().perform();
		softAssert.assertEquals(driver.getTitle(), "Automation", "Not on Automation page");
		Boolean automationTextpresent = automationLabel.isDisplayed();
		softAssert.assertTrue(automationTextpresent, "Automation label is not displayed");
		act.moveToElement(servicesDropList).perform();
		softAssert.assertTrue(servicesDropList.getAttribute("class").contains("selected"),
				"Services option is not selected");
		softAssert.assertTrue(automationOption.getAttribute("class").contains("selected"),
				"Services option is not selected");
		softAssert.assertAll();
	}

	public void testCase2() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(servicesDropList));
		Actions act = new Actions(driver);
		act.moveToElement(servicesDropList).perform();
		act.moveToElement(automationOption).click().perform();
		firstNameTextBox.sendKeys(generateRandomString());
		lastNameTextBox.sendKeys(generateRandomString());
		emailTextBox.sendKeys(generateRandomString() + "@gmail.com");
		phoneTextBox.sendKeys(generateRandomNumber());
		companyTextBox.sendKeys(generateRandomString());
		Select select = new Select(countryDropDown);
		select.selectByVisibleText("Germany");
		messageTextArea.sendKeys(generateRandomString());
		act.sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.elementToBeClickable(iAgreeCheckBox));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", iAgreeCheckBox);
		// Further steps cannot be automated since it has captcha.
		// We do not have any solution to handle the captcha in automation.
		// Alternate solution would be disabling the captcha in test env.
	}

	public void testCase3() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(servicesDropList));
		worldwideDropdown.click();
		verifySogetiLocations();
	}

	String generateRandomString() {
		String generatedString = RandomStringUtils.randomAlphabetic(10);
		return generatedString;
	}

	String generateRandomNumber() {
		Random rand = new Random();
		int generatedNumber = rand.nextInt(99999);
		return String.valueOf(generatedNumber + generatedNumber);
	}

	void getWindowsHandle() {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	public void verifySogetiLocations() {
		List<WebElement> countriesList = driver.findElements(By.xpath("//*[@id='country-list-id']//child::a"));
		List<String> expectedTitles = new ArrayList<String>(Arrays.asList("Belgium", "Etu", "France", "Deutschland",
				"Ireland", "Luxembourg", "We Make Technology Work | Sogeti", "Norge", "Espa", "Sverige", "UK", "USA"));
		String parentWindow = driver.getWindowHandle();
		ListIterator<WebElement> countries = countriesList.listIterator();
		ListIterator<String> titles = expectedTitles.listIterator();
		while (countries.hasNext()) {
			WebElement country = countries.next();
			String countryName = country.getText();
			country.click();
			getWindowsHandle();
			String actualTitle = driver.getTitle();
			String expectedTitle = (String) titles.next();
//			System.out.println(actualTitle);
			boolean expectedResult = actualTitle.contains(expectedTitle);
			softAssert.assertTrue(expectedResult, "Not on " + countryName + " page");
			driver.close();
			driver.switchTo().window(parentWindow);
		}
		softAssert.assertAll();
	}

}
