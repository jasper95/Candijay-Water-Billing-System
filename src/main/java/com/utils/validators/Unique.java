/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Bert
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {
    String message() default "value already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /**
     *
     * @return
     */
    Class<? extends FieldValueExists> service();
    String serviceQualifier() default "";
    String fieldName();
 
}

