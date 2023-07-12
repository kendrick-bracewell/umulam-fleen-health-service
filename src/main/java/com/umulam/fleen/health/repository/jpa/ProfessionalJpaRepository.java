package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateAvailabilityStatusResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessionalJpaRepository extends JpaRepository<Professional, Integer> {

  @Query("SELECT b FROM Professional b WHERE b.member.emailAddress = :emailAddress")
  Optional<Professional> findProfessionalByEmailAddress(String emailAddress);

  @Query(value ="SELECT availability_status as availabilityStatus from professional where id = :id", nativeQuery = true)
  GetProfessionalUpdateAvailabilityStatusResponse getProfessionalAvailabilityStatus(@Param("id") Integer professionalId);
}
