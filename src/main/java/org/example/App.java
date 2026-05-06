package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
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

public class App {
    public static void main(String[] args) throws InterruptedException {

        // Variables configurables vía parámetros (-Dproyecto, -DtipoHora, etc.)
        String proyecto = System.getProperty("proyecto", "Mantenimiento Datos");
        String tipoHora = System.getProperty("tipoHora", "H. PROYECTO");
        String servicio = System.getProperty("servicio", "DATA TESTING");
        String actividad = System.getProperty("actividad", "GESTIÓN DE PRUEBAS");
        String comentario = System.getProperty("comentario", "Support GCP");

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://maxtime.choucairtesting.com/");
        driver.manage().window().minimize();

        driver.findElement(By.xpath("//input[@id=\":r0:\"]")).sendKeys("csanchezl");
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\":r1:\"]")));
        pass.sendKeys("Cibertec@22");
        driver.findElement(By.xpath("//button[normalize-space()='Ingresar']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-2e16le']//table")));

        int diasProcesados = 0;

        while (true) {
            try {
                List<WebElement> filas = driver.findElements(By.xpath("//div[@class='MuiBox-root css-2e16le']//table//tr"));
                List<WebElement> filasValidas = new ArrayList<>();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                Map<WebElement, LocalDate> mapaFechas = new HashMap<>();

                for (WebElement fila : filas) {
                    List<WebElement> celdas = fila.findElements(By.xpath(".//td"));
                    if (celdas.size() >= 10) {
                        String fechaTexto = celdas.get(6).getText().trim();
                        String estado = celdas.get(8).getText().trim();
                        if ("Abierto".equalsIgnoreCase(estado)) {
                            try {
                                if (fechaTexto.contains(",")) {
                                    fechaTexto = fechaTexto.substring(fechaTexto.indexOf(",") + 1).trim();
                                }
                                LocalDate fecha = LocalDate.parse(fechaTexto, formatter);
                                mapaFechas.put(fila, fecha);
                                filasValidas.add(fila);
                            } catch (Exception e) {
                                System.out.println("No se pudo parsear fecha: " + fechaTexto);
                            }
                        }
                    }
                }

                if (filasValidas.isEmpty()) {
                    System.out.println("No hay más filas abiertas.");
                    break;
                }

                filasValidas.sort(Comparator.comparing(mapaFechas::get));
                WebElement fila = filasValidas.get(0);

                List<WebElement> celdas = fila.findElements(By.xpath(".//td"));
                String fechaTexto = celdas.get(6).getText();
                String estado = celdas.get(8).getText();

                System.out.println("Procesando fila más antigua → Fecha: " + fechaTexto + " | Estado: " + estado);

                WebElement boton = wait.until(ExpectedConditions.elementToBeClickable(
                        fila.findElement(By.xpath(".//button[starts-with(@aria-label,'Agregar detalle de reporte')]"))
                ));
                boton.click();

                // Proyecto
                driver.findElement(By.xpath("//div[@role='presentation']//div[2]//div[1]//div[1]//div[1]//div[1]//button[1]//*[name()='svg']")).click();
                driver.findElement(By.xpath("//ul[@role='listbox']//li//*[normalize-space(text())='" + proyecto + "']")).click();

                Thread.sleep(3000L);
                driver.findElement(By.xpath("//label[contains(text(),'Tipo hora')]/following::div[@role='combobox'][1]")).click();
                driver.findElement(By.xpath("//ul[@role='listbox']//li[normalize-space(.)='" + tipoHora + "']")).click();
                Thread.sleep(3000L);

                // Horas inicio
                driver.findElement(By.xpath("//input[@placeholder='hh:mm']")).click();
                driver.findElement(By.xpath("//input[@placeholder='hh:mm']")).sendKeys("08:00");
                Thread.sleep(3000L);

                // Servicio
                driver.findElement(By.xpath("//label[contains(text(),'Tipo hora')]/following::div[@role='combobox'][2]")).click();
                driver.findElement(By.xpath("//ul[@role='listbox']//li[normalize-space(.)='" + servicio + "']")).click();
                Thread.sleep(3000L);

                // Actividad
                driver.findElement(By.xpath("//div[@class='MuiAutocomplete-root MuiAutocomplete-fullWidth MuiAutocomplete-hasPopupIcon css-1j1cs9h']//button[@title='Abrir']//*[name()='svg']")).click();
                driver.findElement(By.xpath("//ul[@role='listbox']//li//*[normalize-space(text())='" + actividad + "']")).click();
                Thread.sleep(3000L);

                // Horas requeridas
                driver.findElement(By.xpath("//input[@name='Horas']")).click();
                driver.findElement(By.xpath("//input[@name='Horas']")).sendKeys("8");
                Thread.sleep(3000L);

                // Tiempo almuerzo
                driver.findElement(By.xpath("//input[@name='tiempoAlmuerzo']")).click();
                driver.findElement(By.xpath("//input[@name='horasAlmuerzo']")).click();
                driver.findElement(By.xpath("//input[@name='horasAlmuerzo']")).sendKeys("1");
                Thread.sleep(3000L);

                // Comentarios
                WebElement scrolearComentario = driver.findElement(By.xpath("//textarea[@name='Comentario']"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", scrolearComentario);
                driver.findElement(By.xpath("//textarea[@name='Comentario']")).click();
                driver.findElement(By.xpath("//textarea[@name='Comentario']")).sendKeys(comentario);
                Thread.sleep(3000L);

                driver.findElement(By.xpath("//button[text()='Guardar']")).click();
                Thread.sleep(3000L);

                // 🔑 Ajuste: volver a ubicar el botón aceptar después de refrescar la fila
                String xpathFilaHoy2 = "//tr[.//td[contains(., '" + fechaTexto + "')]]";
                WebElement fila2 = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathFilaHoy2))
                );

                // Localizar y hacer click en el toggle
                WebElement boton2 = fila2.findElement(By.xpath(".//button[2]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", boton2);

                // Esperar a que aparezca el modal y el botón Aceptar (refreshed para evitar stale)
                By aceptarLocator = By.xpath("//div[@role='dialog']//button[text()='Aceptar']");
                WebElement aceptarBtn = wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.elementToBeClickable(aceptarLocator)
                ));

                // Click normal
                    aceptarBtn.click();

                // Esperar a que el modal desaparezca antes de continuar
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@role='dialog']")));

                diasProcesados++;
                System.out.println("Día abierto procesado: " + diasProcesados);
                if (diasProcesados >= 2) {
                    System.out.println("Ya se procesaron " + diasProcesados + " días abiertos.");
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