package com.umulam.fleen.health.adapter;

/**
 * <p>The ExternalSystemType is a composition of different types of external service type for which data is obtained from the
 * various APIs</p>
 * <p>
 * <br/>
 * <p>The external system type can be categorized as the CICDType, CodeAnalyzerType, FuzzerType, GitType and also the
 * WorkflowType.</p>
 *
 * @author Alperen Oezkan
 * @version 1.0
 */
public interface ExternalSystemType {

  String getValue();

}
