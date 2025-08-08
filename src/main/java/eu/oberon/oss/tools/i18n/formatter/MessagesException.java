package eu.oberon.oss.tools.i18n.formatter;

/**
 * Generic exception used in the message related classes
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class MessagesException extends RuntimeException {
    /**
     * Creates a basic message exception.
     *
     * @param message The message for the exception.
     *
     * @since 1.0.0
     */
    public MessagesException(String message) {
        super(message);
    }
}
