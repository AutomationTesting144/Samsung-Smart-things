package com.example.a310287808.thingssamsung;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 12/1/2017.
 */

public class SingleLightBrightness {
    public String IPAddress = "192.168.86.21/api";
    public String HueUserName = "uYUjGN-2Qc4p5lO1FOC4NBAQZHjSJlgaWhXN6hYi";
    public String HueBridgeParameterType = "lights/19";
    public String finalURL;
    public String lightStatusReturned;
    public String Status;
    public String ActualResult;
    public String Comments;
    public String lightName;
    public String ExpectedResult;


    public void SingleLightBrightness(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException, ParseException {
        driver.navigate().back();
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

        //Look for the Testing Lamp
        driver.findElement(By.xpath("//android.widget.TextView[@text='TestingLamp']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Clicking on brightness bar
        driver.findElement(By.id("com.smartthings.android:id/multi_slider_progress_bar")).click();
        TimeUnit.SECONDS.sleep(2);

        //changing brightness
        driver.findElement(By.id("com.smartthings.android:id/vertical_slider")).click();
        TimeUnit.SECONDS.sleep(10);

        HttpURLConnection connection;
        finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType;
        URL url = new URL(finalURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer br = new StringBuffer();
        String line = " ";
        while ((line = reader.readLine()) != null) {
            br.append(line);
        }

        String output1 = br.toString();
        JSONObject jsonObject = new JSONObject(output1);

        Object ob = jsonObject.get("state");
        String newString = ob.toString();
        JSONObject jsonObject1 = new JSONObject(newString);
        Object ob1 = jsonObject1.get("bri");


        Object lightNameObject = jsonObject.get("name");
        lightName = lightNameObject.toString();

        br.append(lightName);
        br.append("\n");


        if (ob1.toString().equals("254")) {

            Status = "0";
            ActualResult ="Brightness for the light is not changed: "+"\n"+lightName;
            Comments = "FAIL:Light is not able to change its brightness";
            ExpectedResult= " Light: "+lightName+" should change its brightness successfully";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        } else
        {
            Status = "1";
            ActualResult ="Brightness for the light is  changed: "+"\n"+lightName;
            Comments = "NA";
            ExpectedResult= " Light: "+lightName+" should change its brightness successfully";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);
    }

    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel(String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult
            ,String resultAPIVersion, String resultSWVersion) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber=workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("8");

        HSSFCell r2c3 = row2.createCell((short)2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short)3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short)4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short)5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short)6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();


    }
}
