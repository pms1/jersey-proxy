import java.io.Closeable;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.simple.SimpleContainerFactory;
import org.junit.ClassRule;
import org.junit.Test;

@TestResources(RestServer.class)
public class FirstTest {
	public static void main(String[] args) throws Exception {

		ResourceConfig resourceConfig = new ResourceConfig(RestServer.class);
		// DefaultResourceConfig resourceConfig = new
		// DefaultResourceConfig(RestServer.class);
		// The following line is to enable GZIP when client accepts it
		// resourceConfig.getContainerResponseFilters().add(new
		// GZIPContentEncodingFilter());
		Closeable server = SimpleContainerFactory.create(URI.create("http://127.0.0.1:5555"), resourceConfig);
		try {
			System.out.println("Press any key to stop the service...");
			System.in.read();
		} finally {
			server.close();
		}
	}

	@ClassRule
	public static EmbededServerRule server = new EmbededServerRule();

	// @Rule
	// public TestRule r1 = new TestRule() {
	//
	// public Statement apply(final Statement base, Description description) {
	// return new Statement() {
	//
	// @Override
	// public void evaluate() throws Throwable {
	//
	// ResourceConfig resourceConfig = new ResourceConfig(RestServer.class);
	//
	// try (Closeable server =
	// SimpleContainerFactory.create(URI.create("http://127.0.0.1:5555"),
	// resourceConfig)) {
	//
	// base.evaluate();
	//
	// }
	//
	// }
	//
	// };
	// }
	//
	// };

	@Test
	public void t1() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		WebTarget target = client.target("http://localhost:5555").path("helloworld");

		Response response = target.request().get();

		System.err.println("R=" + response);

	}

	@Test
	public void t2() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		WebTarget target = client.target("http://localhost:5555").path("helloworld/foo");

		Response response = target.request().get();

		System.err.println("R=" + response);

	}
}
