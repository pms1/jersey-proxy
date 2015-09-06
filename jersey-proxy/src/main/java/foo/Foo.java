package foo;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.message.MessageBodyWorkers;

public class Foo implements MessageBodyWriter<Long> {

	@Inject
	private Provider<MessageBodyWorkers> messageBodyWorkers;

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		System.err.println("isWriteable " + type + " " + genericType + " " + mediaType + " " + messageBodyWorkers);

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getSize(Long t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		System.err.println("getSize " + type + " " + genericType + " " + mediaType);
		return 0;
	}

	@Override
	public void writeTo(Long t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		System.err.println("writeTo " + type + " " + genericType + " " + mediaType);

	}

}
