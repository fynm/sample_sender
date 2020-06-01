package sampleSender.supportTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import sampleSender.complexTypes.IndividualData;
import sampleSender.complexTypes.OrganizationData;
import sampleSender.complexTypes.SendData;

public class fileGenerator {

    private static File f = null;
    private static PrintWriter pw = null;

    /*Ind and Org Counter */
    private static int indCount = 0;
    private static int orgCount = 0;

    public static File getGeneratedFile(String fileName) {
        if (f == null) {
            f = new File(fileName);
        }
        return f;
    }

    public static void setPrintWriter() {
        if (pw == null) {
            try {
                pw = new PrintWriter(getGeneratedFile("Sender_file"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void flushPW() {
        System.out.println("...clearing printwriter...");
        pw.flush();
    }

    public static void closePW() {
        System.out.println("...closing printwriter...");
        pw.close();
    }

    public static void generateHeader() {
        System.out.println("...writing Header...");
        String strDate = sendTools.getDate();
        String strTime = sendTools.getTime();
        pw.printf("%-3.3s", "HDR");
        pw.printf("%-1.1s", ",");
        pw.printf("%-10.10s", strDate);
        pw.printf("%-1.1s", ",");
        pw.printf("%-8.8s", strTime);
        pw.printf("%-1.1s", ",");
        pw.printf("%-10.10s", "Sample Send");
        pw.printf("%-1.1s", ",");
        pw.printf("%-40.40s", "SAMPLE SEND PROGRAM");
        pw.print("*\r\n");
    }

    public static void generateBody(SendData sendData) {
        System.out.println("...Writing Core Data...");
        generateInd(sendData);
        generateOrg(sendData);
    }

    public static void generateInd(SendData sendData) {
        System.out.println("...writing Individual Data...");
        int indSize = sendData.getInd_data().size();
        for (int x = 0; x < indSize; x++) {
            IndividualData ind = sendData.getInd_data().get(x);
            pw.printf("%-30.30s", ind.getPrimeIdNumber());
            pw.printf("%-3.3s", ind.getStglevel());
            pw.printf("%-18.18s", ind.getLastName());
            pw.printf("%-12.12s", ind.getFirstName());
            pw.printf("%-1.1s", ind.getMiddleInit());
            pw.printf("%-30.30s", ind.getStreetAddress());
            pw.printf("%-30.30s", ind.getCity());
            pw.printf("%-2.2s", ind.getState());
            pw.printf("%-5.5s", ind.getZipCode());
            pw.printf("%-30.30s", ind.getCountry());
            pw.printf("%-12.12s", ind.getTeleNumber());
            pw.print("*\r\n");
            updateSent(ind.getItemNbr(), "ind");
            indCount++;
        }
    }

    public static void generateOrg(SendData sendData){
        System.out.println("...writing Organization Data...");
        int orgSize = sendData.getOrg_data().size();
        for(int x=0; x < orgSize; x++){
            OrganizationData org = sendData.getOrg_data().get(x);
            pw.printf("%-30.30s", org.getPrimeIdNumber());
            pw.printf("%-3.3s", org.getStgLevel());
            pw.printf("%-30.30s", org.getEmployerId());
            pw.printf("%-30.30s", org.getStreetAddress());
            pw.printf("%-30.30s", org.getCity());
            pw.printf("%-2.2s", org.getState());
            pw.printf("%-5.5s", org.getZipCode());
            pw.printf("%-30.30s", org.getCountry());
            pw.printf("%-12.12s", org.getTeleNumber());
            pw.print("*\r\n");
            updateSent(org.getItemNbr(), "org");
            orgCount++;
        }
    }

    public static void generateTrailer(){
        System.out.println("...writing Trailer...");
        String endDate = sendTools.getDate();
        String endTime = sendTools.getTime();
        pw.printf("%-15.15s", "Send Results: ");
        pw.printf("%-10.10s", "End Date-");
        pw.printf("%-10.10s", endDate);
        pw.printf("%-1.1s", ",");
        pw.printf("%-10.10s", "End Time-");
        pw.printf("%-8.8s", endTime);
        pw.printf("%-1.1s", ",");
        pw.printf("%-12.12s", "ORG Total: ");
        pw.printf("%09d", orgCount);
        pw.printf("%-1.1s", ",");
        pw.printf("%-12.12s", "IND Total: ");
        pw.printf("%09d", indCount);
        pw.printf("%-1.1s", ",");
        pw.printf("%-12.12s", "Total: ");
        pw.printf("%09d", orgCount + indCount);
        pw.print("*\r\n");
    }

    public static void updateSent(int itemNbr, String table){
        //TODO update to SS = S
    }
}