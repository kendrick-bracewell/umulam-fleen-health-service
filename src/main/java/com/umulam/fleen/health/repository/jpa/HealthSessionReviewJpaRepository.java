package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.HealthSessionReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HealthSessionReviewJpaRepository extends JpaRepository<HealthSessionReview, Integer> {


  @Query(value = "SELECT hsr FROM HealthSessionReview hsr WHERE hsr.patient.id = :memberId")
  List<HealthSessionReview> findPatientReviews(@Param("memberId") Integer memberId);

  @Query(value = "SELECT hsr FROM HealthSessionReview hsr WHERE hsr.professional.id = :memberId")
  List<HealthSessionReview> findProfessionalReviews(@Param("memberId") Integer memberId);

}
