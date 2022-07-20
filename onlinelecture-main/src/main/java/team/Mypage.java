package team;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Mypage_table")
public class Mypage {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String courseId;
        private String applyId;
        private String studentName;
        private String payStatus;
        private String payMethod;
        private String payAccount;
        private String deliveryStatus;
        private String addr;
        private String telephoneInfo;
        private String applyStaus;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        public String getCourseId() {
            return courseId ;
        }

        public void setCourseId(String courseId ) {
            this.courseId  = courseId ;
        }
        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }
        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
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
        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
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
        public String getApplyStaus() {
            return applyStaus;
        }

        public void setApplyStaus(String applyStaus) {
            this.applyStaus = applyStaus;
        }

}
