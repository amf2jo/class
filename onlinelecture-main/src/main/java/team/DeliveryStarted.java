package team;

public class DeliveryStarted extends AbstractEvent {

    private Long id;
    private String applyId;
    private String addr;
    private String telelephoneInfo;
    private String deliverStatus;
    private String name;

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
        return telelephoneInfo;
    }

    public void setTelelephoneIno(String telelephoneInfo) {
        this.telelephoneInfo = telelephoneInfo;
    }
    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
    public String getNAme() {
        return name;
    }

    public void setNAme(String name) {
        this.name = name;
    }
}