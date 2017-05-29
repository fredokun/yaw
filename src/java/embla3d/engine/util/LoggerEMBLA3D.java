package embla3d.engine.util;

import org.lwjgl.opengl.GLUtil;
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
 * logger.fine("error");
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


    /**
     * Get instance
     *
     * @return the singleton
     */
    public static LoggerEMBLA3D getInstance() {
        return instance;
    }

    public static void logOpenGl() {
        GLUtil.setupDebugMessageCallback(instance);
    }

    /**
     * set the debug level of the logger
     *
     * @param filtreLev filtreLev Allows to filter the messages according to the level of the messages
     */
    public static void setLevelDebug(Level filtreLev) {
        logger.setLevel(filtreLev);
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * level of the current message
     */
    private Level mLevelLogger;
    /**
     * mNameLogger : must be formatted -> Package.subPackage.Class (Advice for clarity)
     */
    private String mNameLogger;
    private ConsoleHandler mConsoleHandler;
    private FileHandler mFileHandler;
    /**
     * For OpenGL debugging messages
     **/
    private String messageLogOpenGL = "";

    private LoggerEMBLA3D(OutputStream out) {
        super(out);
        /** Allows to initialize the internal logger  */
        init("loggerEMBLA3D");
    }

    @Override
    public void println(String x) {
        messageLogOpenGL = x + "\n";
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
        if (args[0] == "Severity") {
            choiceLevel((String) args[1]);
        }
        if (args[0] == "Message") {
            logger.log(mLevelLogger, (String) args[1]);
        }
        return this;
    }

    /**
     * Allows to choose the level of the message to be allocated in the logger.
     *
     * @param lev lev level of logger Chosen by the developer.
     */
    public void choiceLevel(String lev) {
        switch (lev) {

            case "ALL":
                mLevelLogger = Level.ALL;
                break;
            case "NOTIFICATION":
                mLevelLogger = Level.INFO;
                break;
            case "HIGH":
                mLevelLogger = Level.SEVERE;
                break;
            case "MEDIUM":
                mLevelLogger = Level.WARNING;
                break;
            case "LOW":
                mLevelLogger = Level.INFO;
                break;
            case "SEVERE":
                mLevelLogger = Level.SEVERE;
                break;
            case "WARNING":
                mLevelLogger = Level.WARNING;
                break;
            case "INFO":
                mLevelLogger = Level.INFO;
                break;
            case "CONFIG":
                mLevelLogger = Level.CONFIG;
                break;
            case "FINE":
                mLevelLogger = Level.FINE;
                break;
            case "FINER":
                mLevelLogger = Level.FINER;
                break;
            case "FINEST":
                mLevelLogger = Level.FINEST;
                break;
            case "OFF":
                mLevelLogger = Level.OFF;
                break;
            default:
                logger.warning("Unknow Level:" + lev);
        }
    }

    /**
     * create the logger only once during the first call
     * and then for the other times it retrieves the logger and writes on it
     *
     * @param pName name of logger
     */
    public void init(String pName) {
        logger = Logger.getLogger(pName);
        mConsoleHandler = new ConsoleHandler();
        mNameLogger = pName;
        File mLogDir = new File("./messagesLog");
        if (!mLogDir.exists()) {
            mLogDir.mkdir();
        }
        try {
            mFileHandler = new FileHandler("messagesLog/" + System.currentTimeMillis() + ".log");
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        LogManager.getLogManager().reset();
    }

    /**
     * Allow to activate the console mode of Logger.
     */
    public void activateConsoleMode() {
        logger.addHandler(mConsoleHandler);
    }

    /**
     * Allow to desactivate the console mode of Logger.
     */
    public void desactivateConsoleMode() {
        logger.removeHandler(mConsoleHandler);
    }

    /**
     * Allow to activate the file mode of Logger.
     */
    public void activateFileMode() {
        logger.addHandler(mFileHandler);
    }

    /**
     * Allow to activate the file mode of Logger.
     */
    public void desactivateFileMode() {
        logger.removeHandler(mFileHandler);
    }

    public String getNameLogger() {
        return mNameLogger;
    }

    public void setNameLogger(String nLogger) {
        this.mNameLogger = nLogger;
    }
}