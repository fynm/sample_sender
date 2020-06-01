package sampleSender.businessRules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



import sampleSender.supportTools.sendTools;

public class DataFormatter {

    public static void runDataFormatter(){
        System.out.println("...running Data Formatter...");
        formatSTGLevels();          //Ptypes only
        formatSTGLevels_PType();    //Org Ptypes
    }

    /*Logic Note
        -Organizations can be O-ntypes or P-nTypes
        -Individuals can only be P-ntypes
    */

    public static String queryBuilder(String type){
        String query = "";
        switch(type){
            case "ind":
                query = "select * from send_ind i left join workq_data w on i.item = w.item where sendStatus is null ";
                query = query + "order by i.stgLevelId, i.item ";
                break;
            case "org":
                query = "select * from send_org o left join workq_data w on o.item = w.item where nType = 'O' and sendStatus is null ";
                query = query + "order by o.stgLevelId, o.item";
                break;
            default:
                break;
        }

        return query;
    }

    public static void formatOrganization(){
        //setup Levels for Orgs
        Connection c = sendTools.getMySQLConnect();
        String orgQuery = queryBuilder("org");
        PreparedStatement pOrg = null; 
        ResultSet rsOrg = null;
        try{
            pOrg = c.prepareStatement(orgQuery);
            rsOrg = pOrg.executeQuery();
            String currentStgLevelId = "";
            while(rsOrg.next()){
                if(!currentStgLevelId.equals(rsOrg.getString("stgLevelId"))){
                    boolean hasSent = false;
                    String sentCheck = "select * from send_org where sendStatus = 'S' and stgLevelId = ?";
                    PreparedStatement pSentCheck = c.prepareStatement(sentCheck);
                    pSentCheck.setString(1, rsOrg.getString("stgLevelId"));
                    ResultSet rsSentCheck = pSentCheck.executeQuery();
                    while(rsSentCheck.next()){
                        if(("S").equals(rsSentCheck.getString("sendStatus"))){
                            hasSent = true;
                        }
                    }
                    if(hasSent){
                        //set stgLevel to 2
                        String stgLevelUpdate = "update send_org set stgLevel = '2' where stgLevelId = ? and sendStatus != 'S'";
                        PreparedStatement slUpdate = c.prepareStatement(stgLevelUpdate);
                        slUpdate.setString(1, rsOrg.getString("stgLevelId"));
                        slUpdate.execute();
                        slUpdate.close();
                    }else{
                        //set stgLevel to 1
                        String stgLevelUpdate = "update send_org set stgLevel = '1' where stgLevelId = ?";
                        PreparedStatement slUpdate = c.prepareStatement(stgLevelUpdate);
                        slUpdate.setString(1, rsOrg.getString("stgLevelId"));
                        slUpdate.execute();
                        slUpdate.close();
                    }
                }
                currentStgLevelId = rsOrg.getString("stgLevelId");
            }

            pOrg.close();
            rsOrg.close();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void formatIndividual(){
        //set up Levels for Ind Otypes
        Connection c = sendTools.getMySQLConnect();
        String indQuery = queryBuilder("ind");
        PreparedStatement pInd = null;
        ResultSet rsInd = null;
        try{
            pInd = c.prepareStatement(indQuery);
            rsInd = pInd.executeQuery();
            String currentStgLevelId = "";
            while(rsInd.next()){
                if(!currentStgLevelId.equals(rsInd.getString("stgLevelId"))){
                    boolean hasSent = false;
                    String sentCheck = "select * from send_ind where sendStatus = 'S' and stgLevelId = ?";
                    PreparedStatement pSentCheck = c.prepareStatement(sentCheck);
                    pSentCheck.setString(1, rsInd.getString("stgLevelId"));
                    ResultSet rsSentCheck = pSentCheck.executeQuery();
                    while(rsSentCheck.next()){
                        if(("S").equals(rsSentCheck.getString("sendStatus"))){
                            hasSent = true;
                        }
                    }
                    if(hasSent){
                        String stgLevelUpdate = "update send_ind set stgLevel = '2' where stgLevelId = ? and sendStatus = 'S'";
                        PreparedStatement slUpdate = c.prepareStatement(stgLevelUpdate);
                        slUpdate.setString(1, rsInd.getString("stgLevelId"));
                        slUpdate.execute();
                        slUpdate.close();
                    }else{
                        //set stgLevel to 1
                        String stgLevelUpdate = "update send_ind set stgLevel = '1' where stgLevelId = ?";
                        PreparedStatement slUpdate = c.prepareStatement(stgLevelUpdate);
                        slUpdate.setString(1, rsInd.getString("stgLevelId"));
                        slUpdate.execute();
                        slUpdate.close();
                    }
                }
                currentStgLevelId = rsInd.getString("stgLevelId");
            }

            pInd.close();
            rsInd.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void formatSTGLevels_PType(){
        //set up for Levels for Org pTypes
        //will always be level 2
        Connection c = sendTools.getMySQLConnect();
        try{
            String stgLevelUpdate = "update send_org i left join workq_data w on i.item = w.item set stgLevel = '2' where nType = 'P'";
            PreparedStatement slUpdate = c.prepareStatement(stgLevelUpdate);
            slUpdate.execute();
            slUpdate.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void formatSTGLevels(){
        ExecutorService pool = getPool();
            pool.submit(new MyThread("O"));
            pool.submit(new MyThread("P"));
            pool.shutdown();
    }

    static int corePoolSize = 2;
    static int maximumPoolSize = 2;
    static long keepAliveTime = 0L;
    static TimeUnit unit = TimeUnit.MICROSECONDS;

    public static ThreadPoolExecutor getPool() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, 
            new LinkedBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void shutdownThreadPool(ExecutorService pool){
        try{
            pool.shutdown();
            pool.awaitTermination(3600, TimeUnit.SECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static class MyThread implements Callable<Boolean> {
        String type;
        Connection c;

        public MyThread(String type){
            this.type =type;
        }

        @Override
        public Boolean call(){
            try{
                if(this.type=="O"){
                    formatOrganization();
                }else{
                    formatIndividual();
                }
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }


}