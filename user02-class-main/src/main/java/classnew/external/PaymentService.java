package classnew.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;

import java.util.Date;


//@FeignClient(name="payment", url="http://localhost:8083") //동기식호출- payment가 죽으면 POST가 불가하다.
//@FeignClient(name="payment", url="http://localhost:8083", configuration=PaymentService.PaymentServiceConfiguration.class, fallback=PaymentServiceFallback.class)
@FeignClient(name="payment", url="http://localhost:8083", configuration=PaymentService.PaymentServiceConfiguration.class, fallback=PaymentServiceFallback.class)
public interface PaymentService {
    @RequestMapping(method= RequestMethod.POST, path="/payments", consumes = "application/json") //payments로 해야 데이터insert
    public boolean payApprove(@RequestBody Payment payment);


    @Component
    class PaymentServiceConfiguration {
        Feign.Builder feignBuilder(){
            SetterFactory setterFactory = (target, method) -> HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(target.name()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(Feign.configKey(target.type(), method)))
                    // 위는 groupKey와 commandKey 설정
                    // 아래는 properties 설정
                    .andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter()
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                            .withMetricsRollingStatisticalWindowInMilliseconds(10000) // 기준시간
                            .withCircuitBreakerSleepWindowInMilliseconds(3000) // 서킷 열려있는 시간
                            .withCircuitBreakerErrorThresholdPercentage(50)) // 에러 비율 기준 퍼센트
                    ; // 최소 호출 횟수
            return HystrixFeign.builder().setterFactory(setterFactory);
        }
    }
}

/*
@FeignClient(name="payment", url="${feign.client.url.paymentUrl}", configuration=PaymentService.PaymentServiceConfiguration.class, fallback=PaymentService.PaymentServiceFallback.class)
public interface PaymentService {
    @RequestMapping(method= RequestMethod.GET, path="/payCellPhone")
    public Long payCellPhone(@RequestParam("orderId") Long orderId,
                             @RequestParam("paymentAmt") Integer paymentAmt,
                             @RequestParam("cellphoneId") Long cellphoneId);

    @Component
    class PaymentServiceFallback implements PaymentService {
        @Override
        public Long payCellPhone(Long orderId,Integer paymentAmt,Long cellphoneId){
            System.out.println("\n###PaymentServiceFallback works####\n");   // fallback 메소드 작동 테스트
            return 0L;
        }
    }

    @Component
    class PaymentServiceConfiguration {
        Feign.Builder feignBuilder(){
            SetterFactory setterFactory = (target, method) -> HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(target.name()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(Feign.configKey(target.type(), method)))
                    // 위는 groupKey와 commandKey 설정
                    // 아래는 properties 설정
                    .andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter()
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                            .withMetricsRollingStatisticalWindowInMilliseconds(10000) // 기준시간
                            .withCircuitBreakerSleepWindowInMilliseconds(3000) // 서킷 열려있는 시간
                            .withCircuitBreakerErrorThresholdPercentage(50)) // 에러 비율 기준 퍼센트
                    ; // 최소 호출 횟수
            return HystrixFeign.builder().setterFactory(setterFactory);
        }
    }
}*/


