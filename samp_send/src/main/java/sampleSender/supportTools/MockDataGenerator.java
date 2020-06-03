package sampleSender.supportTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


public class MockDataGenerator {
    public static void runDataUpdate(){
        Connection c = sendTools.getMySQLConnect();
        try{
            String sql = "select * from workq_data";
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                if(rs.getString("nType").equals("P") && rs.getString("tStatus").equals("A1") && !("7").equals(rs.getString("phase"))){ 
                    //IND A1
                    int item = rs.getInt("item");
                    String sqlInd = "select * from send_ind where item = ?";
                    PreparedStatement pind = c.prepareStatement(sqlInd);
                    pind.setInt(1, item);
                    ResultSet rsind = pind.executeQuery();
                    boolean itExists = false;
                    while(rsind.next()){
                        if(rsind.getInt("item") > 0){
                            itExists = true;
                        }
                    }

                    String sqlOrg = "select * from send_org where item = ? ";
                    PreparedStatement pOrg = c.prepareStatement(sqlOrg);
                    pOrg.setInt(1, item);
                    ResultSet rsOrg = pOrg.executeQuery();
                    while(rsOrg.next()){
                        if(rsOrg.getInt("item")> 0){
                            itExists = true;
                        }
                    }

                    if(!itExists){
                        String insertIndA1 = "insert into send_ind (item) values (?)";
                        PreparedStatement pA1 = c.prepareStatement(insertIndA1);
                        pA1.setInt(1, item);
                        pA1.execute();
                        pA1.close();
                    }

                    

                }else if(rs.getString("nType").equals("P") && rs.getString("tStatus").equals("A2")){ 
                    //Ind A2
                    int item = rs.getInt("item");
                    String sqlInd = "select * from send_ind where item = ?";
                    PreparedStatement pind = c.prepareStatement(sqlInd);
                    pind.setInt(1, item);
                    ResultSet rsind = pind.executeQuery();
                    boolean itExists = false;
                    while(rsind.next()){
                        if(rsind.getInt("item") > 0){
                            itExists = true;
                        }
                    }

                    if(!itExists){
                        String insertIndA2 = "insert into send_ind (item) values (?)";
                        PreparedStatement pA2 = c.prepareStatement(insertIndA2);
                        pA2.setInt(1, item);
                        pA2.execute();
                        pA2.close();
                    }
                   
                }else if(rs.getString("nType").equals("O")){ 
                    //Org O
                    int item = rs.getInt("item");
                    String sqlOrg = "select * from send_org where item = ?";
                    PreparedStatement pOrg = c.prepareStatement(sqlOrg);
                    pOrg.setInt(1, item);
                    ResultSet rsOrg = pOrg.executeQuery();
                    boolean itExists = false;
                    while(rsOrg.next()){
                        if(rsOrg.getInt("item") > 0){
                            itExists = true;
                        }
                    }

                    if(!itExists){
                        String insertORGO = "insert into send_org (item) values (?)";
                        PreparedStatement pO = c.prepareStatement(insertORGO);
                        pO.setInt(1, item);
                        pO.execute();
                        pO.close();
                    }
  
                }else if(rs.getString("nType").equals("P") && rs.getString("phase").equals("7")){ 
                    //Org P

                    int item = rs.getInt("item");
                    String sqlOrg = "select * from send_org where item = ?";
                    PreparedStatement pOrg = c.prepareStatement(sqlOrg);
                    pOrg.setInt(1, item);
                    ResultSet rsOrg = pOrg.executeQuery();
                    boolean itExists = false;
                    while(rsOrg.next()){
                        if(rsOrg.getInt("item") > 0){
                            itExists = true;
                        }
                    }

                    if(!itExists){
                        String insertORGP = "insert into send_org (item) values (?)";
                        PreparedStatement pP = c.prepareStatement(insertORGP);
                        pP.setInt(1, item);
                        pP.execute();
                        pP.close();
                    }
                    
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void dataEntry(){
        //TODO set up data entry for TracerID / PrimeIDNbr / STGLEvelID / ETC
        Connection c = sendTools.getMySQLConnect();
        String getworkq = "select * from workq_data where tracerId is null";
        try{
            PreparedStatement p = c.prepareStatement(getworkq);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                int item = rs.getInt("item");
                int tinN = (((item*888)+123456)/2);
                String tin = Integer.toString(tinN).substring(0,5);
                int maidN = ((item*2222)+12345678)/2;
                String maid = Integer.toString(maidN).substring(0,6);
                String region = "1";
                String phase = "1";
                String tracerId = "N"+tin+"_"+maid;
                String updateWorkq = "update workq_data set tin = ?, maid = ?, region = ?, phase= ?, tracerId = ? where item = ?";
                PreparedStatement pp = c.prepareStatement(updateWorkq);
                pp.setString(1, tin);
                pp.setString(2, maid);
                pp.setString(3, region);
                pp.setString(4, phase);
                pp.setString(5, tracerId);
                pp.setInt(6, item);
                pp.executeUpdate();
                pp.close();    
            }
            p.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        String getInd = "select * from send_ind i left join workq_data w on i.item = w.item where primeIdNumber is null";
        try{
            PreparedStatement p = c.prepareStatement(getInd);
            ResultSet rs = p.executeQuery();
            ArrayList<String> namesList = new ArrayList<String>();
            ArrayList<String> lastNamesList = new ArrayList<String>();

            namesList.add("Shawn");
            namesList.add("Leon");
            namesList.add("Monica");
            namesList.add("Mike");
            namesList.add("X Æ A-12");
            namesList.add("Elon");
            lastNamesList.add("walnut");
            lastNamesList.add("Fong");
            lastNamesList.add("Musk");
            lastNamesList.add("Iphone11");
            lastNamesList.add("Bottle");
            lastNamesList.add("dog");

            while(rs.next()){
                int item = rs.getInt("item");
                String primeIdNumber = "P"+rs.getString("tin")+"_"+rs.getString("maid");
                String stgLevelId = Integer.toString(item)+rs.getString("maid")+rs.getString("tin")+Integer.toString(item*7);
                Random rand = new Random();
                int rand1 = rand.nextInt(6);
                int rand2 = rand.nextInt(6);
                String firstName = namesList.get(rand1);
                String lastName = lastNamesList.get(rand2);
                String middleInit = "Z";
                String streetAddress = Integer.toString(rand1)+" Street Avenue";
                String city = "city "+Integer.toString(rand2);
                String state = "PA";
                String zipcode = Integer.toString(rand1)+"354"+Integer.toString(rand2);
                String country = "USA";
                String teleNumber = "732"+rs.getString("maid")+Integer.toString(rand2);
                String updateInd = "update send_ind set primeIdNumber = ?, stgLevelId = ?, firstName = ?, lastName = ?, middleInit = ?, streetAddress = ?, city = ?, state = ?, zipcode = ?, country = ?, teleNumber = ? where item = ? ";
                PreparedStatement pp = c.prepareStatement(updateInd);
                pp.setString(1, primeIdNumber);
                pp.setString(2, stgLevelId);
                pp.setString(3, firstName);
                pp.setString(4, lastName);
                pp.setString(5, middleInit);
                pp.setString(6, streetAddress);
                pp.setString(7, city);
                pp.setString(8, state);
                pp.setString(9, zipcode);
                pp.setString(10, country);
                pp.setString(11, teleNumber);
                pp.setInt(12, item);
                pp.executeUpdate();
                pp.close();

                if(("A2").equals(rs.getString("tStatus"))){
                    String updateAff = "update send_ind_aff set primeIdNumber = ?, groupIdNumber = ? where item = ?";
                    PreparedStatement pa = c.prepareStatement(updateAff);
                    pa.setString(1, primeIdNumber);
                    pa.setString(2, primeIdNumber+Integer.toString(item));
                    pa.setInt(3, item);
                    pa.executeUpdate();
                    pa.close();
                }

            }
            p.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

        String getOrg = "select * from send_org o left join workq_data w on o.item = w.item where primeIdNumber is null";
        try{
            PreparedStatement p = c.prepareStatement(getOrg);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                int item = rs.getInt("item");
                String primeIdNumber = "P"+rs.getString("tin")+"_"+rs.getString("maid");
                String stgLevelId = Integer.toString(item)+rs.getString("maid")+rs.getString("tin")+Integer.toString(item*7);
                Random rand = new Random();
                int rand1 = rand.nextInt(6);
                int rand2 = rand.nextInt(6);
                String employerId = Integer.toString(item) + rs.getString("tin") + Integer.toString(rand1) + Integer.toString(rand2); 
                String streetAddress = Integer.toString(rand1)+" Street Avenue";
                String city = "city "+Integer.toString(rand2);
                String state = "PA";
                String zipcode = Integer.toString(rand1)+"354"+Integer.toString(rand2);
                String country = "USA";
                String teleNumber = "732"+rs.getString("maid")+Integer.toString(rand2);
                String updateOrg = "update send_org set primeIdNumber = ?, employerId = ?, stgLevelId = ?, streetAddress = ?, city = ?, state = ?, zipcode = ?, country = ?, teleNumber = ? where item = ?";
                PreparedStatement pp = c.prepareStatement(updateOrg);
                pp.setString(1, primeIdNumber);
                pp.setString(2, employerId);
                pp.setString(3, stgLevelId);
                pp.setString(4, streetAddress);
                pp.setString(5, city);
                pp.setString(6, state);
                pp.setString(7, zipcode);
                pp.setString(8, country);
                pp.setString(9, teleNumber);
                pp.setInt(10, item);
                pp.executeUpdate();
                pp.close();

            }
            rs.close();
            p.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void generateAff(){
        Connection c = sendTools.getMySQLConnect();
        String sel =  "select * from send_ind i left join workq_data w on i.item = w.item where tStatus = 'A2' ";
        try{
            PreparedStatement p = c.prepareStatement(sel);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                int item = rs.getInt("item");
                String checkAff = "select * from send_ind_aff where item = ?";
                PreparedStatement pa = c.prepareStatement(checkAff);
                pa.setInt(1, item);
                ResultSet rsa = pa.executeQuery();
                boolean isExist = false;
                
                while(rsa.next()){
                    if(rsa.getInt("item") > 0){
                        isExist = true;
                    }
                }
                String primeIdNumber=rs.getString("primeIdNumber");
                String groupIdNumber=primeIdNumber+"_"+Integer.toString(item);
                if(!isExist){
                    String insert = "insert into send_ind_aff (item, primeIdNumber, groupIdNumber) values (?,?,?)";
                    PreparedStatement up = c.prepareStatement(insert);
                    up.setInt(1, item);
                    up.setString(2, primeIdNumber);
                    up.setString(3, groupIdNumber);
                    up.execute();
                    up.close();
                }
                

            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void multiEntryGenerator(){ //TODO multiEG
        //set up duplicate entries to mock multiple same tin /tracerid/ etc
        //remember its different for each type of item (A1/A2/ orgp/ orgo)
        Connection c = sendTools.getMySQLConnect();
        String getWorkq = "select * from workq_data order by tin";
        try{
            PreparedStatement p = c.prepareStatement(getWorkq);
            ResultSet rs  = p.executeQuery();
            while(rs.next()){
                int item = rs.getInt("item");
                String tStatus = rs.getString("tStatus");
                String tin = rs.getString("tin");
                String maid = rs.getString("maid");
                String tracerId = rs.getString("tracerId");
                String nType = rs.getString("nType");

                //mark org p types with phase = 7
                String insertWorkq = ""; //Insert copy into workq_data
                
                //TODO Dups
                //if A2 and P
                //copy aff
                //if A1 and P and phase != 7

                // go through org 
                // O type copy
                // P type phase = 7 copy

                //TODO TEST FLOW
                
            }

        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    
    //TODO Add outliers such as Complex w/o processed and w/ processed

    



}