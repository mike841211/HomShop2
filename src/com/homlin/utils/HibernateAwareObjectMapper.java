package com.homlin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * https://github.com/FasterXML/jackson-module-hibernate<br>
 * Jackson与Hibernate LazyLoding无法正常工作解决办法
 * 
 * @author Administrator
 * 
 */
public class HibernateAwareObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HibernateAwareObjectMapper() {
		registerModule(new Hibernate4Module());
	}
}