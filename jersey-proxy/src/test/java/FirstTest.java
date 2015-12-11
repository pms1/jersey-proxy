import static org.hamcrest.MatcherAssert.assertThat;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import com.github.ws.rs.client.proxy.ProxyFactory;

@TestResources(RestServer.class)
public class FirstTest {

	@Rule
	public EmbededServerRule server = new EmbededServerRule();

	static class X implements Feature {

		@Inject
		private ServiceLocator serviceLocator;

		@Override
		public boolean configure(FeatureContext context) {
			System.err.println("configure " + context + " " + serviceLocator);
			context.property("foo", "bar");
			return true;
		}

	}

	@Test
	public void t1() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		client.register(X.class);
		// client.register(new X());
		System.err.println("configure result1 " + client.getConfiguration().getProperty("foo"));
		WebTarget target = client.target(server.getURI()).path("helloworld");
		System.err.println("configure result2 " + client.getConfiguration().getProperty("foo"));
		Response response = target.request().get();
		System.err.println("configure result3 " + client.getConfiguration().getProperty("foo"));
		System.err.println("R=" + response);

	}

	Object a = new Object() {
		@Inject
		MessageBodyWorkers x;

		public String toString() {
			return super.toString() + " x=" + x;
		}
	};

	@Test
	public void t2() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());

		WebTarget target = client.target(server.getURI()).path("helloworld/foo");

		Long r = target.request().get(Long.class);
		System.err.println("a=" + r);

	}

	static class Foo implements Feature {
		@Inject
		public void setX(Provider<MessageBodyWorkers> x) {
			System.err.println("SSS 0 " + x);
		}

		@Override
		public boolean configure(FeatureContext context) {
			ServiceLocator serviceLocator = ServiceLocatorProvider.getServiceLocator(context);
			MessageBodyWorkers service = serviceLocator.getService(MessageBodyWorkers.class);
			System.err.println("SSS 1 " + service);
			return true;
		}

	}

	@Test
	public void t3() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		if (true) {
			client.register(Foo.class);
		}
		client.register(new Feature() {

			@Override
			public boolean configure(FeatureContext context) {
				ServiceLocator serviceLocator = ServiceLocatorProvider.getServiceLocator(context);
				MessageBodyWorkers service = serviceLocator.getService(MessageBodyWorkers.class);
				System.err.println("SSS " + service);
				return false;
			}

		});

		for (Object i : client.getConfiguration().getInstances())
			System.err.println("i=" + i);
		WebTarget target = client.target(server.getURI()).path("helloworld/foo2");

		System.err.println("CONF " + target.getConfiguration());
		System.err.println("CONF " + target.getConfiguration().getContracts(Foo.class));
		System.err.println("CONF " + target.getConfiguration().getInstances());

		target.request().get(A.class);

	}

	@Path("")
	public interface IF1 {
		@GET
		String hello();

		@GET
		@Path("a")
		String hello1();
	}

	@Path("foo")
	public interface IF2 {
		@GET
		String hello();

		@GET
		@Path("a")
		String hello1();
	}

	public static class C1 implements IF1 {

		@Override
		public String hello() {
			return "c1:hello";

		}

		@Override
		public String hello1() {
			return "c1:hello1";
		}

	}

	public static class C2 implements IF2 {

		@Override
		public String hello() {
			return "c2:hello";

		}

		@Override
		public String hello1() {
			return "c2:hello1";
		}

	}

	@TestResources(C1.class)
	@Test
	public void t4() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		IF1 if1 = ProxyFactory.createProxy(client.target(server.getURI()), IF1.class);
		String r = if1.hello();
		assertThat(r, CoreMatchers.equalTo("c1:hello"));

		r = if1.hello1();
		assertThat(r, CoreMatchers.equalTo("c1:hello1"));

		// client.target(server.getURI()).request().get();
		// Builder req = client.target(server.getURI()).request();

	}

	@TestResources(C2.class)
	@Test
	public void t41() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		IF2 if1 = ProxyFactory.createProxy(client.target(server.getURI()), IF2.class);
		String r = if1.hello();
		assertThat(r, CoreMatchers.equalTo("c2:hello"));

		r = if1.hello1();
		assertThat(r, CoreMatchers.equalTo("c2:hello1"));

		// client.target(server.getURI()).request().get();
		// Builder req = client.target(server.getURI()).request();

	}

	@Path("")
	static public class C3 {
		@Path("")
		public B a() {
			return new B();
		}
	}

	static public class B {
		@GET
		@Path("")
		public A a() {
			return new A();
		}
	}

	@TestResources(C3.class)
	@Test
	public void t5() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());

		A r = client.target(server.getURI()).request().get(A.class);
		// Builder req = client.target(server.getURI()).request();

	}

	static Logger dummy;

	static {
		for (Handler h : LogManager.getLogManager().getLogger("").getHandlers()) {
			System.err.println("H=" + h);
			h.setLevel(Level.FINEST);
		}
		LogManager.getLogManager().getLogger("").setLevel(Level.FINEST);
		Logger.getLogger("org.glassfish.jersey.server.model.IntrospectionModeller").setLevel(Level.INFO);
		LogManager.getLogManager().getLogger("").fine("FINE");
		Logger l = Logger.getLogger("org.glassfish.jersey.server.model.IntrospectionModeller");
		l.fine("FINE2");
		dummy = l;
	}
}
