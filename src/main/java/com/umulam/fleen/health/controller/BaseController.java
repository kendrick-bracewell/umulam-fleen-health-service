package com.umulam.fleen.health.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class BaseController {

  protected abstract String getCreateViewTitle();

  protected abstract String getUpdateViewTitle();

  protected abstract String getEntriesPath();

  protected abstract String getBasePath();

  protected String getFormProcessedMessageKey() {
    return "formProcessingStatus";
  }

  protected void addFormProcessedAttribute(RedirectAttributes redirectAttributes, String message) {
    redirectAttributes.addFlashAttribute(getFormProcessedMessageKey(), message);
  }

  protected void addFormProcessedAttribute(RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute(getFormProcessedMessageKey(), "Success");
  }

}
