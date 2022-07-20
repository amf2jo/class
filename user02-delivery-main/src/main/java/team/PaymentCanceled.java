package team;

public class PaymentCanceled extends AbstractEvent {

    private Long id;
    private String applyId;
    private String payStaus;

    public PaymentCanceled(){
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
    public String getPayStaus() {
        return payStaus;
    }

    public void setPayStaus(String payStaus) {
        this.payStaus = payStaus;
    }
}
