package com.github.ws.rs.client.proxy;

import java.lang.reflect.Type;

import javax.ws.rs.HttpMethod;

class ModelMethod {
	final String method;
	final Type returnType;
	final Type parameterType;
	final String parameterMediaType;
	final String path;

	public ModelMethod(HttpMethod httpMethod, Type parameterType, String parameterMimeType, Type returnType,
			String path) {
		this.method = httpMethod.value();
		this.returnType = returnType;
		this.parameterType = parameterType;
		this.parameterMediaType = parameterMimeType;
		this.path = path;
	}

}
