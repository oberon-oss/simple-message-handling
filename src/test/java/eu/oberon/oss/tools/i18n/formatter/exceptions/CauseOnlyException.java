package eu.oberon.oss.tools.i18n.formatter.exceptions;

public class CauseOnlyException extends RuntimeException {
    public CauseOnlyException(Throwable cause) {
        super(cause);
    }
}
