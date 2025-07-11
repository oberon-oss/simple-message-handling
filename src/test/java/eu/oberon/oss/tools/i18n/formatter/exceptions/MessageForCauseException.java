package eu.oberon.oss.tools.i18n.formatter.exceptions;

public class MessageForCauseException extends Exception {
    public MessageForCauseException(String message, Throwable cause) {
        super(message,cause);
    }
}
