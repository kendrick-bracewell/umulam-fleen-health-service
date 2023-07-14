package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.member.ProfessionalQualificationType;
import com.umulam.fleen.health.constant.member.ProfessionalType;
import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateAvailabilityStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProfessionalJpaRepository extends JpaRepository<Professional, Integer> {

  @Query("SELECT b FROM Professional b WHERE b.member.emailAddress = :emailAddress")
  Optional<Professional> findProfessionalByEmailAddress(String emailAddress);

  @Query(value ="SELECT availability_status as availabilityStatus from professional where id = :id", nativeQuery = true)
  GetProfessionalUpdateAvailabilityStatusResponse getProfessionalAvailabilityStatus(@Param("id") Integer professionalId);

  @Query(value = "SELECT COUNT(*) FROM Professional p")
  int countAll();

  Page<Professional> findAll(Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.member.firstName LIKE CONCAT('%',INITCAP(?1),'%') AND p.member.lastName LIKE CONCAT('%',INITCAP(?2),'%') ")
  Page<Professional> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.professionalType = :type")
  Page<Professional> findByProfessionalType(@Param("type") ProfessionalType professionalType, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.qualificationType = :qualification")
  Page<Professional> findByQualification(@Param("qualification") ProfessionalQualificationType qualificationType, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.availabilityStatus = :availability")
  Page<Professional> findByAvailabilityStatus(@Param("availability") ProfessionalAvailabilityStatus availabilityStatus, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.languagesSpoken LIKE CONCAT('%',INITCAP(?1),'%')")
  Page<Professional> findByLanguageSpoken(@Param("language") String languageSpoken, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.createdOn <= :created")
  Page<Professional> findByCreatedOnBefore(@Param("created") LocalDateTime createdOn, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.createdOn >= :created")
  Page<Professional> findByCreatedOnAfter(@Param("created") LocalDateTime createdOn, Pageable pageable);

  @Query(value = "SELECT p FROM Professional p WHERE p.createdOn BETWEEN :startDate AND :endDate")
  Page<Professional> findByCreatedOnAndUpdatedOnBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
