package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppTest extends TestCase {
    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get("https://maxtime.choucairtesting.com/");
        driver.manage().window().maximize();

        driver.findElement(By.xpath("//input[@id=\":r0:\"]")).sendKeys("csanchezl");
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\":r1:\"]")));
        pass.sendKeys("Cibertec@22");
        driver.findElement(By.xpath("//button[normalize-space()='Ingresar']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-2e16le']//table")));

        int diasProcesados = 0;

        while (true) {
            try {
                // === NUEVO: recolectar todas las filas y ordenarlas por fecha ASC ===
                List<WebElement> filas = driver.findElements(By.xpath("//div[@class='MuiBox-root css-2e16le']//table//tr"));
                List<WebElement> filasValidas = new ArrayList<>();

                // === NUEVO: definir formato de fecha en español ===
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

                // === NUEVO: mapa para asociar cada fila con su fecha ===
                Map<WebElement, LocalDate> mapaFechas = new HashMap<>();

                // === NUEVO: recorrer filas y filtrar solo las que están "Abierto" ===
                for (WebElement fila : filas) {
                    List<WebElement> celdas = fila.findElements(By.xpath(".//td"));
                    if (celdas.size() >= 10) {
                        String fechaTexto = celdas.get(6).getText().trim(); // ejemplo: "viernes, 29 de mayo de 2026"
                        String estado = celdas.get(8).getText().trim();
                        if ("Abierto".equalsIgnoreCase(estado)) {
                            try {
                                // === NUEVO: limpiar el día de la semana si existe ===
                                if (fechaTexto.contains(",")) {
                                    fechaTexto = fechaTexto.substring(fechaTexto.indexOf(",") + 1).trim();
                                    // ahora queda "29 de mayo de 2026"
                                }
                                // === NUEVO: parsear fecha con Locale español ===
                                LocalDate fecha = LocalDate.parse(fechaTexto, formatter);
                                mapaFechas.put(fila, fecha);
                                filasValidas.add(fila);
                            } catch (Exception e) {
                                System.out.println("No se pudo parsear fecha: " + fechaTexto);
                            }
                        }
                    }
                }

                // === NUEVO: si no hay filas abiertas, terminar ===
                if (filasValidas.isEmpty()) {
                    System.out.println("No hay más filas abiertas.");
                    break;
                }

                // === NUEVO: ordenar filas por fecha ascendente ===
                filasValidas.sort(Comparator.comparing(mapaFechas::get));

                // === NUEVO: tomar siempre la más antigua primero ===
                WebElement fila = filasValidas.get(0);
                // === FIN NUEVO ===

                List<WebElement> celdas = fila.findElements(By.xpath(".//td"));
                String fechaTexto = celdas.get(6).getText();
                String estado = celdas.get(8).getText();

                System.out.println("Procesando fila más antigua → Fecha: " + fechaTexto + " | Estado: " + estado);

                WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(
                        fila.findElement(By.xpath(".//button[starts-with(@aria-label,'Agregar detalle de reporte')]"))
                ));
                boton.click();

                // === Flujo original (sin cambios) ===
                driver.findElement(By.xpath("//div[@role='presentation']//div[2]//div[1]//div[1]//div[1]//div[1]//button[1]//*[name()='svg']")).click();
                driver.findElement(By.xpath("(//ul[@role='listbox']//li)[2]")).click();
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//label[contains(text(),'Tipo hora')]/following::div[@role='combobox'][1]")).click();
                driver.findElement(By.xpath("(//ul[@role='listbox']//li)[2]")).click();
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//label[contains(text(),'Tipo hora')]/following::div[@role='combobox'][2]")).click();
                driver.findElement(By.xpath("(//ul[@role='listbox']//li)[8]")).click();
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//input[@placeholder='hh:mm']")).click();
                driver.findElement(By.xpath("//input[@placeholder='hh:mm']")).sendKeys("08:00");
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//div[@class='MuiAutocomplete-root MuiAutocomplete-fullWidth MuiAutocomplete-hasPopupIcon css-1j1cs9h']//button[@title='Abrir']//*[name()='svg']")).click();
                driver.findElement(By.xpath("(//ul[@role='listbox']//li)[12]")).click();
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//input[@name='Horas']")).click();
                driver.findElement(By.xpath("//input[@name='Horas']")).sendKeys("8");
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//input[@name='tiempoAlmuerzo']")).click();
                driver.findElement(By.xpath("//input[@name='horasAlmuerzo']")).click();
                driver.findElement(By.xpath("//input[@name='horasAlmuerzo']")).sendKeys("1");
                Thread.sleep(3000L);

                WebElement scrolearComentario = driver.findElement(By.xpath("//textarea[@name='Comentario']"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", scrolearComentario);
                driver.findElement(By.xpath("//textarea[@name='Comentario']")).click();
                driver.findElement(By.xpath("//textarea[@name='Comentario']")).sendKeys("Support GCP");
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//button[text()='Guardar']")).click();
                Thread.sleep(3000L);

                String xpathFilaHoy2 = "//tr[.//td[contains(., '" + fechaTexto + "')]]";
                WebElement fila2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFilaHoy2)));
                WebElement boton2 = fila2.findElement(By.xpath(".//button[2]//*[name()='svg']"));
                boton2.click();

                Thread.sleep(3000L);
                driver.findElement(By.xpath("//button[text()='Aceptar']")).click();

                diasProcesados++;
                System.out.println("Día abierto procesado: " + diasProcesados);
                if (diasProcesados >= 30) {
                    System.out.println("Ya se procesaron 30 días abiertos.");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }

        driver.quit();
    }
}