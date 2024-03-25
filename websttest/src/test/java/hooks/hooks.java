package hooks;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;

public class hooks 
{
	@After
	public void Teardown()
	{
		ChromeDriver driver = new ChromeDriver();
		driver.close();
	}
}
