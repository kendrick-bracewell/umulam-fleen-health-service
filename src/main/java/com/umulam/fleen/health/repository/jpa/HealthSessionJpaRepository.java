package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.model.domain.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HealthSessionJpaRepository extends JpaRepository<Professional, Integer> {

  @Query(
    value = "SELECT p FROM Professional p WHERE p.member.firstName LIKE CONCAT('%',INITCAP(:firstName),'%') " +
            "AND p.member.lastName LIKE CONCAT('%',INITCAP(:lastName),'%') AND p.availabilityStatus = :availability")
  Page<Professional> findByFirstNameAndLastName(String firstName, String lastName, @Param("availability") ProfessionalAvailabilityStatus status, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.professionalType = :type AND p.availabilityStatus = :availability")
  Page<Professional> findByProfessionalType(@Param("type") ProfessionalType type, @Param("availability") ProfessionalAvailabilityStatus status, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.qualificationType = :qualification AND p.availabilityStatus = :availability")
  Page<Professional> findByQualification(@Param("qualification") ProfessionalQualificationType qualification, @Param("availability") ProfessionalAvailabilityStatus status, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.languagesSpoken LIKE CONCAT('%',INITCAP(:language),'%') AND p.availabilityStatus = :availability")
  Page<Professional> findByLanguageSpoken(@Param("language") String languageSpoken, @Param("availability") ProfessionalAvailabilityStatus status, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.availabilityStatus = :availability")
  Page<Professional> findByAvailabilityStatus(@Param("availability") ProfessionalAvailabilityStatus status, Pageable pageable);
}
