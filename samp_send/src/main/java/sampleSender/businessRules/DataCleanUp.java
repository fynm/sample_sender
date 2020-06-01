package sampleSender.businessRules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sampleSender.supportTools.sendTools;

public class DataCleanUp {
    public static void runDataCleanup() throws SQLException {
        System.out.println("...running data cleanup...");
        revertHeldItems();
    }

    public static void revertHeldItems() throws SQLException {
        System.out.println("    ...reverting HOLD items...");
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement pHold = null;
        String revertHold = "update send_ind set sendStatus = null where sendStatus = 'H'";
        try{
            pHold = c.prepareStatement(revertHold);
            pHold.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pHold.close();
        }
    }
}