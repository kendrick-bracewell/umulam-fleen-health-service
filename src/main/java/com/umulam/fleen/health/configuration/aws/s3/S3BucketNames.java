package com.umulam.fleen.health.configuration.aws.s3;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.s3.bucket.name")
public class S3BucketNames {

  private String profilePhoto;
  private String memberDocument;
}
