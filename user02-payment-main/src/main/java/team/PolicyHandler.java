package team;

import team.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class PolicyHandler {
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListner(@Payload String eventString) {

    }

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverClassCanceled_PayCancel(@Payload ClassCanceled classCanceled) {

        if (classCanceled.isMe()) {
            System.out.println("##### listener PaymentCancellation : " + classCanceled.toJson() + "\n\n");

            List<Payment> paymentList = paymentRepository.findByApplyId(String.valueOf(classCanceled.getId()));

            for (Payment payment : paymentList) {
                payment.setPayStaus("PaymentCancelled");
                paymentRepository.save(payment);
            }
        }
        return;
    }

    // Sample Logic //
    // Payment payment = new Payment();
    // paymentRepository.save(payment);

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
    }

}