package sampleSender.complexTypes;

public class ItemData {
    private int itemNbr = 0;
    private String complexity = "";
    private String tStatus = "";
    private String tracerId = "";
    private String tin = "";
    private String maid = "";
    private String processed = "";
    private String nType = "";

    public int getItemNbr() {
        return itemNbr;
    }

    public void setItemNbr(int itemNbr) {
        this.itemNbr = itemNbr;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String gettStatus() {
        return tStatus;
    }

    public void settStatus(String tStatus) {
        this.tStatus = tStatus;
    }

    public String getTracerId() {
        return tracerId;
    }

    public void setTracerId(String tracerId) {
        this.tracerId = tracerId;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getMaid() {
        return maid;
    }

    public void setMaid(String maid) {
        this.maid = maid;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    
}