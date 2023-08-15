package com.ceyloncab.authproxymgtservice.domain.boundary;

public interface KafkaProducerService {

    void publishMsgToTopic(String topic, Object msg);
}
