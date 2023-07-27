package com.umulam.fleen.health.adapter.paystack;

import com.umulam.fleen.health.adapter.base.BaseAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaystackAdapter extends BaseAdapter {

  protected PaystackAdapter(@Value("${paystack.base-url}") String baseUrl,
                             @Value("${paystack.secret-key}") String secretKey) {
    super(baseUrl);
  }
}
