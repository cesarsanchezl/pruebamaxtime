import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class objectpage {

    WebDriver driver;
            objectpage(WebDriver driver){
        this.driver= driver;
            }

            By txt_username = By.xpath("//input[@id=\":r0:\"]");
             By txt_password = By.xpath("//input[@id=\":r1:\"]");
             By btnlogging = By.xpath("//button[normalize-space()='Ingresar']");


             public void setUser(String usuario){
            driver.findElement(txt_username).sendKeys(usuario);
             }

            public void setpwd(String pwd){
                driver.findElement(txt_password).sendKeys(pwd);
                }
             public void btnlogin(){
                 driver.findElement(btnlogging).click();
             }


}
