package com.umulam.fleen.health.constant.member;

import com.umulam.fleen.health.adapter.ApiParameter;
import lombok.Getter;

@Getter
public enum ProfessionalTitle implements ApiParameter {

  ADDICTION_COUNSELOR("Addiction Counselor"),
  ART_THERAPIST("Art Therapist"),
  BEHAVIORAL_THERAPIST("Behavioral Therapist"),
  CHILD_PSYCHOLOGIST("Child Psychologist"),
  CLINICAL_PSYCHOLOGIST("Clinical Psychologist"),
  COGNITIVE_BEHAVIORAL_THERAPIST("Cognitive-Behavioral Therapist"),
  COUNSELOR("Counselor"),
  COUPLES_THERAPIST("Couples Therapist"),
  DANCE_MOVEMENT_THERAPIST("Dance & Movement Therapist"),
  EDUCATIONAL_PSYCHOLOGIST("Educational Psychologist"),
  FAMILY_THERAPIST("Family Therapist"),
  FORENSIC_PSYCHOLOGIST("Forensic Psychologist"),
  GERIATRIC_COUNSELOR("Geriatric Counselor"),
  HEALTH_PSYCHOLOGIST("Health Psychologist"),
  HOLISTIC_THERAPIST("Holistic Therapist"),
  INDUSTRIAL_ORGANIZATIONAL_PSYCHOLOGIST("Industrial-Organizational Psychologist"),
  LICENSED_CLINICAL_SOCIAL_WORKER("Licensed Clinical Social Worker"),
  MARRIAGE_AND_FAMILY_THERAPIST("Marriage and Family Therapist"),
  MENTAL_HEALTH_COUNSELOR("Mental Health Counselor"),
  MUSIC_THERAPIST("Music Therapist"),
  NEUROPSYCHOLOGIST("Neuropsychologist"),
  OCCUPATIONAL_THERAPIST("Occupational Therapist"),
  PEDIATRIC_PSYCHOLOGIST("Pediatric Psychologist"),
  PLAY_THERAPIST("Play Therapist"),
  POSITIVE_PSYCHOLOGY_COACH("Positive Psychology Coach"),
  PSYCHIATRIST("Psychiatrist"),
  PSYCHODYNAMIC_THERAPIST("Psychodynamic Therapist"),
  PSYCHOTHERAPIST("Psychotherapist"),
  REHABILITATION_THERAPIST("Rehabilitation Therapist"),
  SCHOOL_COUNSELOR("School Counselor"),
  SEX_THERAPIST("Sex Therapist"),
  SOCIAL_WORKER("Social Worker"),
  SPEECH_THERAPIST("Speech Therapist"),
  SPORT_PSYCHOLOGIST("Sport Psychologist"),
  SUBSTANCE_ABUSE_COUNSELOR("Substance Abuse Counselor"),
  TELEHEALTH_THERAPIST("Telehealth Therapist"),
  THERAPIST("Therapist"),
  TRAUMA_THERAPIST("Trauma Therapist"),
  YOGA_THERAPIST("Yoga Therapist");

  private final String value;

  ProfessionalTitle(String value) {
    this.value = value;
  }

}

