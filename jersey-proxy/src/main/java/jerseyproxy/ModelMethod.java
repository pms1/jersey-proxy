package jerseyproxy;

import java.lang.reflect.Type;

import javax.ws.rs.HttpMethod;

public class ModelMethod {
	final String method;
	public final Type returnType;
	
	public ModelMethod(HttpMethod httpMethod, Type returnType) {
		this.method = httpMethod.value();
		this.returnType = returnType;
		}

}
