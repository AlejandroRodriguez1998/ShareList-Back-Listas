package edu.uclm.esi.listasbe.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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

	@BeforeAll // @BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\\\Users\\\\Andrés\\\\Downloads\\\\chromedriver-win64\\\\chromedriver.exe");
		
		
		//webdriver.edge.driver
		//C:\\Users\\Andrés\\Downloads\\edgedriver_win64\\msedgedriver.exe
		
		ChromeOptions options = new ChromeOptions();
		//EdgeOptions options = new EdgeOptions();
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--allow-insecure-localhost");
		

		options.setBinary("C:\\Users\\Andrés\\Downloads\\chrome-win64\\\\chrome.exe");
		options.addArguments("--remote-allow-origins=*");

		driverPepe = new ChromeDriver(options);
		//driverPepe = new EdgeDriver(options);
		new WebDriverWait(driverPepe, Duration.ofSeconds(3));

		jsExecutor = (JavascriptExecutor) driverPepe;
		vars = new HashMap<String, Object>();

		driverAna = new ChromeDriver(options);
		//driverPepe = new EdgeDriver(options);
		new WebDriverWait(driverAna, Duration.ofSeconds(3));

		jsExecutor2 = (JavascriptExecutor) driverAna;
		vars2 = new HashMap<String, Object>();

		driverPepe.get("https://localhost:4200/");
		driverAna.get("https://localhost:4200/");
		this.pausa(1000);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		driverPepe.manage().window().setPosition(new Point(0, 0));
		driverPepe.manage().window().setSize(new Dimension(screenWidth / 2, screenHeight));

		driverAna.manage().window().setPosition(new Point((screenWidth / 2) + 1, 0));
		driverAna.manage().window().setSize(new Dimension(screenWidth / 2, screenHeight));
	}

	@AfterEach
	public void tearDown() {

	}

	public void testCookies() {
		
		driverPepe.get("chrome://settings/trackingProtection?search=cookies");
		
		JavascriptExecutor js = (JavascriptExecutor) driverPepe;
		js.executeScript("document.body.style.zoom='50%'");

		this.pausa(1000);
		
		WebElement botonAñadirPepe = driverPepe
				.findElement(By.xpath("cr-button"));
		
		botonAñadirPepe.click();
		
		this.pausa(1000);
		
		WebElement input = driverPepe.findElement(By.xpath("/html/body/settings-ui//div[2]/settings-main//settings-basic-page//div[1]/settings-section[5]/settings-privacy-page//settings-animated-pages/settings-subpage[10]/settings-cookies-page//site-list//add-site-dialog//cr-dialog/div[2]/cr-input//div[2]/div/div[1]/div[2]/input"));
		
		input.sendKeys("https://localhost:4200");
		
		WebElement boton = driverPepe.findElement(By.xpath("/html/body/settings-ui//div[2]/settings-main//settings-basic-page//div[1]/settings-section[5]/settings-privacy-page//settings-animated-pages/settings-subpage[10]/settings-cookies-page//site-list//add-site-dialog//cr-dialog/div[3]/cr-button[2]"));
		
		boton.click();		
		driverPepe.get("https://localhost:4200/");
	}
	
	@Test
	@Order(0)
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

		WebElement inputNombreLista = driverPepe
				.findElement(By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[1]/input"));
		inputNombreLista.sendKeys("Cumpleaños");
		WebElement botonCrear = driverPepe
				.findElement(By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[1]/button"));
		botonCrear.click();

		this.pausa(2000);

		WebDriverWait wait = new WebDriverWait(driverPepe, Duration.ofSeconds(10));
		WebElement nombreLista = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[2]/div/div/div[1]/h5")));
		
		assertEquals("Cumpleaños", nombreLista.getText());

		WebElement botonAñadirProducto = driverPepe.findElement(
				By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[2]/div/div/div[2]/div[2]/span[1]"));
		botonAñadirProducto.click();

		this.pausa(1000);

		this.anadirProductos(this.driverPepe, "Latas de cerveza", "30");

		driverPepe.navigate().refresh();

		this.pausa(1000);

		this.anadirProductos(this.driverPepe, "Tarta", "1");

		driverPepe.navigate().refresh();

		this.pausa(1000);

		this.anadirProductos(this.driverPepe, "Bolsas patatas fritas", "2");

		driverPepe.navigate().refresh();

		this.pausa(1000);
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

	@Test
	@Order(3)
	public void testInvitacion() {
		WebElement botonInvitar = driverPepe.findElement(
				By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[2]/div/div/div[2]/div[2]/span[1]"));
		botonInvitar.click();
		WebDriverWait waitPepe = new WebDriverWait(driverPepe, Duration.ofSeconds(10));
		WebElement invitar = waitPepe
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/h2")));

		WebElement botonCopiarEnlace = driverPepe.findElement(By.xpath("/html/body/div[2]/div/div[2]/button"));

		botonCopiarEnlace.click();

		WebElement botonEtiqueta = waitPepe
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[6]/button[1]")));
		
		botonEtiqueta.click();

		this.pausa(1000);
	}

	@Test
	@Order(4)
	public void testAceptarInvitacion() {
		// Copia contenido portapapeles
		String contenidoPortapapeles = obtenerContenidoPortapapeles();

		this.pausa(1000);

		// cambia de ventana en el driverAna
		((JavascriptExecutor) driverAna).executeScript("window.open()");
		String originalHandle = driverAna.getWindowHandle();
		for (String handle : driverAna.getWindowHandles()) {
			if (!handle.equals(originalHandle)) {
				driverAna.switchTo().window(handle);
				break;
			}
		}

		this.pausa(3000);

		// Encuentra un campo de texto y pega el contenido del portapapeles
		Actions actions = new Actions(driverAna);
		actions.keyDown(Keys.CONTROL).sendKeys("l").keyUp(Keys.CONTROL).perform(); // Ctrl + L para enfocar la barra
		actions.sendKeys(contenidoPortapapeles).perform();

		actions.sendKeys(Keys.ENTER).perform();

		WebDriverWait waitAna = new WebDriverWait(driverAna, Duration.ofSeconds(10));
		WebElement botonAceptarInvitacion = waitAna.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("/html/body/app-root/div/div/app-invitacion/div/div/div/button")));
		
		botonAceptarInvitacion.click();

		this.pausa(3000);
		
		WebElement botonEtiqueta = waitAna
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[6]/button[1]")));

		botonEtiqueta.click();

		String url = driverAna.getCurrentUrl();
		assertEquals("https://localhost:4200/GestionarListas", url);

	}
	
	@Test
	@Order(5)
	public void testCompraTarta() {
		WebElement celdaComprarTarta = driverAna.findElement(By.xpath(
				"/html/body/app-root/div/div/app-gestor-listas/section/div/div[2]/div/div/div[1]/table/tbody/tr[td[contains(text(), 'Tarta')]]/td[5]"));
		celdaComprarTarta.sendKeys("1");
		
		WebElement celdaCompradasTarta = driverPepe.findElement(By.xpath("/html/body/app-root/div/div/app-gestor-listas/section/div/div[2]/div/div/div[1]/table/tbody/tr[td[contains(text(), 'Tarta')]]/td[2]"));
		 String valorCompradas = celdaCompradasTarta.getText();
		 
		 assertEquals(valorCompradas, "1");
	}
	
		
	

	private void pausa(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String obtenerContenidoPortapapeles() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private void registrar(WebDriver driver, String email, String pwd1, String pwd2) {
		WebElement botonIniciarSesion = driver.findElement(By.xpath("/html/body/app-root/div/nav/div/div[2]/a/button"));
		botonIniciarSesion.click();

		this.pausa(1000);

		String url = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/IniciarSesion", url);

		this.pausa(1000);

		WebElement linkRegistrarse = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-login1/div[1]/div/div/form/p/a"));
		linkRegistrarse.click();

		this.pausa(1000);

		String urlRegistrar = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/Registrarse", urlRegistrar);

		this.pausa(1000);

		WebElement inputEmail = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-registrar1/div/div/div/form/div[1]/div/input"));
		inputEmail.click();
		inputEmail.sendKeys(email);
		WebElement inputPwd1 = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-registrar1/div/div/div/form/div[2]/div/input"));
		inputPwd1.click();
		inputPwd1.sendKeys(pwd1);
		WebElement inputPwd2 = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-registrar1/div/div/div/form/div[3]/div/input"));
		inputPwd2.click();
		inputPwd2.sendKeys(pwd2);
		WebElement botonRegistrarse = driver.findElement(By.className("button-submit"));
		botonRegistrarse.click();

		this.pausa(1000);

		WebElement botonEtiqueta = driver.findElement(By.xpath("/html/body/div/div/div[6]/button[1]"));
		botonEtiqueta.click();

	}

	private void iniciarSesion(WebDriver driver, String email, String pwd) {
		WebElement botonIniciarSesion = driver.findElement(By.xpath("/html/body/app-root/div/nav/div/div[2]/a/button"));
		botonIniciarSesion.click();

		this.pausa(1000);

		String url = driver.getCurrentUrl();
		assertEquals("https://localhost:4200/IniciarSesion", url);

		this.pausa(1000);

		WebElement inputEmail = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-login1/div[1]/div/div/form/div[1]/div/input"));
		WebElement inputPwd = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-login1/div[1]/div/div/form/div[2]/div/input"));
		WebElement botonLogin = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-login1/div[1]/div/div/form/div[4]/button[1]"));
		
		this.pausa(1000);
		
		inputEmail.sendKeys(email);
		inputPwd.sendKeys(pwd);
		
		this.pausa(1000);
		
		botonLogin.click();

		this.pausa(1000);

		WebElement botonEtiqueta = driver.findElement(By.xpath("/html/body/div/div/div[6]/button[1]"));
		
		this.pausa(2000);
		
		botonEtiqueta.click();
	}

	private void anadirProductos(WebDriver driver, String producto, String cantidad) {
		WebElement nombreProducto = driver.findElement(
				By.xpath("/html/body/app-root/div/div/app-gestor-listas/div[1]/div/div/div[2]/div[1]/div/div/input"));
		WebElement cantidadProducto = driver.findElement(
				By.xpath("/html/body/app-root/div/div/app-gestor-listas/div[1]/div/div/div[2]/div[2]/div/div/input"));
		WebElement botonAnadir = driver
				.findElement(By.xpath("/html/body/app-root/div/div/app-gestor-listas/div[1]/div/div/div[3]/button"));

		nombreProducto.sendKeys(producto);
		cantidadProducto.sendKeys(cantidad);

		this.pausa(1000);

		botonAnadir.click();
	}
}
