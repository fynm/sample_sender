package sampleSender.businessRules;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;

import sampleSender.complexTypes.ItemData;
import sampleSender.supportTools.sendTools;

/*Main Business Logic */
public class SendRules {

    private static String indA1, indA2, orgO, orgP;

    public static void runSendRules() throws SQLException {
        System.out.println("...running Send Rules...");
        queryBuilder();
        /*Ind*/
        selectIndividuals();
        processA1Individuals();
        processA2Individuals();
        /*Org*/
        selectOrganizations();
    }

    public static void queryBuilder(){
        indA1 = "select * from send_ind i left join workq_data w on i.item = w.item where sendStatus is NULL and w.tStatus = 'A1' order by w.maid, w.tin, i.item ";
        indA2 = "select * from send_ind i left join workq_data w on i.item = w.item where sendStatus is NULL and w.tStatus = 'A2' order by i.primeIdNumber ";
        orgO = "select * from send_org o left join workq_data w on o.item = w.item where sendStatus is NULL and w.nType = 'O' ";
        orgP = "select * from send_org o left join workq_data w on o.item = w.item where sendStatus is NULL and w.nType = 'P' ";
    }

    public static void selectIndividuals() throws SQLException {
        System.out.println("    ... Selecting Individuals ...");
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement pA2 = null;
        ResultSet rsA2 = null;
        try{
            pA2 = c.prepareStatement(indA2);
            rsA2 = pA2.executeQuery();
            while(rsA2.next()){
                ItemData indData = new ItemData();
                indData.setItemNbr(rsA2.getInt("item"));
                indData.setComplexity(rsA2.getString("complexity"));
                indData.settStatus(rsA2.getString("tStatus"));
                indData.setTracerId(rsA2.getString("tracerId"));
                indData.setTin(rsA2.getString("tin"));
                indData.setMaid(rsA2.getString("maid"));
                indData.setProcessed(rsA2.getString("processed"));
                if(checkInd(indData)){
                    updateItem("ind", indData.getItemNbr(), "Y");
                }else{
                    updateItem("ind", indData.getItemNbr(), "N");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA2.close();
            rsA2.close();
        }

        PreparedStatement pA1 = null;
        ResultSet rsA1 = null;
        try{
            pA1 = c.prepareStatement(indA1);
            rsA1 = pA1.executeQuery();
            while(rsA1.next()){
                ItemData indData = new ItemData();
                indData.setItemNbr(rsA1.getInt("item"));
                indData.setComplexity(rsA1.getString("complexity"));
                indData.settStatus(rsA1.getString("tStatus"));
                indData.setTracerId(rsA1.getString("tracerId"));
                indData.setTin(rsA1.getString("tin"));
                indData.setMaid(rsA1.getString("maid"));
                indData.setProcessed(rsA1.getString("processed"));
                if(checkInd(indData)){
                    updateItem("ind", indData.getItemNbr(), "Y");
                }else{
                    updateItem("ind", indData.getItemNbr(), "N");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA1.close();
            rsA1.close();
        }
    }

    public static boolean checkInd(ItemData indItem) throws SQLException {
        if(!complexityCheck(indItem)){
            return false;
        }

        if(("A1").equals(indItem.gettStatus())){
            if(a1CheckSent(indItem)){
                return false;
            }
        }
        
        if(("A2").equals(indItem.gettStatus())){
            if(a2CheckSent(indItem)){
                return false;
            }
        }

        return true;
    }

    public static boolean complexityCheck(ItemData item){
        if(("C").equals(item.getComplexity())  && ("N").equals(item.getProcessed())) {
            return false;
        }
        return true;
    }

    public static boolean a1CheckSent(ItemData indItem) throws SQLException {
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement pA1 = null;
        ResultSet rsA1 = null;
        String a1CS = "select * from send_ind i left join workq_data w on i.item = w.item where w.tStatus = 'A1' and w.tin = ?";
        try{
            pA1 = c.prepareStatement(a1CS);
            pA1.setString(1, indItem.getTin());
            rsA1 = pA1.executeQuery();
            while(rsA1.next()){
                if(("S").equals(rsA1.getString("sendStatus"))){
                    return true;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA1.close();
            rsA1.close();
        }
        return false;
    }

    public static boolean a2CheckSent(ItemData indItem) throws SQLException {
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement pA2 = null;
        ResultSet rsA2 = null;
        String a2CS = "select * from send_ind i left join workq_data w on i.item = w.item where w.tStatus='A2' and w.tracerId = ?";
        try{
            pA2 = c.prepareStatement(a2CS);
            pA2.setString(1, indItem.getTracerId());
            rsA2 = pA2.executeQuery();
            while(rsA2.next()){
                if(("S").equals(rsA2.getString("sendStatus"))){
                    return true;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA2.close();
            rsA2.close();
        }
        return true;
    }

    public static void processA1Individuals() throws SQLException {
        System.out.println("    ...Processing A1 Individuals...");
        Connection c = sendTools.getMySQLConnect();
        String processA1 = "select * from send_ind i left join workq_data w on i.item = w.item where w.tStatus = 'A1' and i.sendStatus = 'Y' order by w.tin, i.item ";
        PreparedStatement pA1 = null;
        ResultSet rsA1 = null;
        try{
            pA1 = c.prepareStatement(processA1);
            rsA1 = pA1.executeQuery();
            String currentTin = "";
            while(rsA1.next()){
                if(!rsA1.getString("tin").equals(currentTin)){
                    String pTinSelect = "select * from send_ind i left join workq_data w on i.item = w.item where w.tin = ? and w.tStatus = 'A1' and sendStatus = 'Y' order by i.item";
                    PreparedStatement pTS = c.prepareStatement(pTinSelect);
                    pTS.setString(1, rsA1.getString("tin"));
                    ResultSet rsTS = pTS.executeQuery();
                    ArrayList<Integer> itemMemory = new ArrayList<Integer>();
                    while(rsTS.next()){
                        itemMemory.add(rsTS.getInt("item"));
                    }

                    if(itemMemory.size() > 0){
                        for(int x = 0; x < itemMemory.size()-1; x++){
                            //Will set all items to N except for the last one which should be the highest item number of the lot
                            updateItem("ind", itemMemory.get(x), "N");
                        }
                    }
                }
                currentTin = rsA1.getString("tin");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA1.close();
            rsA1.close();
        }
    }

    public static void processA2Individuals() throws SQLException {
        System.out.println("    ...Processing A2 Individuals...");
        Connection c = sendTools.getMySQLConnect();
        String processA2 = "select * from send_ind i left join workq_data w on i.item = w.item where w.tStatus = 'A2' and i.sendStatus = 'Y' order by w.tracerId, i.item ";
        PreparedStatement pA2 = null;
        ResultSet rsA2 = null;
        try{
            pA2 = c.prepareStatement(processA2);
            rsA2 = pA2.executeQuery();
            String currentTracerId = "";
            while(rsA2.next()){
                if(!currentTracerId.equals(rsA2.getString("tracerId"))){
                    String pTracerSelect = "select * from send_ind i left join workq_data w on i.item = w.item where w.tracerId = ? and n.tStatus = 'A02' and i.sendStatus = 'Y' order by i.item";
                    PreparedStatement pTS = c.prepareStatement(pTracerSelect);
                    pTS.setString(1, rsA2.getString("tracerId"));
                    ResultSet rsTS = pTS.executeQuery();
                    boolean holdCheck = false;
                    boolean sentCheck = false;
                    ArrayList<Integer> holdMemory = new ArrayList<Integer>();
                    ArrayList<Integer> itemMemory = new ArrayList<Integer>();
                    while(rsTS.next()){
                        if(!affiliationCheck(rsTS.getInt("item"))){
                            //hold
                            holdCheck = true;
                            holdMemory.add(rsTS.getInt("item"));
                        }else{
                            //send
                            sentCheck = true;
                            itemMemory.add(rsTS.getInt("item"));
                        }
                    }
                    if(holdCheck && !sentCheck){
                        for(int x=0; x<holdMemory.size(); x++){
                            updateItem("ind", holdMemory.get(x), "H");
                        }
                    }else if(sentCheck){
                        for(int x=0; x<holdMemory.size(); x++){
                            updateItem("ind", holdMemory.get(x), "N");
                        }
                        for(int y=0; y<itemMemory.size()-1; y++){
                            //Should update all except for the last one (highest number)
                            updateItem("ind", itemMemory.get(y), "N");
                        }
                    }
                }
                currentTracerId = rsA2.getString("tracerId");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pA2.close();
            rsA2.close();
        }
    }

    public static boolean affiliationCheck(int item){
        Connection c = sendTools.getMySQLConnect();
        String getAff = "select * from send_ind_aff where item = ?";
        PreparedStatement pAff = null;
        ResultSet rsAff = null;
        try{
            pAff = c.prepareStatement(getAff);
            pAff.setInt(1, item);
            rsAff = pAff.executeQuery();
            String groupId = "";
            while(rsAff.next()){
                groupId = rsAff.getString("groupIdNumber");
            }

            if(("").equals(groupId) || groupId == null){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }


    public static void selectOrganizations() throws SQLException {
        System.out.println("    ... Selecting Organizations ...");
        Connection c = sendTools.getMySQLConnect();
        PreparedStatement pFo = null;
        ResultSet rsFo = null;
        try{
            pFo = c.prepareStatement(orgO);
            rsFo = pFo.executeQuery();
            while(rsFo.next()){
                ItemData orgData = new ItemData();
                orgData.setItemNbr(rsFo.getInt("item"));
                orgData.setComplexity(rsFo.getString("complexity"));
                orgData.settStatus(rsFo.getString("tStatus"));
                orgData.setTracerId(rsFo.getString("tracerId"));
                orgData.setTin(rsFo.getString("tin"));
                orgData.setMaid(rsFo.getString("maid"));
                orgData.setProcessed(rsFo.getString("processed"));
                orgData.setnType(rsFo.getString("nType"));
                if(orgCheck(orgData)){
                    updateItem("org", orgData.getItemNbr(), "Y");
                }else{
                    updateItem("org", orgData.getItemNbr(), "N");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pFo.close();
            rsFo.close();
        }

        PreparedStatement  pFp = null;
        ResultSet rsFp = null;
        try{
            pFp = c.prepareStatement(orgP);
            rsFp = pFp.executeQuery();
            while(rsFp.next()){
                ItemData orgData = new ItemData();
                orgData.setItemNbr(rsFp.getInt("item"));
                orgData.setComplexity(rsFp.getString("complexity"));
                orgData.settStatus(rsFp.getString("tStatus"));
                orgData.setTracerId(rsFp.getString("tracerId"));
                orgData.setTin(rsFp.getString("tin"));
                orgData.setMaid(rsFp.getString("maid"));
                orgData.setProcessed(rsFp.getString("processed"));
                orgData.setnType(rsFp.getString("nType"));
                if(orgCheck(orgData)){
                    updateItem("org", orgData.getItemNbr(), "Y");
                }else{
                    updateItem("org", orgData.getItemNbr(), "N");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pFp.close();
            rsFp.close();
        }
    }

    public static boolean orgCheck(ItemData orgData){
        if(!complexityCheck(orgData)){
            return false;
        }

        return true;
    }

    public static void updateItem(String table, int item, String status) throws SQLException {
        Connection c = sendTools.getMySQLConnect();
        String updateQ = "";
        switch(table){
            case "ind":
                updateQ = "update send_ind set sendStatus = ? where item = ? ";
                break;
            case "org":
                updateQ = "update send_org set sendStatus = ? where item = ? ";
                break;
            default:
                System.out.println("Error incorrect table");
                break;
        }
        PreparedStatement pUp = null;
        try{
            pUp = c.prepareStatement(updateQ);
            pUp.setString(1, status);
            pUp.setInt(2,item);
            pUp.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pUp.close();
        }
    }





}