package com.umulam.fleen.health.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.umulam.fleen.health.annotation.impl.ToUppercaseConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(converter = ToUppercaseConverter.class)
@JsonDeserialize(converter = ToUppercaseConverter.class)
public @interface ToUpperCase {
}
