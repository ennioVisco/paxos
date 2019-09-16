package App;

import Networking.TrackerNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileSystemApp {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Application entry point.
     *
     * @param args application command line arguments
     */
    public static void main(String[] args) {
        FileSystemApp app = new FileSystemApp();
        app.run(args);
    }

    /**
     * Runs the application
     *
     * @param args an array of String arguments to be parsed
     */
    public void run(String[] args) {

        CommandLine line = parseArguments(args);

        if (line.hasOption("role")) {

            //System.out.println(line.getOptionValue("role"));
            String role = line.getOptionValue("role");

            if( role.equals("s")) //if Server...
                runServer();
            else //if Peer...
                runPeer();

        } else { //Default: Peer...
            printAppHelp();
            LOGGER.info("No parameter provided, starting peer...");
            runPeer();
        }
    }

    private void runServer() {
        TrackerNode.main(null);
    }

    private void runPeer() {
        FileSystemPeer peer = new FileSystemPeer();
        peer.run();

    }

    /**
     * Parses application arguments
     *
     * @param args application arguments
     * @return <code>CommandLine</code> which represents a list of application
     * arguments.
     */
    private CommandLine parseArguments(String[] args) {

        Options options = getOptions();
        CommandLine line = null;

        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(options, args);

        } catch (ParseException ex) {

            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            printAppHelp();

            System.exit(1);
        }

        return line;
    }

    /**
     * Generates application command line options
     *
     * @return application <code>Options</code>
     */
    private Options getOptions() {

        Options options = new Options();

        options.addOption("r", "role", true, "role of the node: 's' means server");
        return options;
    }

    /**
     * Prints application help
     */
    private void printAppHelp() {
        Options options = getOptions();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("PaxosFileSystem", options, true);
    }
}
