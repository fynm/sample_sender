package sampleSender.businessRules;

import java.sql.Connection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataFormatter {
    public static void runDataFormatter(){
        formatSTGLevels();
    }

    public static void formatOrganization(){
        //setup Levels for Orgs
    }

    public static void formatIndividual(){
        //set up Levels for Ind
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