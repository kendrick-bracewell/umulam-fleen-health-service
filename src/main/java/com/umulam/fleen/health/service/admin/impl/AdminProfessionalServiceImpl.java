package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.repository.jpa.ProfessionalJpaRepository;
import com.umulam.fleen.health.service.admin.AdminProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminProfessionalServiceImpl implements AdminProfessionalService {

  private final ProfessionalJpaRepository repository;

  public AdminProfessionalServiceImpl(ProfessionalJpaRepository repository) {
    this.repository = repository;
  }
}
