package com.ceyloncab.authproxymgtservice.external.serviceimpl;

import com.ceyloncab.authproxymgtservice.domain.boundary.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 */
@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void publishMsgToTopic(String topic, Object msg){
        try {
            kafkaTemplate.send(topic,msg).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error("Error occurred in publish message to kafkaTopic:{}|Message:{}|Error:{}",topic,msg,ex.getMessage(),ex);
                }

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    log.info("successfully published");
                }
            });
        }catch (Exception ex){
            //handle errors before publish to kafka topic(network)
            log.error("Error occurred in publish message to kafkaTopic:{}",topic,ex);
        }
    }
}
