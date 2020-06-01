package sampleSender.businessRules;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sampleSender.complexTypes.IndividualData;
import sampleSender.complexTypes.OrganizationData;
import sampleSender.complexTypes.SendData;
import sampleSender.supportTools.sendTools;

public class FormattingRules {
    public static void runFormattingRules(SendData sendData){
        System.out.println("...running Formatting Rules...");
        formatInd(sendData);
        formatOrg(sendData);
    }

    /*Formatting Summary
    
        Normally there will be more rulesets here but due to the limited amount
        of fields from this mock project the logic here will be less intricate.

        Most changes here are due to specified business rules that I did not include

        The purpose of this class is to abstract all changes and future changes
        that need to be done to fields of data here.
    
        I have added a few examples but this can be expanded much more.
    
    */


    public static void formatInd(SendData sendData){
        int indSize = sendData.getInd_data().size();
        for(int x = 0; x < indSize; x++){
            IndividualData iData = sendData.getInd_data().get(x);

            //Minor Example - Truncation
            if(iData.getMiddleInit().length() > 1){
                iData.setMiddleInit(iData.getMiddleInit().substring(0,1));
            }

        }
    }


    public static void formatOrg(SendData sendData){
        int orgSize = sendData.getOrg_data().size();
        for(int x=0; x< orgSize; x++){
            OrganizationData oData = sendData.getOrg_data().get(x);

            //Minor Example - Missing Data
            if(oData.getEmployerId() == null || ("").equals(oData.getEmployerId())){
                oData.setEmployerId(findEmployerId(oData.getItemNbr()));
            }
            
        }
    }

    public static String findEmployerId(int item){
        Connection c = sendTools.getMySQLConnect();
        String employerId = "";
        String getEId = "select * from workq_data where item = ?";
        try{
            PreparedStatement p = c.prepareStatement(getEId);
            p.setInt(1, item);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                String tin = rs.getString("tin");
                String maid = rs.getString("maid");
                String type = rs.getString("type");

                employerId = type + maid + tin;
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }

        return employerId;

    }
}