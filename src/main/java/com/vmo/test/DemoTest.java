package com.vmo.test;

import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vmo.listener.TestNGListener;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.vmo.utils.log.LogHelper;

import pages.Ebay;
import pages.Amazon;
import products.Product;

@Listeners({ TestNGListener.class})
public class DemoTest extends TestNGListener {
	private static Logger logger = LogHelper.getLogger();

	private Ebay ebay;
	private Amazon amazon;

	public Product product;
	private drivers.DriverManager DriverManager;

	private static String CONTENT_SEARCH = "iPhone 11";
	Comparator<Product> comparator = Comparator.comparing(Product::getPrice);

	List<Product> listProducts = new ArrayList<>();

	public DemoTest() {
		super();
	}

	public WebDriver getDriver() {
		return DriverManager.getDriver();
	}
	
	@Test
	public void getProducts01Ebay() {
		ebay = new Ebay(action);
		ebay.search(CONTENT_SEARCH);
		assertTrue(ebay.shoudBeToShowResultSearch());

		ebay.getListProducts();

		List<Product> productse = ebay.getListProducts();
		for (Product ProductsEbay : productse) {
			listProducts.add(ProductsEbay);
		}
		logger.info(MessageFormat.format("Add list ''{0}'' products in Ebay successfully : ''{1}''", productse.size(),
				listProducts));
	}
	
	@Test
	public void getProducts02Amazon() throws InterruptedException {
		amazon = new Amazon(action);
		amazon.search(CONTENT_SEARCH);
		assertTrue(amazon.shoudBeToShowResultSearch());

		amazon.getListProducts();

		List<Product> productsa = amazon.getListProducts();
		for (Product ProductsAmazon : productsa) {
			listProducts.add(ProductsAmazon);
		}

		logger.info(MessageFormat.format("Add list ''{0}'' products in Amazon successfully : ''{1}''", productsa.size(),
				listProducts));
	}
	
	@Test
	public void testSort() {
		Collections.sort(listProducts, comparator);

		logger.info(MessageFormat.format(
				"List ''{0}'' products sorted in both website Amazon and Ebay of search ''{1}'': ''{2}''",
				listProducts.size(), CONTENT_SEARCH, listProducts));
	}
}
