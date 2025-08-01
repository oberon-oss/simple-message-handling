package eu.oberon.oss.tools.i18n.formatter;

/**
 * Generic exception used in the message related classes
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class MessagesException extends RuntimeException {
    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    public MessagesException(String message) {
        super(message);
    }
}
