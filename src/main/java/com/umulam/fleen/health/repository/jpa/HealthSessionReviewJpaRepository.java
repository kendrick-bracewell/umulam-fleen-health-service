package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.HealthSessionReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthSessionReviewJpaRepository extends JpaRepository<HealthSessionReview, Long> {

  @Query(value = "SELECT hsr FROM HealthSessionReview hsr WHERE hsr.patient.id = :memberId")
  List<HealthSessionReview> findPatientReviews(@Param("memberId") Long memberId);

  @Query(value = "SELECT hsr FROM HealthSessionReview hsr WHERE hsr.professional.id = :memberId")
  List<HealthSessionReview> findProfessionalReviews(@Param("memberId") Long memberId);
}
