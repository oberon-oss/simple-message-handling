package eu.oberon.oss.tools.i18n.formatter;

/**
 * An interface for handling messages through logging and exception creation. This interface
 * is designed to provide a unified mechanism for logging parameterized messages and creating
 * exceptions with formatted messages.
 *
 * @param <L> Represents the Logger class to use when logging messages.
 * @param <V> The class type of the Log levels to use.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface MessagesHelper<L, V> extends MessageFormatter {
    /**
     * Returns the message definition used by a helper instance.
     *
     * @return The message definition.
     *
     * @since 1.0.0
     */
    MessageDefinition getMessageDefinition();

    /**
     * Logs a message using the provided logger with optional parameters for formatting.
     * The message format, logging level, and behavior depend on the implementation and
     * the message definition associated with the instance.
     *
     * @param logger the logger used to log the message
     * @param params optional parameters to be included in the formatted message
     *
     * @since 1.0.0
     */
    void logMessage(L logger, Object... params);

    /**
     * Logs a message using the provided logger with optional parameters for formatting, overriding the loglevel defined
     * The message format, logging level, and behavior depend on the implementation and
     * the message definition associated with the instance.
     *
     * @param logger        the logger used to log the message
     * @param overrideLevel the log level message to override the default log level
     * @param params        optional parameters to be included in the formatted message
     *
     * @since 1.0.0
     */
    void logMessageWithLevelOverride(L logger, V overrideLevel, Object... params);

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
    Exception createExceptionFullParameters(Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                                            Object... params);
}
