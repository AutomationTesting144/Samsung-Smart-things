package com.example.a310287808.thingssamsung;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 11/29/2017.
 */

public class BridgeAuthenticationFirstTime {
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    public boolean ob3;
    Dimension size;
    public boolean result1;

    public void BridgeAuthenticationFirstTime(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws JSONException, IOException, InterruptedException {
        driver.navigate().back();
        driver.navigate().back();

        //Opening Smart Things Application
        driver.findElement(By.xpath("//android.widget.TextView[@text='SmartThings']")).click();
        TimeUnit.SECONDS.sleep(5);

        //click on My home button
        driver.findElement(By.xpath("//android.widget.TextView[@text='My Home']")).click();
        TimeUnit.SECONDS.sleep(2);

        //click on Things Tab
        driver.findElement(By.xpath("//android.widget.TextView[@text='Things']")).click();
        TimeUnit.SECONDS.sleep(2);


        By by = By.xpath("//android.widget.TextView[@text='Add a Thing']");

        try {
            MobileElement result = (MobileElement) driver.findElement(by);
            result1= true;
            if(result1==true){
                driver.findElement(By.xpath("//android.widget.TextView[@text='Add a Thing']")).click();
            }
        }
        catch (org.openqa.selenium.NoSuchElementException e)
        {
            result1= false;
        }

        if(result1==false)
        {
            size = driver.manage().window().getSize();

            //Find starty point which is at bottom side of screen.
            int starty = (int) (size.height * 0.80);
            //Find endy point which is at top side of screen.
            int endy = (int) (size.height * 0.20);
            //Find horizontal point where you wants to swipe. It is in middle of screen width.
            int startx = size.width / 2;

            //Swipe from Bottom to Top.
            driver.swipe(startx, starty, startx, endy, 1000);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//android.widget.TextView[@text='Add a Thing']")).click();
        }




        //looking for pushlink
        ob3 = driver.findElement(By.id("com.smartthings.android:id/smartalert_root")).isDisplayed();
        TimeUnit.SECONDS.sleep(2);
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();


        if (ob3 == true) {

            Status = "1";
            ActualResult = "Application is asking user to press bridge pushlink while connecting for first time";
            Comments = "NA";
            ExpectedResult= "Application should ask user to press bridge pushlink while connecting for first time";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        } else {
            Status = "0";
            ActualResult = "Application is not asking user to press bridge pushlink while connecting for first time";
            Comments = "FAIL: Bridge is already connected to the application";
            ExpectedResult= "Application should ask user to press bridge pushlink while connecting for first time";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);
        }
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();


        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult, APIVersion, SWVersion);
    }

    public String CurrentdateTime;
    public int nextRowNumber;

    public void storeResultsExcel(String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult
            , String resultAPIVersion, String resultSWVersion) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber = workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("2");

        HSSFCell r2c3 = row2.createCell((short) 2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short) 3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short) 4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short) 5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short) 6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();


    }

}
