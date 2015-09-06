import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class RestServer {

	@GET
	@Produces("text/html")
	public String getMessage() {
		System.out.println("sayHello()");
		return "Hello, world!";
	}

	@GET
	@Path("foo")
	@Produces("application/xml")
	public Long getMessage2() {
		return 42L;
	}

}