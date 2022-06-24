package pages;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import com.vmo.utils.log.LogHelper;

import io.qameta.allure.Step;
import products.Product;
import webkeywords.WebKeywords;

public class Ebay {

	private static Logger logger = LogHelper.getLogger();

	private WebKeywords action;
	private Product product;
	List<Product> products = new ArrayList<Product>();

	private static String INPUT_SEARCH = "//input[@id='gh-ac']";
	private static String INPUT_SUBMIT = "//input[@id='gh-btn']";
	
	private static String SPAN_CONTENT_SEARCH = "//div[@id='mainContent']//div[2]//h1/span[2]";
	private static String SPAN_RESULT_SEARCH = "//div[@id='mainContent']//div[2]//h1/span[1]";

	private static String A_NEXT_PAGE = "//nav[@class='pagination']/a[@class='pagination__next icon-link']";

	private static String DIV_LIST_ITEMS = "//div[@class='s-item__info clearfix']";
	private static String H3_ITEM_NAME = "//div[@class='s-item__info clearfix']/a/h3";
	private static String SPAN_ITEM_PRICE = "//div[@class='s-item__info clearfix']/div//span[@class='s-item__price']";
	private static String A_LINK_ITEM = "//div[@class='s-item__info clearfix']/a";

	public Ebay(WebKeywords action) {
		this.action = action;
	}

	@Step("Search content")
	public Ebay search(String contentSearch) {
		action.setText(INPUT_SEARCH, contentSearch);
		action.click(INPUT_SUBMIT);
		return new Ebay(this.action);
	}
	

	@Step("Should to be show content search")
	public boolean shoudBeToShowContentSearch(String contentSearch) {
		return action.verifyElementText(SPAN_CONTENT_SEARCH, contentSearch);
	}

	@Step("Should to be show results search")
	public boolean shoudBeToShowResultSearch() {
		return action.verifyElementValueBl(SPAN_RESULT_SEARCH);
	}

	public boolean verifyButtonNextPageAble() {
		if (action.getAttributeBl(A_NEXT_PAGE, "aria-disabled") == true) {
			return false;
		} else {
			return true;
		}
	}

	@Step("Get list products")
	public List<Product> getListProducts() {
		int count = 0;
		String pr = "";

		logger.info("Getting information of ListProducts in website Ebay");

		while (verifyButtonNextPageAble()) {
			List<WebElement> els = action.findWebElements(DIV_LIST_ITEMS);
			List<WebElement> elsName = action.findWebElements(H3_ITEM_NAME);
			List<WebElement> elsPrice = action.findWebElements(SPAN_ITEM_PRICE);
			List<WebElement> elsLink = action.findWebElements(A_LINK_ITEM);

			els = action.findWebElements(DIV_LIST_ITEMS);

//			for (int index = 1; index < els.size(); index++) {
			for (int index = 1; index < 2; index++) {

				count++;

				elsName = action.findWebElements(H3_ITEM_NAME);
				elsPrice = action.findWebElements(SPAN_ITEM_PRICE);
				elsLink = action.findWebElements(A_LINK_ITEM);

				logger.info(MessageFormat.format("Getting information of product in ''{0}'' of one page are:", index));

				logger.info(MessageFormat.format("Product: ''{0}''", count));
				String name = action.getText(elsName.get(index));
				logger.info(MessageFormat.format("Name: ''{0}''", name));

				pr = action.getText(elsPrice.get(index));
				double price = 0;

				if (pr.contains("to")) {
					String[] parts = pr.split("to");
					String pr1 = parts[0];
					price = Double.parseDouble(
							pr1.replaceAll("VND", "").replaceAll("usd", "").replaceAll(",", "").replaceAll(" ", ""));
				}
				if (pr.contains("$") || pr.contains("usd")) {
					logger.info("Price in present is dolars. It is needed convert to vnd");
					price = Double.parseDouble(pr.replace("$", "").replaceAll("usd", "")
							.replaceAll(",", "").replaceAll(" ", ""));
					price = price * 23;
				}
				if (pr.contains("to") == false && pr.contains("usd") == false && pr.isEmpty() == false) {
					price = Double.parseDouble(
							pr.replaceAll("VND", "").replaceAll("usd", "").replaceAll(",", "").replaceAll(" ", ""));
				}
				if (pr == "" || pr.isEmpty()) {
					price = 0;
				}

				logger.info(MessageFormat.format("Price: ''{0}'' vnd", price));

				String link = action.getAttribute(elsLink.get(index), "href");
				logger.info(MessageFormat.format("Link: ''{0}''", link));

				product = new Product(name, price, link);

				products.add(product);
			}
			action.scrollToElement(A_NEXT_PAGE);
			action.click(A_NEXT_PAGE);
		}
		logger.info("End of page display for the result filter product");

		logger.info(MessageFormat.format(
				"Got information of product successfully. List of infor ''{0}'' products in website Ebay are: ''{1}''",
				products.size(), products));
		return products;
	}
}
