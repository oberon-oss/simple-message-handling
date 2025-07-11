package eu.oberon.oss.tools.i18n.formatter.exceptions;

public class MessageOnlyException extends RuntimeException {
    public MessageOnlyException(String message) {
        super(message);
    }
}
