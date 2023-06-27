package com.umulam.fleen.health.configuration.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AwsConfig {

  private final String accessKeyId;
  private final String accessKeySecret;
  private final String regionName;

  public AwsConfig(@Value("${aws.access.key.id}") String accessKeyId,
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

  @Bean
  public AmazonS3 getS3Client() {
    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(getAwsCredentials())
      .withRegion(regionName)
      .build();
  }

  @Bean
  public AmazonSimpleEmailService emailService() {
    return AmazonSimpleEmailServiceClientBuilder
            .standard()
            .withCredentials(getAwsCredentials())
            .withRegion(regionName)
            .build();
  }

  @Bean
  public AmazonSNS getSnsClient() {
    return AmazonSNSClientBuilder
            .standard()
            .withCredentials(getAwsCredentials())
            .withRegion(regionName)
            .build();
  }

  @Bean
  public MailSender mailSender() {
    return new SimpleEmailServiceMailSender(emailService());
  }

  @Bean
  public JavaMailSender javaMailSender() {
    return new SimpleEmailServiceJavaMailSender(emailService());
  }

}
