package eu.oberon.oss.tools.i18n.formatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static eu.oberon.oss.tools.i18n.formatter.FormatStringType.STRING_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatStringTypeTest {

    private static final String SUPPLEMENTAL_DATE_FORMAT_SPECIFIERS = "HIklMSLNPZzsQBbAaCYyjmdeRTrDFc";
    private static final Random random = new SecureRandom();

    static Stream<Arguments> testStringFormat() {

        List<Arguments> list = new ArrayList<>(Arrays.asList(
                // Argument placeholders
                Arguments.of("first %1$d, second %2$d, third %3$d", true, 3),
                Arguments.of("first %1$d, second %2$d, third %6$d", true, 6),
                Arguments.of("first %1$d, second %2$d, third %<d", true, 2),
                Arguments.of("first %1$d, second %1$d, third %<d", true, 1),
                Arguments.of("first %1$d, second %<d, third %<d", true, 1),
                // Flags
                Arguments.of("first %#d", true, 1),
                Arguments.of("first %#d", true, 1),
                Arguments.of("first %+d", true, 1),
                Arguments.of("first % d", true, 1),
                Arguments.of("first %0d", true, 1),
                Arguments.of("first %,d", true, 1),
                Arguments.of("first %(d", true, 1),
                // Conversions - non replacements
                Arguments.of("first %%d", true, 0),
                Arguments.of("first %nd", true, 0),
                // Conversions - Basics
                Arguments.of("first %B, seconds %b", true, 2),
                Arguments.of("first %H, seconds %h", true, 2),
                Arguments.of("first %S, seconds %a", true, 2),
                Arguments.of("first %C, seconds %c", true, 2),
                Arguments.of("first %X, seconds %x", true, 2),
                Arguments.of("first %G, seconds %g", true, 2),
                Arguments.of("first %A, seconds %a", true, 2),
                // Conversions - Numbers
                Arguments.of("first %d", true, 1),
                Arguments.of("first %o", true, 1),
                Arguments.of("first %f", true, 1),
                // Conversions - Date/Time
                Arguments.of(createDateTimeSpecifier(true), true, 1),
                Arguments.of(createDateTimeSpecifier(false), true, 1),
                // Combinations
                Arguments.of("first %f#", true, 1),
                Arguments.of("first %3d", true, 1),
                Arguments.of("first %-5.2f", true, 1),
                Arguments.of("first %+2.2f", true, 1),
                Arguments.of("first % 8.2f", true, 1),
                Arguments.of("first %05.4f", true, 1),
                Arguments.of("first %,18.6f", true, 1),
                Arguments.of("first %,18d", true, 1),
                Arguments.of("first %(7.2f", true, 1),

                // incorrect format specifiers
                Arguments.of("#%2$", false, 0),
                Arguments.of("%$1", false, 0),
                Arguments.of("#%<", false, 0),
                Arguments.of("#%2$", false, 0),
                Arguments.of("%$1", false, 0),
                Arguments.of("#%<", false, 0)
        ));


        return list.stream();
    }

    @ParameterizedTest
    @MethodSource
    void testStringFormat(String formatString, boolean expectedValid, int expectedReplacementCount) {
        if (expectedValid) {
            int actualReplacementCount = STRING_FORMAT.analyze(formatString);
            assertEquals(expectedReplacementCount, actualReplacementCount);
        }
    }

    private static String createDateTimeSpecifier(boolean upperCase) {
        StringBuilder result = new StringBuilder("first %").append(upperCase ? 'T' : 't');
        for (int i = 0; i < random.nextInt(1,SUPPLEMENTAL_DATE_FORMAT_SPECIFIERS.length()); i++) {
            result.append(SUPPLEMENTAL_DATE_FORMAT_SPECIFIERS.charAt(random.nextInt(SUPPLEMENTAL_DATE_FORMAT_SPECIFIERS.length())));
        }
        return result.toString();
    }
}