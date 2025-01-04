package edu.uclm.esi.listasbe.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class TestSelenium {

	private WebDriver driverPepe, driverAna;
	private Map<String, Object> vars, vars2;
	JavascriptExecutor jsExecutor, jsExecutor2;
 
  @BeforeAll //@BeforeEach 
  public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Andrés\\Downloads\\chromedriver-win64\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--allow-insecure-localhost");

		options.setBinary("C:\\Users\\Andrés\\Downloads\\chrome-win64\\\\chrome.exe");
		options.addArguments("--remote-allow-origins=*");
    
		driverPepe = new ChromeDriver(options);
		new WebDriverWait(driverPepe, Duration.ofSeconds(3));

		jsExecutor = (JavascriptExecutor) driverPepe;
		vars = new HashMap<String, Object>();
		
		driverAna = new ChromeDriver(options);
		new WebDriverWait(driverAna, Duration.ofSeconds(3));

		jsExecutor2 = (JavascriptExecutor) driverAna;
		vars2 = new HashMap<String, Object>();
		
		driverPepe.get("https://localhost:4200/");
		driverAna.get("https://localhost:4200/");
		this.pausa(1000);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		driverPepe.manage().window().setPosition(new Point(0,0));
		driverPepe.manage().window().setSize(new Dimension(screenWidth / 2, screenHeight));
		
		driverAna.manage().window().setPosition(new Point((screenWidth/ 2) + 1, 0));
		driverAna.manage().window().setSize(new Dimension(screenWidth / 2, screenHeight));
  }
  
  @AfterEach
  public void tearDown() {
    
  }
  
  @Test @Order(0)
	public void testRegistroPepe() {
		this.registrar(this.driverPepe, "gonzalezandres010@gmail.com", "Andres1234@", "Andres1234@");

		this.pausa(1000);

		this.iniciarSesion(this.driverPepe, "gonzalezandres010@gmail.com", "Andres1234@");

		this.pausa(1000);

		String url = driverPepe.getCurrentUrl();
		assertEquals("https://localhost:4200/GestionarListas", url);
		
	}
  
	@Test
	@Order(1)
	public void testCreacionLista() {
		String url = driverPepe.getCurrentUrl();
		assertEquals("https://localhost:4200/GestionarListas", url);
		
		WebElement inputNombreLista = driverPepe.findElement(By.xpath("/html/body/app-root/div/app-gestor-listas/section/div/div[1]/input"));
		inputNombreLista.sendKeys("Cumpleaños");
		WebElement botonCrear = driverPepe.findElement(By.xpath("/html/body/app-root/div/app-gestor-listas/section/div/div[1]/button"));
		botonCrear.click();
		
		this.pausa(2000);
		
		WebDriverWait wait = new WebDriverWait(driverPepe, Duration.ofSeconds(10));
		WebElement nombreLista = wait.until(
		        ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/app-gestor-listas/section/div/div[2]/div/div/div[1]/h5"))
		);
		assertEquals("Cumpleaños", nombreLista);
	}

	@Test
	@Order(2)
	public void testRegistroAna() {
		this.registrar(this.driverAna, "gonzalezvarela010@gmail.com", "Andres1234@", "Andres1234@");

		this.pausa(1000);

		this.iniciarSesion(this.driverAna, "gonzalezvarela010@gmail.com", "Andres1234@");

		this.pausa(1000);

		String url = driverAna.getCurrentUrl();
		assertEquals("https://localhost:4200/GestionarListas", url);
	}

	private void pausa(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void registrar(WebDriver driver, String email, String pwd1, String pwd2) {
		WebElement botonIniciarSesion = driver.findElement(By.xpath("/html/body/app-root/div/div/a/button"));
	    botonIniciarSesion.click();
	    
	    this.pausa(1000);
	    
	    String url = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/IniciarSesion", url);
		
		this.pausa(1000);
	    
	    WebElement linkRegistrarse = driver.findElement(By.xpath("/html/body/app-root/div/app-login1/div[1]/div/div/form/p/a"));
	    linkRegistrarse.click();
	    
	    this.pausa(1000);
	    
	    String urlRegistrar = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/Registrarse", urlRegistrar);
		
		this.pausa(1000);
	    
	    WebElement inputEmail = driver.findElement(By.xpath("/html/body/app-root/div/app-registrar1/div/div/div/form/div[1]/div/input"));
	    inputEmail.click();
	    inputEmail.sendKeys(email);
	    WebElement inputPwd1 = driver.findElement(By.xpath("/html/body/app-root/div/app-registrar1/div/div/div/form/div[2]/div/input"));
	    inputPwd1.click();
	    inputPwd1.sendKeys(pwd1);
	    WebElement inputPwd2 = driver.findElement(By.xpath("/html/body/app-root/div/app-registrar1/div/div/div/form/div[3]/div/input"));
	    inputPwd2.click();
	    inputPwd2.sendKeys(pwd2);
	    WebElement botonRegistrarse = driver.findElement(By.className("button-submit"));
	    botonRegistrarse.click();
	    
	    this.pausa(1000);
	    
	    WebElement botonEtiqueta = driver.findElement(By.xpath("/html/body/div/div/div[6]/button[1]"));
		botonEtiqueta.click();
		     
	  }
	
	private void iniciarSesion(WebDriver driver, String email, String pwd) {
		WebElement botonIniciarSesion = driver.findElement(By.xpath("/html/body/app-root/div/div/a/button"));
	    botonIniciarSesion.click();
	    
	    this.pausa(1000);
	    
	    String url = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/IniciarSesion", url);
		
		this.pausa(1000);
	    
	    WebElement inputEmail = driver.findElement(By.xpath("/html/body/app-root/div/app-login1/div[1]/div/div/form/div[1]/div/input"));
	    WebElement inputPwd = driver.findElement(By.xpath("/html/body/app-root/div/app-login1/div[1]/div/div/form/div[2]/div/input"));
	    WebElement botonLogin = driver.findElement(By.xpath("/html/body/app-root/div/app-login1/div[1]/div/div/form/div[4]/button[1]"));

	    inputEmail.sendKeys(email);
	    inputPwd.sendKeys(pwd);
	    botonLogin.click();
	    
	    this.pausa(1000);
	    
	    WebElement botonEtiqueta = driver.findElement(By.xpath("/html/body/div/div/div[6]/button[1]"));
		botonEtiqueta.click();
	  }
}
