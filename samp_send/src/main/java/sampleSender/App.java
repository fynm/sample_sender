package sampleSender;

import sampleSender.supportTools.FileFTP;
import sampleSender.supportTools.MockDataGenerator;
import sampleSender.supportTools.fileGenerator;
import sampleSender.supportTools.sendTools;
import sampleSender.businessRules.SendRules;
import sampleSender.businessRules.DataCleanUp;
import sampleSender.businessRules.DataFormatter;
import sampleSender.businessRules.DataLoader;
import sampleSender.businessRules.FormattingRules;
import sampleSender.complexTypes.SendData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public final class App {
    private App() {
    }
    public static void main(String[] args) throws SQLException {

        /*Testing Database Connection - no relation to program logic*/
        //databaseConnTest();
        databaseEntryCheck();

        //MockDataGenerator.runDataUpdate();
        //MockDataGenerator.dataEntry();
        //MockDataGenerator.generateAff();
        //System.exit(0);

        System.out.println("Initiating Sample Send ...");

        /*Initial Data Format Logic*/
        DataFormatter.runDataFormatter();

        /*Set up File */
        fileGenerator.setPrintWriter();

        /*Generate Header in File */
        fileGenerator.generateHeader();

        /*Initialize Data Object */
        SendData sendData = new SendData();

        /*Main Business Rules */
        SendRules.runSendRules();

        /*Data Load into Data Object and Format Data*/
        DataLoader.runDataLoader(sendData);
        FormattingRules.runFormattingRules(sendData);

        /*Write Data to file */
        fileGenerator.generateBody(sendData);
        fileGenerator.generateTrailer();
        fileGenerator.flushPW();

        /*FTP File to Destination*/
        FileFTP.ftpSend();

        /*Data Cleanup / Oneshot Data changes */
        DataCleanUp.runDataCleanup();


        /*Close PrintWriter */
        fileGenerator.closePW();

        System.out.println("... Sample Send Complete");
    }


    public static void databaseConnTest(){
        Connection conn = null;
 
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/samplesenderdb";
            String user = "root";
            String pass = "admin";
            conn = DriverManager.getConnection(dbURL, user, pass);
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
 
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void databaseEntryCheck(){
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement p = null;
        ResultSet rs = null;
        String sql = "select * from workq_data where item = 1";
        String tracerId = "";
        try{
            p = c.prepareStatement(sql);
            rs = p.executeQuery();
            while(rs.next()){
                tracerId = rs.getString("tracerId");
            }

            //System.out.println("tracerId: "+tracerId);

            p.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
