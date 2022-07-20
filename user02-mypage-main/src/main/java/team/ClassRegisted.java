package team;

public class ClassRegisted extends AbstractEvent {

    private Long id;
    private String studentName;
    private String addr;
    private String telephoneInfo;
    private String payMethod;
    private String payAccount;
    private String applyStatus;
    private Long classId;
    private Long courseId;

    public ClassRegisted(){
        super();
        System.out.println("------Register Class Start!!----");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTelephoneInfo() {
        return telephoneInfo;
    }

    public void setTelephoneInfo(String telephoneInfo) {
        this.telephoneInfo = telephoneInfo;
    }
    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
