package StepDefinitions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchAddRemoveSteps 
{
	ChromeDriver _driver = new ChromeDriver();
	WebDriverWait _wait = new WebDriverWait(_driver, Duration.ofSeconds(15));

	@Given("user navigates to the main Webstaurant page")
	public void user_navigates_to_the_main_webstaurant_page() 
	{
		WebDriverWait wait = new WebDriverWait(_driver, Duration.ofSeconds(15));
		_driver.get("https://www.webstaurantstore.com");		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchval")));		
		_driver.manage().window().maximize();
	}

	@When("user enters a stainless work table in the search bar")
	public void user_enters_a_stainless_work_table_in_the_search_bar() 
	{		
		WebElement searchBar = _driver.findElement(By.id("searchval"));
		searchBar.sendKeys("stainless work table");
		searchBar.sendKeys(Keys.RETURN);
		_wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchval")));		
	}

	@Then("results are returned")
	public void results_are_returned() 
	{
		WebElement results = _driver.findElement(By.id("categoriesBox"));
		List<WebElement> allResults = _driver.findElements(By.cssSelector(".ag-item a[href]"));
		
		List<String> stringValues = new ArrayList<>();
				
		for (int i = 0; i < allResults.size(); i++)
		{
			stringValues.add(allResults.get(i).getText().replaceAll("(\\d+\\s)(PRODUCTS+)|(\\d+\\s)(CATEGORIES+)", " ") + "\r\n");
			
			try
			{
				for(String value: stringValues)
				{
					Assert.assertTrue(value.contains("Table"));
				}
			}
			catch(Exception e)
			{
				System.out.printf("Search results for products did not contain \"Table\" in the title, title of the product was: %s", stringValues.get(i));
				throw e;
			}
		}		
	}

	@Then("user is able to add item to cart")
	public void user_is_able_to_add_item_to_cart() 
	{		
		WebElement nextPage = _driver.findElement(By.cssSelector("[href='/regency-24-x-48-18-gauge-304-stainless-steel-commercial-work-table-with-galvanized-legs-and-casters/600T2448GC.html']"));
		
		((JavascriptExecutor) _driver).executeScript("arguments[0].scrollIntoView();", nextPage);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) 
		{
			System.out.printf("Failed to scroll down");
			e.printStackTrace();
		}
		
		WebElement addToCart = _driver.findElement(By.cssSelector("[data-item-number='600t2448gc'] .add-to-cart"));
		addToCart.click();
		_wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Regency24")));			
	}

	@Then("user is able to remove item from cart")
	public void user_is_able_to_remove_item_from_cart() 
	{ 		
		WebElement viewCheckout = _driver.findElement(By.cssSelector(".btn-primary"));
		viewCheckout.click();
		_wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".emptyCartButton")));
		
		WebElement emptyCartBtn = _driver.findElement(By.cssSelector(".emptyCartButton"));
		emptyCartBtn.click();
		_wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='modal-footer']")));
		
		WebElement modalBtns = _driver.findElement(By.cssSelector("[data-testid='modal-footer'] .border-solid"));
		((JavascriptExecutor)_driver).executeScript("arguments[0].click();",modalBtns);
		_wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-toggle='loginModal']")));
		
		String header1Text = _driver.findElement(By.cssSelector(".header-1")).getText();
		Assert.assertTrue(header1Text.contains("Your cart is empty."));
	}
}
