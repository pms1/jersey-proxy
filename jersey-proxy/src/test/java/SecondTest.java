import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Rule;
import org.junit.Test;

import com.github.ws.rs.client.proxy.ProxyFactory;

@TestResources(RestServer.class)
public class SecondTest {

	@Rule
	public EmbededServerRule server = new EmbededServerRule();

	@XmlRootElement
	@XmlSeeAlso({ B.class, C.class })
	static public class A {

	}

	@XmlRootElement
	static public class B extends A {

	}

	@XmlRootElement
	static public class C extends A {

	}

	@Path("")
	public interface IF1 {
		@Path("")
		@POST
		@Consumes(MediaType.APPLICATION_XML)
		String post(List<A> as);

		@Path("a")
		@POST
		@Consumes(MediaType.APPLICATION_XML)
		List<A> post2(List<A> as);
	}

	public static class C1 implements IF1 {

		@Override
		public String post(List<A> as) {
			return as.toString();
		}

		@Override
		public List<A> post2(List<A> as) {
			return as;
		}

	}

	@TestResources(C1.class)
	@Test
	public void t4() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		IF1 if1 = ProxyFactory.createProxy(client.target(server.getURI()), IF1.class);
		String r = if1.post(Arrays.asList(new A(), new B(), new C()));

		System.err.println("R=" + r);
	}

	@TestResources(C1.class)
	@Test
	public void t5() {
		Client client = ClientBuilder.newClient();
		client.register(new LoggingFilter());
		IF1 if1 = ProxyFactory.createProxy(client.target(server.getURI()), IF1.class);
		List<A> r = if1.post2(Arrays.asList(new A(), new B(), new C()));

		System.err.println("R2=" + r);
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
