package sampleSender.supportTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MockDataGenerator {
    public static void runDataUpdate(){
        Connection c = sendTools.getMySQLConnect();
        try{
            String sql = "select * from workq_data";
            PreparedStatement p = c.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                if(rs.getString("nType").equals("P") && rs.getString("tStatus").equals("A1")){
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
                    String insertIndA2 = "insert into send_ind (item) values (?)";
                    PreparedStatement pA2 = c.prepareStatement(insertIndA2);
                    pA2.setInt(1, item);
                    pA2.execute();
                    pA2.close();
                }else if(rs.getString("nType").equals("O")){
                    //Org O
                    int item = rs.getInt("item");
                    String insertORGO = "insert into send_org (item) values (?)";
                    PreparedStatement pO = c.prepareStatement(insertORGO);
                    pO.setInt(1, item);
                    pO.execute();
                    pO.close();
                }else if(rs.getString("nType").equals("P") && rs.getString("phase").equals("7")){
                    //Org P
                    int item = rs.getInt("item");
                    String insertORGP = "insert into send_org (item) values (?)";
                    PreparedStatement pP = c.prepareStatement(insertORGP);
                    pP.setInt(1, item);
                    pP.execute();
                    pP.close();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}