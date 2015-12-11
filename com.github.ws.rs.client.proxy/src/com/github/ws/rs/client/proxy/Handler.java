package com.github.ws.rs.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;

public class Handler implements InvocationHandler {
	final Model model;
	final WebTarget target;

	public Handler(Model model, WebTarget webTarget) {
		Objects.requireNonNull(model);
		Objects.requireNonNull(webTarget);
		this.model = model;
		this.target = webTarget;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		ModelMethod modelMethod = model.methods.get(method);

		if (modelMethod == null)
			throw new RuntimeException("no method " + method);

		WebTarget t = target;

		if (modelMethod.path != null && !modelMethod.path.isEmpty())
			t = target.path(modelMethod.path);

		Builder request = t.request();

		if (modelMethod.parameterType != null) {
			Entity entity = Entity.entity(new GenericEntity<>(args[0], modelMethod.parameterType),
					modelMethod.parameterMediaType);

			if (modelMethod.returnType != null)
				return request.method(modelMethod.method, entity, new GenericType(modelMethod.returnType));
			else
				request.method(modelMethod.method, entity);
		} else {
			if (modelMethod.returnType != null)
				return request.method(modelMethod.method, new GenericType(modelMethod.returnType));
			else
				request.method(modelMethod.method);
		}

		return null;
	}

}
