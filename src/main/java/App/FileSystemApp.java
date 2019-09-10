package App;

import Execution.TrackerNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
        LOGGER.debug("Server should now do something");
        TrackerNode.main(null);
    }

    private void runPeer() {
        LOGGER.debug("Peer should now do something");

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
     * Reads application data from a file
     *
     * @param fileName file of application data
     * @return array of double values
     */
    private double[] readData(String fileName) {

        List data = new ArrayList<Double>();
        double[] mydata = null;

        /*try (var reader = Files.newBufferedReader(Paths.get(fileName));
             var csvReader = new CSVReaderBuilder(reader).build()) {

            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {

                for (String e : nextLine) {

                    data.add(Double.parseDouble(e));
                }
            }

            mydata = ArrayUtils.toPrimitive(data.toArray(new Double[0]));

        } catch (IOException ex) {

            System.err.println("Failed to read file");
            System.err.println(ex.toString());
            System.exit(1);
        }*/

        return mydata;
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
