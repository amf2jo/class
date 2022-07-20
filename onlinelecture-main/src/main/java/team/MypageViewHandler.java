package team;

import team.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {


    private static final String Interger = null;
    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenClassRegisted_then_CREATE_1 (@Payload ClassRegisted classRegisted) {
        try {

            System.out.println("\n\n##### listener classRegisted : " + classRegisted.toJson() + "\n\n");

            if (!classRegisted.validate()) return;

            // view 객체 생성
            Mypage mypage = new Mypage();
            // view 객체에 이벤트의 Value 를 set 함
            mypage.setApplyId(String.valueOf(classRegisted.getId()));
            mypage.setCourseId(String.valueOf(classRegisted.getCourseId()));
            mypage.setApplyStaus("ApplyFinish");
            mypage.setPayMethod(classRegisted.getPayMethod());
            mypage.setPayAccount(classRegisted.getPayAccount());
            mypage.setStudentName(classRegisted.getStudentName());
            mypage.setAddr(classRegisted.getAddr());
            mypage.setTelephoneInfo(classRegisted.getTelephoneInfo());
            mypage.setPayStatus("PayFinish");
            //mypage.setDeliveryStatus("D");
            // view 레파지 토리에 save
            mypageRepository.save(mypage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentAppoved_then_UPDATE_1(@Payload PaymentAppoved paymentAppoved) {
        try {
            if (!paymentAppoved.validate()) return;
                // view 객체 조회
                System.out.println("\n\n##### listener paymentAppoved : " + paymentAppoved.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(paymentAppoved.getApplyId());
                for(Mypage mypage : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setPayStatus("PayFinish");
                    mypage.setApplyStaus("ApplyFinish");
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_2(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
                System.out.println("\n\n##### listener deliveryStarted : " + deliveryStarted.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(deliveryStarted.getApplyId());
                for(Mypage mypage : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함

                    mypage.setDeliveryStatus("DeliveryFinish");
                // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenClassCanceled_then_UPDATE_3(@Payload ClassCanceled classCanceled) {
        try {
            if (!classCanceled.validate()) return;
                // view 객체 조회
            System.out.println("\n\n##### listener classCanceled : " + classCanceled.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(String.valueOf(classCanceled.getId()));
                for(Mypage mypage : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setApplyStaus("CancelRequest");
                // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCanceled_then_UPDATE_4(@Payload PaymentCanceled paymentCanceled) {
        try {
            if (!paymentCanceled.validate()) return;
                // view 객체 조회
                System.out.println("\n\n##### listener PaymentCanceled : " + paymentCanceled.toJson() + "\n\n");

                List<Mypage> mypageList = mypageRepository.findByApplyId(paymentCanceled.getApplyId());
                for(Mypage mypage : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setApplyStaus("ApplyCanceled");
                    mypage.setPayStatus("PayCanceled");
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_5(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (!deliveryCanceled.validate()) return;
                // view 객체 조회
            System.out.println("\n\n##### listener deliveryCanceled : " + deliveryCanceled.toJson() + "\n\n");

            List<Mypage> mypageList = mypageRepository.findByApplyId(deliveryCanceled.getApplyId());

            for(Mypage mypage : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setDeliveryStatus("DeliveryCanceled");
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

