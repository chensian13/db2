package csa.frame.db2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import csa.frame.db2.constant.IsolateLevel;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transaction {
	public int isolate() default IsolateLevel.NO_TRANSACTION;

}
