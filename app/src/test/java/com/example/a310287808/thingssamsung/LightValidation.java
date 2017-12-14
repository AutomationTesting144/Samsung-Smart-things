package com.example.a310287808.thingssamsung;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 11/30/2017.
 */

public class LightValidation {

    public MobileElement RoomlistItem1;
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    public int notToTalkCounter=0;
    public MobileElement listItem;
    public List RoomList;
    public List RoomList2;
    public List lighList;
    Dimension size;
    AndroidDriver driver;

    public void LightValidation(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException {

        driver.navigate().back();
        //Opening Hue applictaion
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,200][1074,248]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Selecting light setup
        driver.findElement(By.xpath("//android.widget.TextView[@text='Light setup']")).click();
        TimeUnit.SECONDS.sleep(2);
        //getting the list of rooms available in the application and inserting it in Hashmap
        driver.findElement(By.id("com.philips.lighting.hue2:id/list_item_title")).isDisplayed();
        HashMap<Object, Integer> Rooms = new HashMap<>();

        RoomList = driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title"));
        for(int i=0; i< RoomList.size(); i++){

            if((RoomList.get(i)=="Living room")&&(RoomList.get(i)=="Bedroom")) {
                RoomList.remove(i);
                i++;
            }else{
                RoomlistItem1 = (MobileElement) RoomList.get(i);
            }

            System.out.println(RoomlistItem1.getText());
        }

        for(int j=0; j<RoomList.size(); j++){
            if((RoomlistItem1.getText()=="Living room")&&(RoomlistItem1.getText()=="Bedroom"))
            {
                j++;
            }else {
       //     RoomList2=RoomlistItem1.getText();
            }

        }






        //Going back from the application

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        //opening Smart Things
        //Opening Smart Things Application
        driver.findElement(By.xpath("//android.widget.TextView[@text='SmartThings']")).click();
        TimeUnit.SECONDS.sleep(5);

        //click on My home button
        driver.findElement(By.xpath("//android.widget.TextView[@text='My Home']")).click();
        TimeUnit.SECONDS.sleep(2);

        //click on Things Tab
        driver.findElement(By.xpath("//android.widget.TextView[@text='Things']")).click();
        TimeUnit.SECONDS.sleep(2);

        List lightBoxContainer = driver.findElements(By.className("android.widget.RelativeLayout"));
        MobileElement firstE = (MobileElement)lightBoxContainer.get((lightBoxContainer.size()/2));

        int dy = firstE.getSize().getHeight();
        size = driver.manage().window().getSize();
        driver.swipe(size.getWidth()/2, size.getHeight()/2, size.getWidth()/2, size.getHeight()/2 - dy, 3000);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> lightList= new ArrayList<String>();
        Boolean status2=true;
        List dropList = null;
        dropList = driver.findElements(By.id("com.smartthings.android:id/device_tile_device_name"));
        addToList(lightList, dropList);
        By by = By.xpath("//android.widget.TextView[@text='Add a Thing']");
        while(status2==true)
        {


            By addThingBtnPath = By.xpath("//android.widget.TextView[@text='Add a Thing']");
            if(hasElement(addThingBtnPath))
                status2 = false;
            else
            {
                driver.swipe(size.getWidth()/2, size.getHeight()/2, size.getWidth()/2, size.getHeight()/2 - dy, 3000);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Extract text from each element of drop down list one by one.
                dropList = driver.findElements(By.id("com.smartthings.android:id/device_tile_device_name"));

//                addToList(lightList, dropList);
                if(!hasElement(addThingBtnPath))
                    lightList.add(((MobileElement)dropList.get(dropList.size()-1)).getText());
            }
        }
        Assert.assertEquals(lightList, RoomList);



//        if(RoomList.size()==notToTalkCounter){
//            Status = "1";
//            ActualResult = "Same rooms are available in Hue applications and IFTTT";
//            Comments = "NA";
//            ExpectedResult = "Same rooms should be available in Hue applications and IFTTT";
//            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
//        }
//        else{
//            Status = "0";
//            ActualResult = "Same rooms are  not available in Hue applications and IFTTT";
//            Comments = "Fail: Same rooms are not available";
//            ExpectedResult = "Same rooms should be available in Hue applications and IFTTT";
//        }

        //Going back from the application
        driver.navigate().back();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.id("android:id/button1")).click();
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);
    }
    void addToList( List<String> list, List mobileElements){

        //Extract text from each element of drop down list one by one.
        for (int j = 0; j < mobileElements.size(); j++)
        {
            MobileElement listItem = (MobileElement) mobileElements.get(j);

            list.add(listItem.getText());
        }
    }
    private boolean hasElement(By by)
    {
        try {
            MobileElement result = (MobileElement) driver.findElement(by);
            return true;
        }
        catch (java.lang.NullPointerException e)
        {
            return false;
        }
    }


    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel (String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult, String resultAPIVersion, String resultSWVersion) throws IOException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber = workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short)0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short)1);
        r2c2.setCellValue("5");

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
        FileOutputStream out =new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();

    }

}
