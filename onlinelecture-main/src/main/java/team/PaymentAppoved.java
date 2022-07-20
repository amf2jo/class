package team;

public class PaymentAppoved extends AbstractEvent {

    private Long id;
    private String applyId;
    private String payMethod;
    private String payInfo;
    private String payStaus;
    private String addr;
    private String telephoneInfo;
    private String studentName;

    public PaymentAppoved() {
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getPayStaus() {
        return this.payStaus;
    }

    public void setPayStaus(String payStaus) {
        this.payStaus = payStaus;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
