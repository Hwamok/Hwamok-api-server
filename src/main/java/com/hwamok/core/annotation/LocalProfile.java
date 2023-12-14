package com.hwamok.core.annotation;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Profile({"default", "local"})
@Retention(RUNTIME)
public @interface LocalProfile {
}
