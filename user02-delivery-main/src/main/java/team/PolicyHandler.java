package team;

import team.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyHandler{
    @Autowired DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentAppoved_DeliveryStart(@Payload PaymentAppoved paymentAppoved){

        if(!paymentAppoved.validate()) return;

        System.out.println("\n\n##### listener DeliveryStart : " + paymentAppoved.toJson() + "\n\n");



        // Sample Logic //
        Delivery delivery = new Delivery();

        delivery.setAddr(paymentAppoved.getAddr());
        delivery.setApplyId(paymentAppoved.getApplyId());
        delivery.setName(paymentAppoved.getStudentName());
        delivery.setTelelephoneInfo(paymentAppoved.getTelephoneInfo());
        delivery.setDeliveryStatus("DeliveryStart");

        deliveryRepository.save(delivery);
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCanceled_DeliveryCancel(@Payload PaymentCanceled paymentCanceled){

        System.out.println("\n\n##### listener DeliveryCancel : " + paymentCanceled.toJson() + "\n\n");

        
        if(paymentCanceled.isMe()){
            List<Delivery> deliveryList = deliveryRepository.findByApplyId(paymentCanceled.getApplyId());
            
            if ((deliveryList != null) && !deliveryList.isEmpty()){
                deliveryRepository.deleteAll(deliveryList);
            }
        }
        // Sample Logic //
        // Delivery delivery = new Delivery();
        // deliveryRepository.save(delivery);
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
