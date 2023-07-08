package com.umulam.fleen.health.model.request;

import com.umulam.fleen.health.model.json.SmsMessage;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreVerificationOrAuthenticationRequest {

  private String code;
  private String emailAddress;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String emailMessageTitle;
  private String emailMessageBody;
  private SmsMessage smsMessage;
  private String errorMessage;

}
