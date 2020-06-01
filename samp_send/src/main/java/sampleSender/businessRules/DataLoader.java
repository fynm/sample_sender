package sampleSender.businessRules;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sampleSender.complexTypes.IndividualData;
import sampleSender.complexTypes.OrganizationData;
import sampleSender.complexTypes.SendData;
import sampleSender.supportTools.sendTools;

/*Data Loader into CT for processing */
public class DataLoader {
    public static void runDataLoader(SendData sendData){
        System.out.println("...running Data Loader...");
        loadInd(sendData);
        loadOrg(sendData);
        
    }

    public static void loadInd(SendData sendData){
        Connection c = sendTools.getMySQLConnect();
        String indLoader = "select * from send_ind i left join workq_data w on i.item = w.item where sendStatus = 'Y'";
        try{
            PreparedStatement pInd = c.prepareStatement(indLoader);
            ResultSet rsInd = pInd.executeQuery();
            while(rsInd.next()){
                IndividualData indData = new IndividualData();
                indData.setItemNbr(rsInd.getInt("item"));
                indData.setPrimeIdNumber(rsInd.getString("primeIdNumber"));
                indData.setStglevel(rsInd.getString("stgLevel"));
                indData.setStgLevelId(rsInd.getString("stgLevelId"));
                indData.setFirstName(rsInd.getString("firstName"));
                indData.setLastName(rsInd.getString("lastName"));
                indData.setMiddleInit(rsInd.getString("middleInit"));
                indData.setStreetAddress(rsInd.getString("streetAddress"));
                indData.setCity(rsInd.getString("city"));
                indData.setState(rsInd.getString("state"));
                indData.setZipCode(rsInd.getString("zipcode"));
                indData.setCountry(rsInd.getString("country"));
                indData.setTeleNumber(rsInd.getString("teleNumber"));
                sendData.getInd_data().add(indData);
            }

            pInd.close();
            rsInd.close();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void loadOrg(SendData sendData){
        Connection c = sendTools.getMySQLConnect();
        String orgLoader = "select * from send_org o left join workq_data w on o.item = w.item where sendStatus = 'Y'";
        try{
            PreparedStatement pOrg = c.prepareStatement(orgLoader);
            ResultSet rsOrg = pOrg.executeQuery();
            while(rsOrg.next()){
                OrganizationData orgData = new OrganizationData();
                orgData.setItemNbr(rsOrg.getInt("item"));
                orgData.setPrimeIdNumber(rsOrg.getString("primeIdNumber"));
                orgData.setEmployerId(rsOrg.getString("employerId"));
                orgData.setStgLevel(rsOrg.getString("stgLevel"));
                orgData.setStgLevelId(rsOrg.getString("stgLevelId"));
                orgData.setStreetAddress(rsOrg.getString("streetAddress"));
                orgData.setCity(rsOrg.getString("city"));
                orgData.setState(rsOrg.getString("state"));
                orgData.setZipCode(rsOrg.getString("zipcode"));
                orgData.setCountry(rsOrg.getString("country"));
                orgData.setTeleNumber(rsOrg.getString("teleNumber"));
                sendData.getOrg_data().add(orgData);
            }
            pOrg.close();
            rsOrg.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}