package pages;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import com.vmo.listener.TestNGListener;
import com.vmo.utils.log.LogHelper;

import io.qameta.allure.Step;
import products.Product;
import webkeywords.WebKeywords;

public class Amazon extends TestNGListener {

	private static Logger logger = LogHelper.getLogger();

	private WebKeywords action;
	private Product product;

	private static String INPUT_SEARCH = "//input[@id='twotabsearchtextbox']";
	private static String INPUT_SUBMIT = "//input[@id='nav-search-submit-button']";

	private static String SPAN_CONTENT_SEARCH = "//span[@class='a-color-state a-text-bold']";
	private static String SPAN_RESULT_SEARCH = "//span[contains(text(),'results for')]";

	private static String A_NEXT_PAGE = "//a[@class='s-pagination-item s-pagination-next s-pagination-button s-pagination-separator']";

	private static String DIV_LIST_ITEMS = "//div[@class='sg-col sg-col-4-of-12 sg-col-8-of-16 sg-col-12-of-20 s-list-col-right']";
	private static String SPAN_NAME_PRODUCT = "//div[@class='sg-col-inner']//div/h2/a/span";
	private static String SPAN_PRICE_PRODUCT = "//span[@class='a-price']//span[@class='a-offscreen']";
	private static String A_LINK_PRODUCT = "//div[@class='sg-col-inner']//div/h2/a";

	public Amazon(WebKeywords action) {
		this.action = action;
	}

	@Step("Search content")
	public Amazon search(String contentSearch) {
		action.setText(INPUT_SEARCH, contentSearch);
		action.click(INPUT_SUBMIT);
		return new Amazon(this.action);
	}
	
	@Step("Should to be show content search")
	public boolean shoudBeToShowContentSearch(String contentSearch) {
		String actualText = action.getText(SPAN_CONTENT_SEARCH);
		if(actualText.contains(contentSearch)) {
			return true;
		}
		return false;
	}

	@Step("Should to be show result search")
	public boolean shoudBeToShowResultSearch() {
		return action.verifyElementContainsValueBl(SPAN_RESULT_SEARCH, "of");
	}

	@Step("Get list products")
	public List<Product> getListProducts() {
		int i;
		int count = 0;
		String pr = "";
		List<WebElement> els = action.findWebElements(DIV_LIST_ITEMS);
		List<WebElement> elsName = action.findWebElements(SPAN_NAME_PRODUCT);
		List<WebElement> elsPrice = action.findWebElements(SPAN_PRICE_PRODUCT);
		List<WebElement> elsLink = action.findWebElements(A_LINK_PRODUCT);

		List<Product> products = new ArrayList<Product>();

		logger.info("Getting information of ListProducts in website Amazon");

		for (i = 0; i < 1; i++) {
			els = action.findWebElements(DIV_LIST_ITEMS);

			for (int index = 1; index < 3; index++) {
				count++;

				elsName = action.findWebElements(SPAN_NAME_PRODUCT);
				elsPrice = action.findWebElements(SPAN_PRICE_PRODUCT);
				elsLink = action.findWebElements(A_LINK_PRODUCT);

				logger.info(MessageFormat
						.format("Getting information of product in ''{0}'' of page number ''{1}'' are:", index, i + 1));

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
					price = Double.parseDouble(pr.replaceAll("VND", "").replaceAll("usd", "").replaceAll("$", "")
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
		}
		logger.info(MessageFormat.format(
				"Got information of product successfully. List infor of ''{0}'' products in website Amazon are: ''{1}''",
				count, products));
		return products;
	}
}
