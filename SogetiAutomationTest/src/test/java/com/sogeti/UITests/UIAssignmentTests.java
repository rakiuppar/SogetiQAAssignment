/**
 * Author: Rakesh Mustoor
 */
package com.sogeti.UITests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sogeti.base.BaseClass;
import com.sogeti.pages.CommonPage;
import com.sogeti.util.TestUtil;

public class UIAssignmentTests extends BaseClass {

	CommonPage commonPage;
	TestUtil testUtil;

	public UIAssignmentTests() {
		super();
	}

	@BeforeMethod
	public void setup() {
		initialization();
		testUtil = new TestUtil();
		commonPage = new CommonPage();
	}

	@Test(priority = 0)
	public void verifyLinksSelected() {
		commonPage.testCase1();
	}

	@Test(priority = 1)
	public void fillContactForm() {
		commonPage.testCase2();
		// This test has been automated until the Captcha step.
		// We do not have any solution to handle the captcha in automation.
		// Alternate solution would be disabling the captcha in test env.
	}

	@Test(priority = 2)
	public void verifyCountriesLink() {
		commonPage.testCase3();
	}

	@AfterMethod
	public void cleanUp() {
		driver.quit();
	}

}
