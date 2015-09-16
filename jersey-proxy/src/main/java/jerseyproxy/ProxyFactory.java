package jerseyproxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.filter.LoggingFilter;

public class ProxyFactory {
	public static <T> T createProxy(WebTarget webTarget, Class<T> clazz) {

		Model model = buildModel(clazz);

		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new Handler(model, webTarget));
	}

	private static Model buildModel(Class<?> clazz) {
		if (!clazz.isInterface())
			throw new ModelException("Not an interface: " + clazz);

		Map<Method, ModelMethod> methods = new HashMap<>();

		for (Method m : clazz.getMethods()) {
			Optional<HttpMethod> o = Arrays.stream(m.getAnnotations()).map(a -> a.annotationType().getAnnotation(HttpMethod.class))
					.filter(a -> a != null).reduce((a, b) -> {
						throw new RuntimeException();
					});
			if(!o.isPresent())
				throw new RuntimeException();
			
			Type returnType;
			if(m.getReturnType().equals(Void.TYPE))
				returnType = null;
			else
				returnType = m.getGenericReturnType();
			
			methods.put(m,  new ModelMethod(o.get(), returnType));
		}

		return new Model(methods);
	}
}
