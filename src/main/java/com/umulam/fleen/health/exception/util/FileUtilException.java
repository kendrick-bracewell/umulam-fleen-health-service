package com.umulam.fleen.health.exception.util;

import com.umulam.fleen.health.exception.base.FleenHealthException;

/**
 * <p>The FileUtilException class is used to define the exception response message for issues related to File utility processes.
 * </p>
 *
 * @author Alperen Oezkan
 * @version 1.0
 */
public class FileUtilException extends FleenHealthException {

  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_MESSAGE = "An exception occurred in a file util method!";

  public FileUtilException(String message) {
    super(DEFAULT_MESSAGE + ": " + message);
  }
}

