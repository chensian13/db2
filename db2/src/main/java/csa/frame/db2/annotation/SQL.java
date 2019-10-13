package csa.frame.db2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import csa.frame.db2.constant.ExecuteType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SQL {
	public String value();
	public String type() default ExecuteType.SELECT;
	public Class resultType() default void.class;
}
