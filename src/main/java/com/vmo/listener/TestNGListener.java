package com.vmo.listener;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.vmo.test.DemoTest;
import com.vmo.utils.configs.ConfigSettings;
import com.vmo.utils.log.LogHelper;

import io.qameta.allure.Attachment;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.TmsLink;
import webkeywords.WebKeywords;

public class TestNGListener implements ITestNGListener {
	protected WebKeywords action;

	private static Logger logger = LogHelper.getLogger();
	private ConfigSettings configSettings;

	public TestNGListener() {
		action = new WebKeywords();
		configSettings = new ConfigSettings(System.getProperty("user.dir"));
	}
	
	@Parameters({ "browser" })
	@BeforeSuite
	public void beforeSuite(String browser) {
		deleteFileFromDirectory();
		action.openBrowser(browser);
	}
	
	@AfterSuite
	public void afterSuite() {
		action.closeBrowser(configSettings.getEbayUrl());
		action.closeBrowser(configSettings.getAmazonUrl());
	}

	@BeforeTest
	public void beforeTest() {
		action.navigateToUrl(configSettings.getEbayUrl());
	}
	
	@AfterMethod
	public void afterMethod() {
		action.navigateToUrl(configSettings.getAmazonUrl());
	}

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	// Text attachments for allure
	@Attachment(value = "Form screenshot", type = "image/png")
	public byte[] saveScreenshotPNG(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	// Text attachments for allure
	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}

	// @Override
	public void onTestFailure(ITestResult iTestResult) {
		System.out.print("I am in onTestFailure method " + getTestMethodName(iTestResult) + "failed");

		// Get driver from DemoTest and assign to local webdriver variable
		Object testClass = iTestResult.getInstance();
		WebDriver driver = ((DemoTest) testClass).getDriver();

		// Allure ScreenshotRobot and SaveTestLog
		if (driver instanceof WebDriver) {
			System.out.println("Screenshot captured for test case: " + getTestMethodName(iTestResult));
			saveScreenshotPNG(driver);
		}

		// Save a log on allure
		saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
	}
	
	public void deleteFileFromDirectory() {
	    //String user = System.getProperty("user home");   // user if data in your user profile
		//String filePath = user + "/Downloads/Test;
		String directory = "D:\\VMO_F2\\EbayAmazon_and_PowerBuy\\EbayAndAmazon\\target\\allure-results"; // If download is in IDE project folder

		File file = new File(directory);
		String[] currentFiles;
		if (file.isDirectory()) {
			currentFiles = file.list();
			for (int i = 0; i < currentFiles.length; i++) {
				File myFile = new File(file, currentFiles[i]);
				myFile.delete();
			}
		}
	}
}
