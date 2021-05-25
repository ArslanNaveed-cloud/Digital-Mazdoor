package aust.fyp.pk.project.application.digitalmazdoor;

public class RequestDataModel {
    String pid ;
    String ptitle;
    String pprice ;
    String pdescription;
    String pcategory ;
    String bpic;
    int pbids ;
    String pstatus;

    String pdate;
    String bcity;
    String pdays ;

    public String getPid() {
        return pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public String getPprice() {
        return pprice;
    }

    public String getPdescription() {
        return pdescription;
    }

    public String getPcategory() {
        return pcategory;
    }

    public String getBpic() {
        return bpic;
    }

    public int getPbids() {
        return pbids;
    }

    public String getPstatus() {
        return pstatus;
    }

    public String getPdate() {
        return pdate;
    }

    public String getBcity() {
        return bcity;
    }

    public String getPdays() {
        return pdays;
    }

    public String getBid() {
        return bid;
    }

    public String getBname() {
        return bname;
    }

    public RequestDataModel(String pid, String ptitle, String pprice, String pdescription, String pcategory, String bpic, int pbids, String pstatus, String pdate, String bcity, String pdays, String bid, String bname) {
        this.pid = pid;
        this.ptitle = ptitle;
        this.pprice = pprice;
        this.pdescription = pdescription;
        this.pcategory = pcategory;
        this.bpic = bpic;
        this.pbids = pbids;
        this.pstatus = pstatus;
        this.pdate = pdate;
        this.bcity = bcity;
        this.pdays = pdays;
        this.bid = bid;
        this.bname = bname;
    }

    String bid ;
    String bname;
}
