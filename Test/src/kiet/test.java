package kiet;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import graphql.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class test 
{
	public static void main(String[] args) 
	{
		ChromeDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		
		try
		{
		driver.get("https://www.webstaurantstore.com");		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchval")));		
		driver.manage().window().maximize();
		
		WebElement searchBar = driver.findElement(By.id("searchval"));
		searchBar.sendKeys("stainless work table");
		searchBar.sendKeys(Keys.RETURN);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchval")));
		
		WebElement results = driver.findElement(By.id("categoriesBox"));
		List<WebElement> allResults = driver.findElements(By.cssSelector(".ag-item a[href]"));
		
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
				
		WebElement nextPage = driver.findElement(By.cssSelector("[href='/regency-24-x-48-18-gauge-304-stainless-steel-commercial-work-table-with-galvanized-legs-and-casters/600T2448GC.html']"));
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", nextPage);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) 
		{
			System.out.printf("Failed to scroll down");
			e.printStackTrace();
		}
		
		WebElement addToCart = driver.findElement(By.cssSelector("[data-item-number='600t2448gc'] .add-to-cart"));
		addToCart.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Regency24")));
		
		WebElement viewCheckout = driver.findElement(By.cssSelector(".btn-primary"));
		viewCheckout.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".emptyCartButton")));
		
		WebElement emptyCartBtn = driver.findElement(By.cssSelector(".emptyCartButton"));
		emptyCartBtn.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='modal-footer']")));
		
		WebElement modalBtns = driver.findElement(By.cssSelector("[data-testid='modal-footer'] .border-solid"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();",modalBtns);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-toggle='loginModal']")));
		
		String header1Text = driver.findElement(By.cssSelector(".header-1")).getText();
		Assert.assertTrue(header1Text.contains("Your cart is empty.")); //Your cart is empty.
		
		}
		finally
		{
			driver.close();
		}
	}
}