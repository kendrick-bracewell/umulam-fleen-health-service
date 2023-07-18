package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.ProfessionalService;
import com.umulam.fleen.health.validator.ProfessionalValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static com.umulam.fleen.health.constant.base.ProfileType.PROFESSIONAL;
import static com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus.AVAILABLE;
import static com.umulam.fleen.health.constant.verification.ProfileVerificationStatus.APPROVED;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class ProfessionalValidValidator implements ConstraintValidator<ProfessionalValid, String> {

  private final MemberService service;
  private final ProfessionalService professionalService;

  public ProfessionalValidValidator(MemberService service,
                                    ProfessionalService professionalService) {
    this.service = service;
    this.professionalService = professionalService;
  }

  @Override
  public void initialize(ProfessionalValid constraintAnnotation) {}

  @Override
  public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
    try {
      Member member = service.getMemberById(Integer.parseInt(id));
      if (nonNull(member) && member.getUserType() == PROFESSIONAL && member.getVerificationStatus() == APPROVED) {
        Optional<Professional> professionalExists = professionalService.findProfessionalByMember(member);
        if (professionalExists.isPresent()) {
          Professional professional = professionalExists.get();
          if (professional.getAvailabilityStatus() == AVAILABLE) {
            return true;
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return false;
  }
}
