/**
 *
 */
package com.tiny.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zouqinghua
 * @date 2016年4月9日 下午6:01:54
 *
 */
public class JsonUtils {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);


	public static ObjectMapper NOT_NULL_MAPPER = new ObjectMapper();
	static{
		// 设置输出时包含属性的风格
		NOT_NULL_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		NOT_NULL_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		NOT_NULL_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	/**
	 * 格式化日期，有的返回值的日期是有格式化的，所以需要特殊的转一下
	 */
	public static ObjectMapper DATA_FORMAT_MAPPER = new ObjectMapper();
	static{
		// 设置输出时包含属性的风格
		DATA_FORMAT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		DATA_FORMAT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DATA_FORMAT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		DATA_FORMAT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


	}



	private static ObjectMapper mapper = new ObjectMapper();
	static{
		// 设置输出时包含属性的风格
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


	}


	public static String toJson(Object object) {
		if (object == null) {
			return StringUtils.EMPTY;
		}
		try {
			return object instanceof String ? (String) object : mapper.writeValueAsString(object);
		} catch (Exception e) {
			String message = String.format("Object to jsonString error;object=%s", object.getClass());
			logger.error(message, e);
			return StringUtils.EMPTY;
		}
	}

	public static <T> T toBean(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return null;
		}
		try {
			return clazz.equals(String.class) ? (T) json : mapper.readValue(json, clazz);
		} catch (Exception e) {
			String message = String.format("jsonString to Object error;jsonString=%s", json);
			logger.error(message, e);
			return null;
		}

	}

	public static String toJson(Object object, ObjectMapper objectMapper) {
		if (object == null) {
			return StringUtils.EMPTY;
		}
		try {
			return object instanceof String ? (String) object : objectMapper==null? mapper.writeValueAsString(object):objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			String message = String.format("Object to jsonString error;object=%s", object.getClass());
			logger.error(message, e);
			return StringUtils.EMPTY;
		}
	}


	/**
	 * 将json通过类型转换成对象
	 *
	 * @param json          json字符串
	 * @param typeReference 引用类型
	 * @return 返回对象
	 * @throws IOException
	 */
	public static <T> T readJson(String json, TypeReference<T> typeReference) {

		if (StringUtils.isBlank(json) || typeReference == null) {
			return null;
		}
		try {
			return (T) (typeReference.getType().equals(String.class) ? json : mapper.readValue(json, typeReference));
		} catch (Exception e) {
			String message = String.format("jsonString to Object error;jsonString=%s", json);
			logger.error(message, e);
			return null;
		}
	}




	/**
	 * 将json通过类型转换成对象
	 *
	 * @param json          json字符串
	 * @param typeReference 引用类型
	 * @return 返回对象
	 * @throws IOException
	 */
	public static <T> T readJson(String json, TypeReference<T> typeReference, ObjectMapper defMapper) {

		if (StringUtils.isBlank(json) || typeReference == null) {
			return null;
		}
		try {
			return (T) (typeReference.getType().equals(String.class) ? json : defMapper==null?mapper.readValue(json, typeReference):defMapper.readValue(json,typeReference));
		} catch (Exception e) {
			String message = String.format("jsonString to Object error;jsonString=%s", json);
			logger.error(message, e);
			return null;
		}
	}



	public static JsonNode parseObject(String jsonStr) {
		try {
			return mapper.readTree(jsonStr);
		} catch (Exception e) {
			String message = String.format("jsonString to JsonNode error;jsonString=%s", jsonStr);
			logger.error(message, e);
			return null;
		}
	}


	public static Map<String,Object> parseJsonToHttpParams(JsonNode jsonNode){
	    return parseJsonToMap(jsonNode,null);
    }

	private static Map<String, Object> parseJsonToMap(JsonNode rootNode, String keyPre){
		Map<String, Object> map = Maps.newHashMap();

		if(StringUtils.isNoneBlank(keyPre)){
			keyPre += ".";
		}else{
			keyPre ="";
		}

		if(rootNode == null ){
			return map;
		}
		Iterator<String> fieldNames = rootNode.fieldNames();

		while(fieldNames.hasNext()){
			String fieldName = fieldNames.next();
			String key = keyPre+fieldName;
			JsonNode childNode=rootNode.path(fieldName);
			put(childNode,key,map);

		}
		return map;
	}

	private  static Map<String, Object> parseJsonArrayToMap(JsonNode rootNode, String keyPre){
		Map<String, Object> map = Maps.newHashMap();
		if(keyPre == null){
			keyPre ="";
		}
		if(!rootNode.isArray()) {
			return map;
		}
		Iterator<JsonNode> elements = rootNode.elements();
		Integer index = 0;
		while(elements.hasNext()) {

			JsonNode element = elements.next();
			String key = keyPre+"["+index+"]";
			put(element,key,map);
			index++;

		}
		return map;
	}

	private  static void put(JsonNode jsonNode, String key, Map<String,Object> map){
		if(jsonNode.isArray()){
			map.putAll(parseJsonArrayToMap(jsonNode,key));
		}else if(jsonNode.isTextual()){
			map.put(key,jsonNode.asText());
		}else if(jsonNode.isNumber()){
			map.put(key,jsonNode.numberValue());
		}else if(jsonNode.isObject()){
			map.putAll(parseJsonToMap(jsonNode,key));
		}
	}



}
