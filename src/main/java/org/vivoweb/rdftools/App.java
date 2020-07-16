package org.vivoweb.rdftools;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * This class provides a simple CLI for running rdftools
 * - See README.md for usage details
 *
 * @author awoods
 * @since 2020-07-15
 */
@CommandLine.Command(name = "rdftools", mixinStandardHelpOptions = true, sortOptions = false,
        description = "Compare two RDF files, serializations inferred from file extensions",
        version = "RDF Utils - 1.0.0")
public class App implements Callable<Integer> {

    @CommandLine.Option(names = {"--rdf-file-A", "-a"}, required = true, order = 1,
            description = "First RDF file in comparison")
    private File rdfA;

    @CommandLine.Option(names = {"--rdf-file-B", "-b"}, required = true, order = 2,
            description = "Second RDF file in comparison")
    private File rdfB;

    @CommandLine.Option(names = {"--debug"}, order = 30, description = "Enables debug logging")
    private boolean debug;


    public static void main( String[] args ) {
        final App app = new App();
        final CommandLine cmd = new CommandLine(app);
        cmd.setExecutionExceptionHandler(new RDFToolsExceptionHandler(app));

        cmd.execute(args);
    }

    @Override
    public Integer call() throws Exception {

        // Set debug log level if requested
        if (debug) {
            setDebugLogLevel();
        }

        // Ensure arg files exist
        if (!rdfA.exists()) {
            throw new IllegalArgumentException("Arg 'rdfA' must be a file that exists!");
        }

        if (!rdfB.exists()) {
            throw new IllegalArgumentException("Arg 'rdfB' must be a file that exists!");
        }

        ModelDiff modelDiff = new ModelDiff(rdfA, rdfB);
        modelDiff.difference();

        return 0;
    }

    private static void setDebugLogLevel() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger logger = loggerContext.getLogger("org.vivoweb.rdftools");
        logger.setLevel(Level.toLevel("DEBUG"));
    }

    private static class RDFToolsExceptionHandler implements CommandLine.IExecutionExceptionHandler {

        private App app;

        RDFToolsExceptionHandler(final App app) {
            this.app = app;
        }

        @Override
        public int handleExecutionException(
                final Exception ex,
                final CommandLine commandLine,
                final CommandLine.ParseResult parseResult) {
            commandLine.getErr().println(ex.getMessage());
            if (app.debug) {
                ex.printStackTrace(commandLine.getErr());
            }
            commandLine.usage(commandLine.getErr());
            return commandLine.getCommandSpec().exitCodeOnExecutionException();
        }
    }
}
