package log;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * 产生日志
 * @author Qinger
 *
 */
public class Log {
	
	static Logger log = Logger.getLogger(Log.class.getName());
	
    public static void logDebug(String message) {
			log.debug(message);
	}

	public static void logInfo(String message) {
			log.info(message);
	}

	public static void logInfo(Throwable t)
	{
		StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        log.info(buffer.toString());
	}
}

