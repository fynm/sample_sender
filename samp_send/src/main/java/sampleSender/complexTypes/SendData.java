package sampleSender.complexTypes;

import java.util.ArrayList;

public class SendData {
    
    private ArrayList<IndividualData> ind_data = new ArrayList<IndividualData>();
    private ArrayList<OrganizationData> org_data = new ArrayList<OrganizationData>();

    /*Constructor */
    public SendData(){
    }

    /*Copy Constructor */
    public SendData(ArrayList<IndividualData> ogInd, ArrayList<OrganizationData> ogOrg) {
        this.ind_data = ogInd;
        this.org_data = ogOrg;
    }

    public ArrayList<IndividualData> getInd_data() {
        return ind_data;
    }

    public void setInd_data(ArrayList<IndividualData> ind_data) {
        this.ind_data = ind_data;
    }

    public ArrayList<OrganizationData> getOrg_data() {
        return org_data;
    }

    public void setOrg_data(ArrayList<OrganizationData> org_data) {
        this.org_data = org_data;
    }

    
}