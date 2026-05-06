package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));
        new WebDriverWait(driver, Duration.ofSeconds(15L));
        driver.get("https://www.sodimac.com.pe/sodimac-pe");
        driver.manage().window().maximize();

        WebElement pop = driver.findElement(By.xpath("//span[@id='closePopup']"));
        pop.click();

        driver.findElement(By.xpath("//input[@id='testId-SearchBar-Input']")).sendKeys(new CharSequence[]{"escri"});
        List<WebElement> lista = driver.findElements(By.xpath("//li[@class='SearchList-module_searchListItem__JYqjK']"));
        System.out.println("lista" + lista.size());

        for(int i=0;i< lista.size();i++){
        if(i==3){
            lista.get(i).click();
        }

        }


    }
}
