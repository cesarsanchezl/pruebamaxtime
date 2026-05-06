package prueba3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class pruebatest3  {
WebDriver driver;
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
@Test
@Parameters({"Parametro1"})
    void ingresarpagina(String br){

    switch (br){
        case "chrome":driver = new ChromeDriver(); break;
        case "edge":driver = new EdgeDriver(); break;
        default: System.out.println("nevagador no encontrado");break;

    }


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));
        new WebDriverWait(driver, Duration.ofSeconds(15L));

        driver.get("https://maxtime.choucairtesting.com/");
    }

@Test(dataProvider = "dp1")
    void sigin (String usuario, String pwd){

    objectpage obj = new objectpage(driver);

    obj.setUser(usuario);
    obj.setpwd(pwd);
    obj.btnlogin();
       // driver.findElement(By.xpath("//input[@id=\":r0:\"]")).sendKeys(usuario);
        //driver.findElement(By.xpath("//input[@id=\":r1:\"]")).sendKeys(pwd);

        //driver.findElement(By.xpath("//button[normalize-space()='Ingresar']")).click();

    }


    @DataProvider(name = "dp1")
    Object[][] navegadores(){
    Object data[][]={
            {"csanchezl","Cibertec@22"}

    };
        return data;
    }

}
