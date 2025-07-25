package eu.oberon.oss.tools.i18n.formatter;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enumerates the possible types of message formats
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public enum FormatStringType {

    /**
     * Represents a message format type where the string is used as-is without any formatting or replacements.
     * This is typically used for messages that do not contain placeholders for dynamic content.
     */
    NON_FORMATTED_STRING(Pattern.compile(".*")),
    /**
     * Represents a message format type where the string contains placeholders
     * that follow the formatting conventions of {@link String#format}.
     * <p>
     * Placeholders in this format typically use syntax like `%s`, `%d`, etc.,
     * enabling dynamic content insertion at runtime during logging operations.
     * <p>
     * Placeholders are replaced with dynamic content during runtime.
     * Typically used when messages need to support parameterized formatting.
     *
     * @since 1.0.0
     */
    STRING_FORMAT(Pattern.compile("%(?:[a-z]|%\\d+(?:\\.\\d+)?[a-z])")),

    /**
     * Represents a message format type where the string contains placeholders
     * compatible with the format used by logging frameworks, such as in Log4j.
     * <p>
     * Represents a message format type where the string contains placeholders
     * compatible with the format used by logging frameworks, such as in Log4j.
     * <p>
     * Parameter replacements are specified by '{}'.
     * This format is commonly used for structured log messages that support
     * parameterized values.
     *
     * @since 1.0.0
     */
    LOG_FORMAT(Pattern.compile("\\{}")),

    /**
     * Defines the formatting handled by the {@link java.text.MessageFormat} formatter.
     * This formatter uses replacement strings like {0...n}. This format is distinctly different from the
     * {@link #LOG_FORMAT}, that uses unnumbered replacement placeholders.
     *
     * @since 1.0.0
     */
    TEXT_FORMAT(Pattern.compile("\\{\\d+}"));

    @Getter
    private final Pattern pattern;

    FormatStringType(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Analyzes the given formatting string against the format pattern.
     *
     * @param format The format string to by analyzed.
     *
     * @return The number of replacement/substitution elements where found in the format string.
     *
     * @since 1.0.0 
     */
    public int analyze(String format) {
        Matcher logMatcher = pattern.matcher(format);
        int replacementCount = 0;

        while (logMatcher.find()) {
            replacementCount++;
        }

        return replacementCount;
    }
}
