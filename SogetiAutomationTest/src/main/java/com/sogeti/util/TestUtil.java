/**
 * 
 */
package com.sogeti.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.sogeti.base.BaseClass;

/**
 * Rakesh Mustoor
 */
public class TestUtil extends BaseClass{

	public static int PAGE_LOAD_TIMEOUT = 20;
	public static int IMPLICIT_WAIT = 20;
	
	public static void takeScreenshotAtEndOfTest() throws IOException 
	{
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String curDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(curDir + "/screenshots/" + System.currentTimeMillis() + ".png"));
	}
}
