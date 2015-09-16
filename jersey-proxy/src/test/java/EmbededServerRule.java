import java.net.URI;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.simple.SimpleContainerFactory;
import org.glassfish.jersey.simple.SimpleServer;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class EmbededServerRule implements TestRule {

	static final int port = 5555;

	@Override
	public Statement apply(Statement base, Description description) {

		
		TestResources resources = description.getAnnotation(TestResources.class);

		System.err.println("D " + description + " " + resources);
		
		if (resources == null)
			resources = description.getTestClass().getAnnotation(TestResources.class);

		if (resources == null)
			throw new Error("Test " + description + " has no @" + TestResources.class + " annotation");

		System.err.println("RES " + resources);
		
		ResourceConfig resourceConfig = new ResourceConfig(resources.value());

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try (SimpleServer server = SimpleContainerFactory.create(getURI(), resourceConfig)) {
					base.evaluate();
				}
			}

		};
	}

	public URI getURI() {
		return URI.create("http://127.0.0.1:" + port);
	}
}
