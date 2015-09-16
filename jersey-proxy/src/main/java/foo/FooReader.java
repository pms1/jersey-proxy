package foo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.glassfish.jersey.message.MessageBodyWorkers;

public class FooReader implements MessageBodyReader<Long> {
	@Inject
	private Provider<MessageBodyWorkers> messageBodyWorkers;

//	@Inject
//	public void setX(Provider<MessageBodyWorkers> x) {
//		throw new Error("don't call me");
//	}
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
System.err.println("isReadable " + type + " " + genericType + " " + mediaType + " "+ messageBodyWorkers);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long readFrom(Class<Long> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
