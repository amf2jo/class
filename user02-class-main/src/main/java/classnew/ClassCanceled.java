package classnew;

public class ClassCanceled extends AbstractEvent {

    private Long id;
    private Long classId;
    private String applyStatus;

    public ClassCanceled(){
        super();
        System.out.println("------Cancel Class Start!!----");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
}
