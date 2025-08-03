package eu.oberon.oss.tools.i18n.formatter;

import org.jetbrains.annotations.Nullable;

/**
 * Contract for basic message formatting.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface MessageFormatter {
    /**
     * Returns a formatted string based on the specified format type and replacement parameters.
     * If the format type is STRING_FORMAT, the method utilizes {@link String#format}.
     * If the format type is LOG_FORMAT, it uses a logging-specific formatting style.
     * For non-formatted strings, the original format string is returned as-is.
     *
     * @param replacementParameters the values to replace placeholders in the format string.
     *
     * @return the formatted string with replacements applied, or the original format string for non-formatted types.
     *
     * @throws IllegalArgumentException if the replacement parameters are null, or their count does not match the
     *                                  expected count.
     * @since 1.0.0
     */
    String createFormattedMessage(@Nullable Object... replacementParameters);
}
