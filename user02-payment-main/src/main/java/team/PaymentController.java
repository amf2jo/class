package team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

 @RestController
 public class PaymentController {


  //System.out.println("\n\n#############Controller시작  result : " + result + "\n\n");
  PaymentRepository paymentRepository;

  @PostMapping(value = "/payApprove")
  public boolean payApprove(@RequestBody Map<String, String> param) {

   Payment payment = new Payment();
   boolean result = false;

   //payment.setClassId(Long.parseLong(param.get("classId")));
   //payment.setTextBook(param.get("textBook"));

   payment.setApplyId(param.get("id"));
   payment.setPayMethod(param.get("payMethod"));
   payment.setPayAccount(param.get("payAccount"));
   payment.setPayStaus("PAYMENT_COMPLETED");
   payment.setAddr(param.get("addr"));
   payment.setTelephoneInfo(param.get("telephoneInfo"));
   payment.setStudentName(param.get("studentName"));

  /* try {
    paymentRepository.save(payment);
    result = true;
   } catch (Exception e) {
    e.printStackTrace();
   }*/
   paymentRepository.save(payment);
   result = true;

   System.out.println("\n\n#############Controller안에서  result : " + result + "\n\n");
   return result;
  }

 }