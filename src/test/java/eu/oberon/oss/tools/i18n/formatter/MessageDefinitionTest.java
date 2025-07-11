package eu.oberon.oss.tools.i18n.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static eu.oberon.oss.tools.i18n.formatter.FormatStringType.*;
import static org.junit.jupiter.api.Assertions.*;

class MessageDefinitionTest {

    private static Stream<Arguments> formattedStringTest() {
        return Stream.of(
                Arguments.of(STRING_FORMAT, "Hello %% %s!", "Hello % World!", 1, new Object[]{"World"}),
                Arguments.of(STRING_FORMAT, "Hello %s!", "Hello World!", 1, new Object[]{"World"}),
                Arguments.of(LOG_FORMAT, "Hello {}!", "Hello World!", 1, new Object[]{"World"}),
                Arguments.of(TEXT_FORMAT, "Hello {0}!", "Hello World!", 1, new Object[]{"World"}),
                Arguments.of(NON_FORMATTED_STRING, "Hello!", "Hello!", 0, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void formattedStringTest(FormatStringType formatType, String format, String expectedString, int replacementCount, Object[] replacementParameters) {
        MessageDefinition messageDefinition = new MessageDefinition(format);
        assertNotNull(messageDefinition);
        assertEquals(formatType, messageDefinition.getFormatStringType());
        assertEquals(expectedString, messageDefinition.createFormattedMessage(replacementParameters));
        assertEquals(replacementCount, messageDefinition.getReplacementCount());
    }

    private static Stream<Arguments> ambiguousFormatStringTest() {
        return Stream.of(
                Arguments.of("{} %s", true),
                Arguments.of("{0} %s", true),
                Arguments.of("{} {0}", true),
                Arguments.of("{} {0} %s", true),
                Arguments.of("%% %s", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void ambiguousFormatStringTest(String formatString, boolean throwsException) {

        if (throwsException) {
            assertThrows(IllegalArgumentException.class, () -> new MessageDefinition(formatString));
        } else {
            MessageDefinition messageDefinition = new MessageDefinition(formatString);
            assertNotNull(messageDefinition);
        }
    }


    public static Stream<Arguments> incorrectReplacementParametersTest() {
        return Stream.of(
                Arguments.of("Test String {}", null),
                Arguments.of("Test String {}", new Object[]{"1","2"}),
                Arguments.of("Test String {} {} {}", new Object[]{"1","2"}),

                Arguments.of("Test String {0}", null),
                Arguments.of("Test String {0}", new Object[]{"1","2"}),
                Arguments.of("Test String {0} {1} {2}", new Object[]{"1","2"}),

                Arguments.of("Test String %s", null),
                Arguments.of("Test String %s", new Object[]{"1","2"}),
                Arguments.of("Test String %s %s %s", new Object[]{"1","2"})
        );
    }

    @ParameterizedTest
    @MethodSource
    void incorrectReplacementParametersTest(String formatString, Object[] replacementParameters) {
        MessageDefinition definition = new MessageDefinition(formatString);
        Assertions.assertThrows(MessagesException.class, () -> definition.createFormattedMessage(replacementParameters));
    }
}