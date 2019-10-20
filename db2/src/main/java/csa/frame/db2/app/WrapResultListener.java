package csa.frame.db2.app;

import java.sql.ResultSet;

import csa.frame.db2.template.Beanhandler;

public interface WrapResultListener<T> {
	void wrapResult(Beanhandler<T> beanhandler,ResultSet rs) throws Exception;

}
