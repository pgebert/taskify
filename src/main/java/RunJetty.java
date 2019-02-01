
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class RunJetty {
	public static void main(String[] args) throws Exception {
		// Create the server
		Server server = new Server(8081);

		// Enable parsing of jndi-related parts of web.xml and jetty-env.xml
		org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList
				.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore(
				"org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");

		// Create a WebApp
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setWar("./taskify-1.0-SNAPSHOT.war");
		server.setHandler(webapp);

		server.start();
		server.join();
	}
}
