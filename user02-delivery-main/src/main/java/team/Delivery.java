package team;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String applyId;
    private String addr;
    private String telelephoneInfo;
    private String deliveryStatus;
    private String name;

    @PostPersist
    public void onPostPersist(){
        DeliveryStarted deliveryStarted = new DeliveryStarted();
        BeanUtils.copyProperties(this, deliveryStarted);
        deliveryStarted.publishAfterCommit();

    }
    
    @PostRemove
    public void onPostRemove(){
        DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        BeanUtils.copyProperties(this, deliveryCanceled);
        deliveryCanceled.publishAfterCommit();

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
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTelelephoneInfo() {
        return telelephoneInfo;
    }

    public void setTelelephoneInfo(String telelephoneInfo) {
        this.telelephoneInfo = telelephoneInfo;
    }
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}