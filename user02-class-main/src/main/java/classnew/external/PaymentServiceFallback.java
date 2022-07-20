package classnew.external;

import org.springframework.stereotype.Component;

@Component
public class PaymentServiceFallback implements PaymentService {
    @Override
    //public Long payApprove(Long classId){
    public boolean payApprove(Payment payment) {

        //do nothing if you want to forgive it

        System.out.println("\\n=========FALL BACK STARTING=========\\n"); //fallback 메소드 작동 테스트
       //return 0L;
        return false;
    }
}