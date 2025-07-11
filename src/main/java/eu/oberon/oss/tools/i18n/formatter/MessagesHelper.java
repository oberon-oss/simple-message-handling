package eu.oberon.oss.tools.i18n.formatter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for handling messages through logging and exception creation. This interface
 * is designed to provide a unified mechanism for logging parameterized messages and creating
 * exceptions with formatted messages.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface MessagesHelper extends MessageFormatter {
    /**
     * Returns the message definition used by a helper instance.
     *
     * @return The message definition.
     *
     * @since 1.0.0
     */
    MessageDefinition getMessageDefinition();

    /**
     * Returns the canonical name of the exception class used by this helper when calling
     * {@link #createExceptionWithMessage(Object...)} (Object...)},
     * if the messsage instance supports the creation of an exception.
     *
     * @return The name of the exception class, or {@literal <null>} if the message cannot be used to create exceptions.
     *
     * @since 1.0.0
     */
    @Nullable String getExceptionClassName();

    /**
     * Returns the log level being used, when the {@link #logMessage(Logger, Object...)} method is called.
     *
     * @return The log level to be used when logging messages, or {@literal <null>}
     */
    @Nullable Level getLogLevel();

    /**
     * Logs a message using the provided logger with optional parameters for formatting.
     * The message format, logging level, and behavior depend on the implementation and
     * the message definition associated with the instance.
     *
     * @param logger the logger used to log the message
     * @param params optional parameters to be included in the formatted message
     */
    void logMessage(Logger logger, Object... params);

    /**
     * Creates an exception using the specified parameters to format the message.
     * The exception's type and behavior are determined by the underlying implementation.
     *
     * @param params the parameters used to format the exception message
     *
     * @return an instance of an Exception subclass with a formatted message
     *
     * @throws IllegalStateException if the exception type is not defined, or the message is not intended for exception
     *                               creation
     * @throws MessagesException     will be thrown when the exception creation runs into an error.*
     * @since 1.0.0
     */
    Exception createExceptionWithMessage(Object... params);

    /**
     * @param cause  The exception that was the reason for the raising for this exception
     * @param params the parameters used to format the exception message
     *
     * @return an instance of an Exception subclass with a formatted message
     *
     * @throws IllegalStateException if the exception type is not defined, or the message is not intended for exception
     *                               creation
     * @throws MessagesException     will be thrown when the exception creation runs into an error, or if the target
     *                               exception
     */
    Exception createExceptionWithCause(Throwable cause, Object... params);

    /**
     * @param cause              The exception that was the reason for the raising for this exception
     * @param enableSuppression  whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     * @param params             the parameters used to format the exception message
     *
     * @return an instance of an Exception subclass with a formatted message
     *
     * @throws IllegalStateException if the exception type is not defined, or the message is not intended for exception
     *                               creation
     * @throws MessagesException     will be thrown when the exception creation runs into an error.*
     */
    Exception createExceptionFullParameters(Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params);
}
