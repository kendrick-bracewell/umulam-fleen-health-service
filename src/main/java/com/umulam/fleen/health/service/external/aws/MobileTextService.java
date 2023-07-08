package com.umulam.fleen.health.service.external.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.constant.VerificationMessageType;
import com.umulam.fleen.health.model.json.SmsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.umulam.fleen.health.util.FleenHealthUtil.readResourceFile;

@Slf4j
@Component
public class MobileTextService {

  private final AmazonSNS service;
  private final ObjectMapper objectMapper;
  private static final String AWS_SNS_SMS_TYPE = "AWS.SNS.SMS.SMSType";
  private static final String AWS_SNS_SMS_SENDER_ID = "AWS.SNS.SMS.SenderID";
  private static final String AWS_SNS_SMS_TYPE_VALUE = "Transactional";
  private static final String AWS_SNS_DATA_TYPE = "String";
  private static final String AWS_SNS_SENDER_ID_VALUE = "FleenHealth";
  private static final String SMS_MESSAGE_TEMPLATE_FILE_PATH = "templates/sms/messages.json";

  public MobileTextService(AmazonSNS service,
                           ObjectMapper objectMapper) {
    this.service = service;
    this.objectMapper = objectMapper;
  }

  public void sendSms(String phoneNumber, String message) {
    try {
      int timeoutPeriod = 3000;
      Map<String, MessageAttributeValue> smsAttributes =
              new HashMap<>();

      smsAttributes.put(AWS_SNS_SMS_TYPE, new MessageAttributeValue()
              .withStringValue(AWS_SNS_SMS_TYPE_VALUE)
              .withDataType(AWS_SNS_DATA_TYPE));

      smsAttributes.put(AWS_SNS_SMS_SENDER_ID, new MessageAttributeValue()
              .withStringValue(AWS_SNS_SENDER_ID_VALUE)
              .withDataType(AWS_SNS_DATA_TYPE));

      service.publish(new PublishRequest()
              .withMessage(message)
              .withPhoneNumber(phoneNumber)
              .withMessageAttributes(smsAttributes)
              .withSdkRequestTimeout(timeoutPeriod));
    } catch (RuntimeException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  public void subscribe(String topicArn, String protocol, String endpoint) {
    SubscribeRequest subscribe = new SubscribeRequest(topicArn, protocol, endpoint);
    SubscribeResult subscribeResult = service.subscribe(subscribe);
    System.out.println("Subscribe request: " +
            service.getCachedResponseMetadata(subscribe));
    System.out.println("Subscribe result: " + subscribeResult);
  }

  public String createTopic(String topicName) {
    CreateTopicResult result = null;
    try {
      CreateTopicRequest request = new CreateTopicRequest();
      request.setName(topicName);

      result = service.createTopic(request);
      return result.getTopicArn();

    } catch (AmazonSNSException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    return "";
  }

  public String sendSMSMessageToTopic(String topicArn, String message) {
    PublishResult result = service.publish(new PublishRequest()
            .withTopicArn(topicArn)
            .withMessage(message));
    return result.getMessageId();
  }

  public List<SmsMessage> getSmsMessages() {
    String value = readResourceFile(SMS_MESSAGE_TEMPLATE_FILE_PATH);
    try {
      return objectMapper.readValue(value, new TypeReference<>() {});
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage(), ex);
    }
    return Collections.emptyList();
  }

  public Optional<SmsMessage> getVerificationSmsMessage(VerificationMessageType verificationMessageType) {
    return getSmsMessages()
            .stream()
            .filter(message -> message.getTitle().equals(verificationMessageType))
            .findFirst();
  }

}
