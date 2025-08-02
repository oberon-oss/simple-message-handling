package eu.oberon.oss.tools.i18n.retriever;


import org.jetbrains.annotations.Nullable;

/**
 * Interface provided by enumerations that provide information required for creating formatted or logging messages, or exceptions.
 *
 * @param <V> Represents the class containing the log levels required for log messages.
 * @param <E> The enumeration that implements this interface
 * @param <O> The type object representing the message key.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface MessageDetailProvider<V, E extends Enum<E> & MessageDetailProvider<V, E, O>, O> {

    /**
     * Provides access to an enum instance that contains the message detail information.
     *
     * @return The enum instance.
     *
     * @since 1.0.0
     */
    O getMessageKey();

    /**
     * Returns the log level to be used, when creating a log message.
     *
     * @return The log level to be used when logging messages, or {@literal <null>}
     *
     * @since 1.0.0
     */
    @Nullable V getLogLevel();

    /**
     * Returns the canonical name of the exception class used for creating an {@link Exception} instance,
     * if the messsage instance supports the creation of an exception.
     *
     * @return The name of the exception class, or {@literal <null>} if the message cannot be used to create exceptions.
     *
     * @since 1.0.0
     */
    @Nullable Class<? extends Exception> getExceptionClass();
}
