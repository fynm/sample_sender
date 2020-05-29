package sampleSender.supportTools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class sendTools {
    
    private static Properties properties = null;
    private static Connection mySQLConnection = null;

    /*Retrieves Specified property from properties file*/
    public static String getProperty(String prop){
        String env = "dev";
        if(properties == null){
            InputStream input = null;
            try{
                input = new FileInputStream("properties/send."+env+".properties");
                properties.load(input);
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                if(input != null){
                    try{
                        input.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            
        }
        return properties.getProperty(prop);
    }


    public static Connection getMySQLConnect(){
        if(mySQLConnection == null){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                String url = getProperty("mysqlServerURLString");
                mySQLConnection = DriverManager.getConnection(url, getProperty("mysqlUserId"), getProperty("mysqlcreds"));
                mySQLConnection.setAutoCommit(true);
            }catch(Exception e){
                e.printStackTrace();;
            }
        }
        return mySQLConnection;
    }


    public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }

    public static String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime()).toString();
    }

}