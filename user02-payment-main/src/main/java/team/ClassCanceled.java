package team;

public class ClassCanceled extends AbstractEvent {

    private Long id;
    private String applyStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPayStatus() {
        return applyStatus;
    }

    public void setPayStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
}