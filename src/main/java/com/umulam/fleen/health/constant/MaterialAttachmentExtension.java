package com.umulam.fleen.health.constant;


public enum MaterialAttachmentExtension {

  PDF;

  public static boolean contains(String extension)
  {
    for(MaterialAttachmentExtension attachmentExtension: MaterialAttachmentExtension.values())
      if (attachmentExtension.name().equalsIgnoreCase(extension)) {
        return true;
      }
    return false;
  }

}
