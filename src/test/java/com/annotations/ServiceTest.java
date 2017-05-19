package com.annotations;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration({"classpath:spring/application-context.xml"})
@TestPropertySource(locations = "classpath:spring/jdbc-test.properties")
@ActiveProfiles("dev")
@Transactional
public @interface ServiceTest { }
