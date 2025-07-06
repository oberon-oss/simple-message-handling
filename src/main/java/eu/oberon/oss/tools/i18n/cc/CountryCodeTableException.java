package eu.oberon.oss.tools.i18n.cc;

/**
 * Exception used by country code table code
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class CountryCodeTableException extends RuntimeException {
    public CountryCodeTableException(String message) {
        super(message);
    }

    public CountryCodeTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
