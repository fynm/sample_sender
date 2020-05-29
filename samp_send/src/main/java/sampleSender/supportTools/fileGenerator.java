package sampleSender.supportTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class fileGenerator {

    private static File f = null;
    private static PrintWriter pw = null;

    public static File getGeneratedFile(String fileName){
        if(f == null){
            File f = new File(fileName);
        }
        return f;
    }

    public static PrintWriter getPrintWriter(){
        if(pw == null){
            try{
                pw = new PrintWriter(getGeneratedFile("Sender_file"));
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }

        return pw;
    }

    public static void flushPW(){
        pw.flush();
    }

    public static void closePW(){
        pw.close();
    }

    public static void generateHeader(){
        pw.printf("%-3.3s", "HDR");
    }

    public static void generateBody(){
        
    }

    public static void generateTrailer(){

    }
}