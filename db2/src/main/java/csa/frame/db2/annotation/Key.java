package csa.frame.db2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代表主键的字段
 * @author csa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Key{
	/**
	 * 查询列名
	 * @return
	 */
	public String value();
	/**
	 * 实体类属性名
	 * @return
	 */
	public String property() default "";
	/**
	 * 查询列名
	 * @return
	 */
	public String column() default "";

}
