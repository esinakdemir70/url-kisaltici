package com.esinakdemir70.urlkisaltici;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Alanın http veya https şemalı, host içeren geçerli bir URL olmasını şart koşar.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GecerliUrlValidator.class)
public @interface GecerliUrl {

	String message() default "Geçerli bir URL giriniz (http veya https ile başlamalı).";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
