package eu.oberon.oss.tools.i18n.formatter;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

/**
 * A default implementation for the {@link MessagesHelper} interface.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
@Log4j2
public class MessagesHelperImpl implements MessagesHelper {
    @Getter
    private final MessageDefinition messageDefinition;
    @Getter
    private final @Nullable String exceptionClassName;
    @Getter
    private final @Nullable Level logLevel;

    private @Nullable Constructor<? extends Exception> standardConstructor = null;
    private @Nullable Constructor<? extends Exception> throwableException = null;
    private @Nullable Constructor<? extends Exception> fullConstructor = null;

    /**
     * Creates a message formater for basic messages.
     *
     * @param formatString The formatting string to use.
     *
     * @since 1.0.0
     */
    public MessagesHelperImpl(String formatString) {
        this(formatString, null, null);
    }

    /**
     * Creates a message formater for messages that are intended for logging.
     *
     * @param formatString The formatting string to use.
     * @param logLevel     the default loglevel to use when logging the message.
     *
     * @since 1.0.0
     */
    public MessagesHelperImpl(String formatString, Level logLevel) {
        this(formatString, null, logLevel);
    }

    /**
     * Creates a message formater intended for exception creation.
     *
     * @param formatString   The formatting string to use.
     * @param exceptionClass the exception class to use when creating an actual exception instance.
     *
     * @since 1.0.0
     */
    public MessagesHelperImpl(String formatString, Class<? extends Exception> exceptionClass) {
        this(formatString, exceptionClass, null);
    }

    private MessagesHelperImpl(String formatString, @Nullable Class<? extends Exception> exceptionClass, @Nullable Level logLevel) {
        if (exceptionClass != null && Exception.class.isAssignableFrom(exceptionClass)) {
            standardConstructor = createConstructor(exceptionClass, String.class);
            throwableException = createConstructor(exceptionClass, String.class, Throwable.class);
            fullConstructor = createConstructor(exceptionClass, String.class, Throwable.class, boolean.class, boolean.class);
            exceptionClassName = exceptionClass.getCanonicalName();
            if (standardConstructor == null && throwableException == null && fullConstructor == null) {
                throw new MessagesException(exceptionClassName + " does not have any suitable constructor.");
            }
        } else {
            exceptionClassName = null;
        }
        this.logLevel = logLevel;
        messageDefinition = new MessageDefinition(formatString);
    }

    private static Constructor<? extends Exception> createConstructor(Class<? extends Exception> exceptionClass, Class<?>... parameterTypes) {
        try {
            return exceptionClass.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            StringBuilder builder = new StringBuilder("Exception class has no constructor: ")
                    .append(exceptionClass.getName()
                    );
            for (Class<?> parameterType : parameterTypes) {
                builder.append(", ").append(parameterType.getName());
            }
            LOGGER.info("{}", builder);
        }
        return null;
    }

    @Override
    public String createFormattedMessage(@Nullable Object... replacementParameters) {
        return messageDefinition.createFormattedMessage(replacementParameters);
    }

    @Override
    @SuppressWarnings("java:S2629")
    public void logMessage(@NotNull Logger logger, @Nullable Object... params) {
        if (logLevel == null) {
            throw new MessagesException("Message is NOT a loggable message.");
        }
        logger.log(logLevel, messageDefinition.createFormattedMessage(params));
    }

    @Override
    public Exception createExceptionWithMessage(Object... params) {
        if (standardConstructor == null) {
            throw new MessagesException("Message is not to be used in an exception.");
        }
        try {
            return standardConstructor.newInstance(messageDefinition.createFormattedMessage(params));
        } catch (Exception e) {
            throw new MessagesException("Failure instantiating exception: {}");
        }
    }

    @Override
    public Exception createExceptionWithCause(Throwable cause, Object... params) {
        if (throwableException == null) {
            throw new MessagesException(exceptionClassName + ".getConstructor(String,Throwable) does not exist.");
        }
        try {
            return throwableException.newInstance(createFormattedMessage(params), cause);
        } catch (Exception e) {
            throw new MessagesException("Failed to create instance of exception: " + exceptionClassName);
        }
    }

    @Override
    public Exception createExceptionFullParameters(Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        if (fullConstructor == null) {
            throw new MessagesException(exceptionClassName + ".getConstructor(String,Throwable,boolean,boolean) does not exist.");
        }
        try {
            return fullConstructor.newInstance(createFormattedMessage(params), cause, enableSuppression, writableStackTrace);
        } catch (Exception e) {
            throw new MessagesException("Failed to create instance of exception: " + exceptionClassName);
        }
    }
}
