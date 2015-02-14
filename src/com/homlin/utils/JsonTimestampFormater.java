package com.homlin.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonTimestampFormater extends JsonSerializer<Timestamp> {

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider sp) throws IOException, JsonProcessingException {
		jgen.writeString(sdf.format(value));
	}

}