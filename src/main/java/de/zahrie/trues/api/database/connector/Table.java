package de.zahrie.trues.api.database.connector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
  String value();
  String department() default "";
}
