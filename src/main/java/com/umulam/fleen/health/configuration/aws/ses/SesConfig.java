package com.umulam.fleen.health.configuration.aws.ses;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.properties")
public class SesConfig {
  private final String accessKeyId;
  private final String accessKeySecret;
  private final String regionName;

  public SesConfig(@Value("${aws.access.key.id}") String accessKeyId,
                   @Value("${aws.access.key.secret}") String accessKeySecret,
                   @Value("${aws.s3.region.name}") String regionName) {
    this.accessKeyId = accessKeyId;
    this.accessKeySecret = accessKeySecret;
    this.regionName = regionName;
  }

  private AWSCredentialsProvider getAwsCredentials() {
    final BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
    return new AWSStaticCredentialsProvider(basicAwsCredentials);
  }

}
