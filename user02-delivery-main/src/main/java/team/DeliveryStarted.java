package team;

public class DeliveryStarted extends AbstractEvent {

    private Long id;
    private String applyId;
    private String addr;
    private String telelephoneIno;
    private String deliverStatus;
    private String nAme;

    public DeliveryStarted(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTelelephoneIno() {
        return telelephoneIno;
    }

    public void setTelelephoneIno(String telelephoneIno) {
        this.telelephoneIno = telelephoneIno;
    }
    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
    public String getNAme() {
        return nAme;
    }

    public void setNAme(String nAme) {
        this.nAme = nAme;
    }
}
