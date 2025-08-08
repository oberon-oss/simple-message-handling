package eu.oberon.oss.tools.i18n.retriever;

import eu.oberon.oss.tools.i18n.MessageBundleResolver;
import eu.oberon.oss.tools.i18n.formatter.MessagesHelper;
import eu.oberon.oss.tools.i18n.formatter.MessagesHelperImpl;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the creation of strings or exceptions, or simple message formatting.
 *
 * @param <O> An enum instance that implements the {@link MessageDetailProvider} interface.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class MessageProcessor<O extends Enum<O> & MessageDetailProvider<Level, O, String>> {
    private final Map<O, MessagesHelper<Logger, Level>> processedMessages = new ConcurrentHashMap<>();
    private final MessageBundleResolver resolver;

    @Getter
    private final Locale locale;

    /**
     * Creates a message processor using the provided {@link MessageBundleResolver}.
     * <p>
     * The default locale will be attempted to be loaded.
     *
     * @param resolver The resolver to be used
     *
     * @since 1.0.0
     */
    public MessageProcessor(MessageBundleResolver resolver) {
        this.resolver = resolver;
        resolver.loadDefaultProperties();
        this.locale = resolver.getCurrentActiveLocale();
    }

    /**
     * Creates a message processor using the specified resolver and locale.
     *
     * @param resolver The resolver to be used
     * @param locale   The locale to load for the resolver.
     *
     * @since 1.0.0
     */
    public MessageProcessor(MessageBundleResolver resolver, Locale locale) {
        this.resolver = resolver;
        resolver.loadMessageResourceBundle(locale);
        this.locale = resolver.getCurrentActiveLocale();
    }

    private MessagesHelper<Logger, Level> getOrCreateMessageHelper(O enumEntry) {
        if (!processedMessages.containsKey(enumEntry)) {
            MessagesHelper<Logger, Level> helper;
            String formatString = resolver.getString(enumEntry.getMessageKey());
            if (enumEntry.getExceptionClass() != null && enumEntry.getLogLevel() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getLogLevel(), enumEntry.getExceptionClass());
            } else if (enumEntry.getExceptionClass() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getExceptionClass());
            } else if (enumEntry.getLogLevel() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getLogLevel());
            } else {
                helper = new MessagesHelperImpl(formatString);
            }
            processedMessages.put(enumEntry, helper);
        }
        return processedMessages.get(enumEntry);
    }

    /**
     * Logs a message to a logger.
     *
     * @param enumEntry    A value from the enum that will provided message details.
     * @param logger       The logger to write the created message to
     * @param replacements the replacement parameters to be applied while creating the message text.
     *
     * @since 1.0.0
     */
    public void logMessage(O enumEntry, Logger logger, Object... replacements) {
        getOrCreateMessageHelper(enumEntry).logMessage(logger, replacements);
    }

    /**
     * Logs a message to a logger, overriding the default loglevel of the log message.
     *
     * @param enumEntry    A value from the enum that will provided message details.
     * @param logger       The logger to write the created message to
     * @param logLevel     The loglevel to write the message at. This will override the message default log level
     * @param replacements the replacement parameters to be applied while creating the message text.
     *
     * @since 1.0.0
     */
    public void logMessageWithLevelOverride(O enumEntry, Logger logger, Level logLevel, Object... replacements) {
        getOrCreateMessageHelper(enumEntry).logMessageWithLevelOverride(logger, logLevel, replacements);
    }

    /**
     * Creates a basic exception.
     *
     * @param enumEntry    A value from the enum that will provided message details.
     * @param replacements the replacement parameters to be applied while creating the message text.
     *
     * @return The created exception
     *
     * @since 1.0.0
     */
    public Exception createExceptionWithMessage(O enumEntry, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionWithMessage(replacements);
    }

    /**
     * Creates an exception with message and cause.
     *
     * @param enumEntry    A value from the enum that will provided message details.
     * @param cause        A {@link Throwable} that is the cause for this exception to be created
     * @param replacements the replacement parameters to be applied while creating the message text.
     *
     * @return The created exception
     *
     * @since 1.0.0
     */
    public Exception createExceptionWithCause(O enumEntry, Throwable cause, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionWithCause(cause, replacements);
    }

    /**
     * Creates an exception with the common parameters for an {@link Exception}
     *
     * @param enumEntry          A value from the enum that will provided message details.
     * @param cause              A {@link Throwable} that is the cause for this exception to be created
     * @param enableSuppression  whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     * @param replacements       the replacement parameters to be applied while creating the message text.
     *
     * @return The created exception
     *
     * @since 1.0.0
     */
    public Exception createExceptionWithFullParameters(O enumEntry, Throwable cause, boolean enableSuppression,
                                                       boolean writableStackTrace,
                                                       Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionFullParameters(cause, enableSuppression, writableStackTrace,
                replacements);
    }

    /**
     * Performs basic message formatting.
     *
     * @param enumEntry    A value from the enum that will provided message details.
     * @param replacements the replacement parameters to be applied while creating the message text.
     *
     * @return The formatted message string.
     *
     * @since 1.0.0
     */
    public String formatMessage(O enumEntry, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createFormattedMessage(replacements);
    }

}
