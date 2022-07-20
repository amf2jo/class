package classnew;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

import classnew.external.Payment;
import classnew.external.PaymentService;

@Entity
@Table(name="Class_table")
public class Class {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String studentName;
    private String addr;
    private String telephoneInfo;
    private String payMethod;
    private String payAccount;
    private String applyStatus;
    private Long courseId;

 /*   @PostPersist
    public void onPostPersist(){
        ClassRegisted classRegisted = new ClassRegisted();
        BeanUtils.copyProperties(this, classRegisted);
        classRegisted.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        classnew.external.Payment payment = new classnew.external.Payment();
        // mappings goes here
        Application.applicat3ionContext.getBean(classnew.external.PaymentService.class)
            .payApprove(payment);

    }
    */
    @PostPersist
    public void onPostPersist() throws Exception {
     // Following code causes dependency to external APIs
     //it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        ClassRegisted classRegisted = new ClassRegisted();

        classnew.external.Payment payment = new classnew.external.Payment();
        payment.setApplyId(String.valueOf(this.getId()));
        payment.setPayMethod(this.getPayMethod());
        payment.setPayAccount(this.getPayAccount());
        payment.setPayStaus("PAYMENT_COMPLETED");
        payment.setAddr(this.getAddr());
        payment.setTelephoneInfo(this.getTelephoneInfo());
        payment.setStudentName(this.getStudentName());
    
        ClassApplication.applicationContext.getBean(classnew.external.PaymentService.class).payApprove(payment);

        this.applyStatus="CLASS_COMPLETED";
        classRegisted.setClassId(this.getId());
        classRegisted.setStudentName(this.getStudentName());
        classRegisted.setAddr(this.getAddr());
        classRegisted.setTelephoneInfo(this.getTelephoneInfo());
        classRegisted.setPayMethod(this.getPayMethod());
        classRegisted.setPayAccount(this.getPayAccount());
        classRegisted.setApplyStatus(this.getApplyStatus());
        classRegisted.setId(this.getId());
        classRegisted.setCourseId(this.getCourseId());
        
     // this.setApplyStatus("CLASS_APPLIED_SUCCESS");
     // this.applyStatus="CLASS_APPLIED_SUCCESS";
        System.out.println("\n\n##### listener classRegisted : " + classRegisted.toJson() + "\n\n");

        BeanUtils.copyProperties(this, classRegisted);
        classRegisted.publishAfterCommit();

 }

    @PostUpdate
    public void onPostUpdate(){
        //this.setPaymentId(this.getOrderId());
        this.setApplyStatus("CLASS_CANCELED"); // path=classes의 applyStatus상태 업데이트

        ClassCanceled classCanceled = new ClassCanceled();

        classCanceled.setClassId(this.getId());
        classCanceled.setApplyStatus(this.getApplyStatus());

        BeanUtils.copyProperties(this, classCanceled);
        classCanceled.publishAfterCommit();


    }

    @PrePersist
    public void onPrePersist(){

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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}