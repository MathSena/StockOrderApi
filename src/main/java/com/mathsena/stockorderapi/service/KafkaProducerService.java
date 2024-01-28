package com.mathsena.stockorderapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

  public static final String TOPIC_ORDER = "order-topic";
  public static final String TOPIC_ITEM = "item-topic";
  public static final String TOPIC_STOCK = "stock-topic";

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessageOrder(String message) {
    log.info("Sending message to topic: {}", TOPIC_ORDER);
    this.kafkaTemplate.send(TOPIC_ORDER, message);
  }

  public void sendMessageStock(String message) {
    log.info("Sending message to topic: {}", TOPIC_STOCK);
    this.kafkaTemplate.send(TOPIC_STOCK, message);
  }

  public void sendMessageItem(String message) {
    log.info("Sending message to topic: {}", TOPIC_ITEM);
    this.kafkaTemplate.send(TOPIC_ITEM, message);
  }
}
