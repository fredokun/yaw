package embla3d.engine;

import org.lwjgl.system.APIUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class represents our logger it inherited from PrintStream to be used for debug opengl .
 */

public class LoggerEMBLA3D extends PrintStream {

    /**
     * Create a log with the name proposed on the first call, it will be retrieved but not recreated on future calls
     */
    private static LoggerEMBLA3D instance = new LoggerEMBLA3D(APIUtil.DEBUG_STREAM);
    private static Logger logger;
    private Level levelLogger;
    private boolean activateConsoleMode = false;
    private boolean activateFileMode = false;
    /**
     * nameLogger : must be formatted -> Package.subPackage.Class (Advice for clarity)
     * Here we can add _java or _opengl to differentiate the 2.
     */
    private String nameLogger;
    private Handler fh;

    private String message_logJAVA = "";
    private String message_logOPENGL = "";

    private LoggerEMBLA3D(OutputStream out) {
        super(out);
        init("loggerEMBLA3D");
    }

    public static LoggerEMBLA3D getInstance() {
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

    public void init(String name) {
        logger = Logger.getLogger(name); /** create the logger only once during the first call
         * and then for the other times it retrieves the logger and writes on it*/
    }

    /**
     * This class is used to process error messages at the logger level.
     *
     * @param message_error Represents the error message to be assigned to the logger.
     * @param namelog       namelog Represents the name of the logger.
     * @param severity      severity Represents the level of the logger.
     */
    public void work_java_Logger(String message_error, String namelog, Level severity) {
        this.setNameLogger(namelog);
        message_logJAVA = "\n== DEBUG JAVA ==\n";
        this.message_logJAVA += message_error;
        //choiceLevel(severity);
        this.levelLogger = severity;
        this.workLogger(message_logJAVA);
    }

    /**
     * Created the logger and according to the activated mode we put the error messages in the terminal or in a file.
     */
    private void workLogger(String message_log) {

        // LogManager lgM= LogManager.getLogManager();
        //lgM.addLogger(logger);
        //lgM.toString();
        if (this.activateConsoleMode) {
            // fh = new ConsoleHandler(); TODO TO DELETE
            // logger.addHandler(fh); /**TODO DELETE, Associates the file pointer to the logger **/.
            logger.log(levelLogger, message_log + "\n");

        }
        if (this.activateFileMode) {
            try {
                fh = new FileHandler(nameLogger + ".log");
                logger.addHandler(fh); /** Associates the file pointer to the logger **/
                logger.log(levelLogger, message_log);

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
     * @return this
     */
    public PrintStream printf(String format, Object... args) {
        //GL.createCapabilities(); //TODO a enlever ??????????????


        if (args[0] == "ID") {
            message_logOPENGL = "== DEBUG OPENGL ==\n";
        }
        message_logOPENGL += args[0] + ": " + args[1] + "\n";

        if (args[0] == "Severity") {
            // System.out.println("MESS"+message_log);
            choiceLevel("" + args[1]);
        }
        if (args[0] == "Message") {
            workLogger(message_logOPENGL);
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

    public void setLevel(Level lev) {
        //choiceLevel(lev);
        // System.out.println(this.levelLogger);
        logger.setLevel(lev);
    }

    public void println(String x) {
    }
}