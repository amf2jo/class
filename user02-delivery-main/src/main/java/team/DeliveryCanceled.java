package team;

public class DeliveryCanceled extends AbstractEvent {

    private String applyId;

    public DeliveryCanceled(){
        super();
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
}
