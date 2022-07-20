package team;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String applyId;
    private String payMethod;
    private String payAccount;
    private String payStaus;
    private String addr;
    private String telephoneInfo;
    private String studentName;

    @PostPersist
    public void onPostPersist() {
        /*
        if (this.getPayStaus().equals("PayApprove")) {

            PaymentAppoved paymentAppoved = new PaymentAppoved();
            //System.out.println(paymentAppoved.getPayStaus());
            BeanUtils.copyProperties(this, paymentAppoved);
            //System.out.println(paymentAppoved.getPayStaus());
            paymentAppoved.setPayStaus("PaymentAprroved");
            System.out.println(paymentAppoved.getPayStaus());
            paymentAppoved.publishAfterCommit();
            System.out.println(paymentAppoved.getPayStaus());
        }
        */
        PaymentAppoved paymentAppoved = new PaymentAppoved();
        BeanUtils.copyProperties(this, paymentAppoved);
        paymentAppoved.setPayStaus("PaymentAprroved");
        paymentAppoved.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        //if (this.getPayStaus().equals("PayCancel")) {
            PaymentCanceled paymentCanceled = new PaymentCanceled();
            BeanUtils.copyProperties(this, paymentCanceled);
            // paymentCanceled.setPayStaus("PaymentCancelled");
            paymentCanceled.publishAfterCommit();
        //}
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

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayStaus() {
        return payStaus;
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