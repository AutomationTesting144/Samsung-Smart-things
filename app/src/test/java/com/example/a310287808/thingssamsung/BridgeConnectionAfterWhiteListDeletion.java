package com.example.a310287808.thingssamsung;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 11/28/2017.
 */

public class BridgeConnectionAfterWhiteListDeletion {
    public String whiteListString;
    public String[] parts;
    public int counter = 0;
    public String WLname;
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    public boolean ob3;
    Dimension size;
 //   AndroidDriver driver = null;
    public boolean result1;

    public void BridgeConnectionAfterWhiteListDeletion(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws JSONException, IOException, InterruptedException {
        driver.navigate().back();
        driver.navigate().back();
        HttpURLConnection connection;
        URL url = new URL("http://192.168.86.21/api/uYUjGN-2Qc4p5lO1FOC4NBAQZHjSJlgaWhXN6hYi/config");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer br = new StringBuffer();
        String line = " ";
        while ((line = reader.readLine()) != null) {
            br.append(line);
        }
        String output = br.toString();
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String DateToStr = format.format(curDate);

        JSONObject jsonObject = new JSONObject(output);
        Object whitelistObject = jsonObject.get("whitelist");
        whiteListString = whitelistObject.toString();

        parts = whiteListString.split("}");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("-0")) {
                counter++;
                WLname = parts[i].substring(2, 42);
                System.out.println(WLname);
                URL url1 = new URL("http://192.168.86.21/api/uYUjGN-2Qc4p5lO1FOC4NBAQZHjSJlgaWhXN6hYi/config/whitelist/" + WLname);
                HttpURLConnection httpCon = (HttpURLConnection) url1.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("DELETE");
                OutputStreamWriter out = new OutputStreamWriter(
                        httpCon.getOutputStream());
                System.out.println(httpCon.getResponseCode());
                System.out.println(httpCon.getResponseMessage());
                out.close();
//                }

            }
        }

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




        if(result1==false)try {
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

        driver.findElement(By.xpath("//android.widget.TextView[@text='Add a Thing']")).click();


        //looking for pushlink
        ob3 = driver.findElement(By.id("com.smartthings.android:id/smartalert_root")).isDisplayed();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        Boolean result = (counter == 0 && ob3 == true);

        if (result == true) {

            Status = "0";
            ActualResult = "There is no white list with the name Smart Things created today to be deleted";
            Comments = "Fail: White list with the name Smart Things is not available to delete";
            ExpectedResult = "Application should ask user to press bridge pushlink after deleting whitelist";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);

        } else {
            Status = "1";
            ActualResult = "Application is asking user to press bridge pushlink after deleting whitelist";
            Comments = "NA";
            ExpectedResult = "Application should ask user to press bridge pushlink after deleting whitelist";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        }


        //going back
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
        r2c2.setCellValue("1");

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
