package embla3d.engine;

import org.lwjgl.system.APIUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.*;

/**
 * Example of use :
 * <p>
 * I) For OpenGl and Java to put : LoggerEMBLA3D logger = LoggerEMBLA3D.getInstance(Level.INFO);
 * <p>
 * // 1.  -> For OpenGl :
 * logger.activateFileMode(); // or logger.activateConsoleMode()
 * GLUtil.setupDebugMessageCallback(logger);
 * <p>
 * // 2. -> For Java :
 * <p>
 * logger.activateFileMode(); // or logger.activateConsoleMode()
 * logger.workJavaLogger("error", "log", Level.WARNING);
 */


/**
 * This class represents our logger it inherited from PrintStream to be used for debug opengl .
 */
public class LoggerEMBLA3D extends PrintStream {

    /**
     * Create a log with the name proposed on the first call, it will be retrieved but not recreated on future calls
     */
    private static LoggerEMBLA3D instance = new LoggerEMBLA3D(APIUtil.DEBUG_STREAM);
    private static Logger logger;
    private static int compteur = 0;
    private static Level filtreLevel;
    private Level levelLogger;
    private boolean activateConsoleMode = false;
    private boolean activateFileMode = false;
    /**
     * nameLogger : must be formatted -> Package.subPackage.Class (Advice for clarity)
     */
    private String nameLogger;
    private ConsoleHandler consoleHandler;
    private Handler fileHandler;
    /**
     * For Java debugging messages
     **/
    private String messageLogJava = "";
    /**
     * For OpenGL debugging messages
     **/
    private String messageLogOpenGL = "";


    private LoggerEMBLA3D(OutputStream out) {
        super(out);
        init("loggerEMBLA3D"); /** Allows to initialize the internal logger  */
    }

    /**
     * Create an instance of our logger with a filtering per level of criticality of the message
     * (messages with the level passed in parameter and more will display the other not)
     *
     * @param filtreLev filtreLev Allows to filter the messages according to the level of the messages
     * @return
     */
    public static LoggerEMBLA3D getInstance(Level filtreLev) {
        filtreLevel = filtreLev;
        return instance;
    }

    /**
     * Allows to choose the level of the message to be allocated in the logger.
     *
     * @param lev lev level of logger Chosen by the developer.
     */
    public void choiceLevel(String lev) {
        switch (lev) {

            case "ALL":
                levelLogger = Level.ALL;
                break;
            case "NOTIFICATION":
                levelLogger = Level.INFO;
                break;
            case "HIGH":
                levelLogger = Level.SEVERE;
                break;
            case "MEDIUM":
                levelLogger = Level.WARNING;
                break;
            case "LOW":
                levelLogger = Level.INFO;
                break;
            case "SEVERE":
                levelLogger = Level.SEVERE;
                break;
            case "WARNING":
                levelLogger = Level.WARNING;
                break;
            case "INFO":
                levelLogger = Level.INFO;
                break;
            case "CONFIG":
                levelLogger = Level.CONFIG;
                break;
            case "FINE":
                levelLogger = Level.FINE;
                break;
            case "FINER":
                levelLogger = Level.FINER;
                break;
            case "FINEST":
                levelLogger = Level.FINEST;
                break;
            case "OFF":
                levelLogger = Level.OFF;
                break;
            default:
                System.out.println("Level inconnu :" + lev);
        }
    }

    /**
     * create the logger only once during the first call
     * and then for the other times it retrieves the logger and writes on it
     *
     * @param name name of logger
     */
    public void init(String name) {
        logger = Logger.getLogger(name);
        consoleHandler = new ConsoleHandler();
        nameLogger = name;
        /** By default, the java.util.logging framework uses
         * the JRE_HOME/lib/logging.properties file to configure itself.
         * We must reset the configuration of the log so that the message does not appear 2 times in the console. */
        LogManager.getLogManager().reset();

    }

    /**
     * This class is used to process error messages at the logger level.
     *
     * @param message_error Represents the error message to be assigned to the logger.
     * @param severity      severity Represents the level of the logger.
     */
    public void workJavaLogger(String message_error, Level severity) {
        messageLogJava = "\n== DEBUG JAVA ==\n";
        this.messageLogJava += message_error + "\n";
        this.levelLogger = severity;
        this.workLogger(messageLogJava, "Java");
    }

    /**
     * Created the logger and according to the activated mode we put the error messages in the terminal or in a file.
     * version Is the type of error handling as Java or OpenGl.
     */
    private void workLogger(String messageLog, String version) {
        if (this.activateConsoleMode) {
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(filtreLevel);
            logger.addHandler(consoleHandler);
            logger.setLevel(filtreLevel);
            logger.log(levelLogger, messageLog + "\n");
            logger.removeHandler(logger.getHandlers()[0]);
            consoleHandler.close();
        }
        if (this.activateFileMode) {
            try {
                /** Avoids creating files for messages that are not loggable **/
                if (logger.isLoggable(this.levelLogger)) {
                    /** Allows to create the folder for the log files**/
                    File dir = new File("./messagesLog" + version + "/");
                    dir.mkdirs();
                    fileHandler = new FileHandler("messagesLog" + version + "/" + nameLogger + "" + compteur + "_" + version + ".log");
                    fileHandler.setLevel(filtreLevel);
                    logger.addHandler(fileHandler); /** Associates the file pointer to the logger **/
                    logger.setLevel(filtreLevel);
                    logger.log(levelLogger, messageLog + "\n");
                    logger.removeHandler(logger.getHandlers()[0]);
                    fileHandler.close();
                    compteur++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Allows to redefine the printf method of the PrintStream class to retrieve error messages
     * And processing these messages by the logger in the workLogger () method.
     *
     * @param format format
     * @param args   args
     * @return this this
     */
    public PrintStream printf(String format, Object... args) {

        if (args[0] == "ID") {
            messageLogOpenGL = "== DEBUG OPENGL ==\n";
        }
        messageLogOpenGL += args[0] + ": " + args[1] + "\n";

        if (args[0] == "Severity") {
            choiceLevel("" + args[1]);
        }
        if (args[0] == "Message") {
            workLogger(messageLogOpenGL, "OpenGL");
        }
        return this;
    }

    /**
     * Allow to activate the console mode of Logger.
     */
    public void activateConsoleMode() {
        activateConsoleMode = true;
    }

    /**
     * Allow to desactivate the console mode of Logger.
     */
    public void desActivateConsoleMode() {
        activateConsoleMode = false;
    }

    /**
     * Allow to activate the file mode of Logger.
     */
    public void activateFileMode() {
        activateFileMode = true;
    }

    /**
     * Allow to activate the file mode of Logger.
     */
    public void desActivateFileMode() {
        activateFileMode = false;
    }

    public Logger get_Logger() {
        return logger;
    }

    public String getNameLogger() {
        return nameLogger;
    }

    public void setNameLogger(String nLogger) {
        this.nameLogger = nLogger;
    }

    public void println(String x) {
    }
}