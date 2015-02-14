package com.homlin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * JacksonUtil封装工具
 * 
 * Date:2014-06-19 12:09:27
 * 
 * @version 1.0.0
 * @author wuduanpiao
 */
public class JacksonUtil {

	private static JacksonUtil jacksonUtil = null;

	private ObjectMapper objectMapper;

	public static JacksonUtil getInstance() {
		if (jacksonUtil == null) {
			synchronized (JacksonUtil.class) {
				if (jacksonUtil == null) {
					jacksonUtil = new JacksonUtil();
				}
			}
		}
		return jacksonUtil;
	}

	private JacksonUtil() {
		objectMapper = new ObjectMapper();

		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		objectMapper.registerModule(new Hibernate4Module()); // Jackson与Hibernate LazyLoding无法正常工作解决办法
	}

	public JacksonUtil setDateFormate(DateFormat dateFormat) {
		objectMapper.setDateFormat(dateFormat);
		return this;
	}

	public JacksonUtil filter(String filterName, String... properties) {
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName,
				SimpleBeanPropertyFilter.serializeAllExcept(properties));
		objectMapper.setFilters(filterProvider);
		return this;
	}

	public JacksonUtil addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
		// objectMapper.getSerializationConfig().addMixInAnnotations(target, mixinSource);
		// objectMapper.getDeserializationConfig().addMixInAnnotations(target, mixinSource);
		return this;
	}

	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	public String obj2Json(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析对象错误");
		}
	}

	public <T> T json2Obj(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> json2List(String json) {
		try {
			return objectMapper.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}

	public JsonNode json2JsonNode(String json) {
		try {
			return objectMapper.readTree(json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}

	// ================= static ===================

	public static ObjectMapper getSimpleObjectMapper() {
		return new ObjectMapper();
	}

	public static String toJsonString(Object obj) {
		return getInstance().obj2Json(obj);
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		return getInstance().json2Obj(json, clazz);
	}

	public static List<Map<String, Object>> toMapList(String json) {
		return getInstance().json2List(json);
	}

	public static JsonNode toJsonNode(String json) {
		return getInstance().json2JsonNode(json);
	}

	public static <T> T convertValue(Object object, Class<T> clazz) {
		return getInstance().getObjectMapper().convertValue(object, clazz);
	}

	/**
	 * Usage:<br>
	 * List&lt;User&gt; list = JacksonUtil.toTypeReferenceList(json, new TypeReference&lt;List&lt;User&gt;&gt;() {});
	 */
	public static <T> List<T> toTypeReferenceList(String json, TypeReference<List<T>> typeReference) {
		// 需要用到TypeReference
		try {
			return getInstance().getObjectMapper().readValue(json, typeReference);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
		// 如果反序列化为List<LinkedHashMap<String,String>>，则直接
		// try {
		// return getInstance().getObjectMapper().readValue(json, List.class);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new RuntimeException("解析json错误");
		// }
	}

}
