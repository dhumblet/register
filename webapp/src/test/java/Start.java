import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.joda.time.Duration;

/**
 * Created by derkhumblet on 10/11/14.
 */
public final class Start {

    private static final int ERROR_EXIT_CODE = 100;
    private static final int SERVER_INTERRUPT_SLEEP = 5000;
    private static final int PORT = 9999;

    private Start() { }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime((int) Duration.standardHours(1).getMillis());
        connector.setSoLingerTime(-1);
        connector.setPort(PORT);
        server.setConnectors(new Connector[] { connector });

        WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath("/");
        bb.setWar("web");

        server.setHandler(bb);

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(SERVER_INTERRUPT_SLEEP);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(ERROR_EXIT_CODE);
        }
    }
}

