package takbaejm;

import takbaejm.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReqCanceled_PaymentCancelPol(@Payload ReqCanceled reqCanceled){

        if(reqCanceled.isMe()){
            Iterator<Payment> iterator = paymentRepository.findAll().iterator();
            while(iterator.hasNext()){
                Payment paymenttmp = iterator.next();
                if(paymenttmp.getRequestId() == reqCanceled.getId()){
                    Optional<Payment> PaymentOptional = paymentRepository.findById(paymenttmp.getId());
                    Payment payment = PaymentOptional.get();
                    payment.setStatus(reqCanceled.getStatus());
                    paymentRepository.save(payment);
                }
            }


            System.out.println("##### listener PaymentCancelPol : " + reqCanceled.toJson());
        }
    }

}
