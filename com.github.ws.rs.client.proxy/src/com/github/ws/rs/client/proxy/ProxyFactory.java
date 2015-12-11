package com.github.ws.rs.client.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.client.WebTarget;

public class ProxyFactory {
	public static <T> T createProxy(WebTarget webTarget, Class<T> clazz) {

		Model model = buildModel(clazz);

		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new Handler(model, webTarget));
	}

	private static Model buildModel(Class<?> clazz) {
		if (!clazz.isInterface())
			throw new ModelException("Not an interface: " + clazz);

		Path entityPath = clazz.getAnnotation(Path.class);
		if (entityPath == null)
			throw new ModelException("Not annotated with @Path: " + clazz);
		Consumes entityConsumes = clazz.getAnnotation(Consumes.class);

		Map<Method, ModelMethod> methods = new HashMap<>();

		for (Method m : clazz.getMethods()) {
			Optional<HttpMethod> o = Arrays.stream(m.getAnnotations())
					.map(a -> a.annotationType().getAnnotation(HttpMethod.class)).filter(a -> a != null)
					.reduce((a, b) -> {
						throw new RuntimeException();
					});
			if (!o.isPresent())
				throw new RuntimeException();

			Type returnType;
			if (m.getReturnType().equals(Void.TYPE))
				returnType = null;
			else
				returnType = m.getGenericReturnType();

			Path methodPath = m.getAnnotation(Path.class);

			Type parameterType;
			String parameterMimeType;
			if (m.getParameterTypes().length == 1) {
				if (o.get().value().equals(HttpMethod.GET))
					throw new RuntimeException();
				parameterType = m.getGenericParameterTypes()[0];
				Consumes methodConsumes = m.getAnnotation(Consumes.class);
				Consumes consumes = methodConsumes != null ? methodConsumes : entityConsumes;
				if (consumes == null)
					throw new RuntimeException();
				parameterMimeType = consumes.value()[0];
			} else {
				parameterType = null;
				parameterMimeType = null;
			}

			methods.put(m, new ModelMethod(o.get(), parameterType, parameterMimeType, returnType,
					joinPath(entityPath, methodPath)));
		}

		return new Model(methods);
	}

	private static String joinPath(Path entityPath, Path methodPath) {
		if (entityPath != null && entityPath.value().isEmpty())
			entityPath = null;
		if (methodPath != null && methodPath.value().isEmpty())
			methodPath = null;

		if (entityPath == null && methodPath == null)
			return null;
		else if (entityPath != null && methodPath == null)
			return entityPath.value();
		else if (entityPath == null && methodPath != null)
			return methodPath.value();
		else
			return entityPath.value() + "/" + methodPath.value();
	}
}
