package jerseyproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class Handler implements InvocationHandler {
	final Model model;
	final	WebTarget target;
		
	public Handler(Model model, WebTarget webTarget) {
		Objects.requireNonNull(model);
		Objects.requireNonNull(webTarget);
		this.model = model;
		this.target = webTarget;
			}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		ModelMethod modelMethod = model.methods.get(method);
		
		if(modelMethod == null)
			throw new RuntimeException("no method "+ method);
		
		Builder request = target.request();
		Response resp;
		if(modelMethod.returnType != null)
			return 	request.method(modelMethod.method, new GenericType(modelMethod.returnType));
		else
			request.method(modelMethod.method);
		return null;}

}
