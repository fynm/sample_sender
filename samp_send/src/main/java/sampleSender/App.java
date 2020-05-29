package sampleSender;

import sampleSender.supportTools.FileFTP;
import sampleSender.supportTools.fileGenerator;
import sampleSender.supportTools.sendTools;
import sampleSender.businessRules.SendRules;
import sampleSender.businessRules.DataCleanUp;
import sampleSender.businessRules.DataFormatter;
import sampleSender.businessRules.DataLoader;
import sampleSender.businessRules.FormattingRules;
import sampleSender.complexTypes.SendData;

public final class App {
    private App() {
    }
    public static void main(String[] args) {

        /*Initial Data Format Logic*/
        DataFormatter.runDataFormatter();

        /*Generate Header in File */
        fileGenerator.generateHeader();

        /*Initialize Data Object */
        SendData sendData = new SendData();

        /*Main Business Rules */
        SendRules.runSendRules();

        /*Data Load into Data Object and Format Data*/
        DataLoader.runDataLoader(sendData);
        FormattingRules.runFormattingRules(sendData);

        /*Write Data to file */
        fileGenerator.generateBody();
        fileGenerator.generateTrailer();
        fileGenerator.flushPW();

        /*FTP File to Destination*/
        FileFTP.ftpSend();

        /*Data Cleanup / Oneshot Data changes */
        DataCleanUp.runDataCleanup();


        /*Close PrintWriter */
        fileGenerator.closePW();


    }
}
