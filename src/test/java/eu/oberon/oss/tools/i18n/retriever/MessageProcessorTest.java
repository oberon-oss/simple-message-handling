package eu.oberon.oss.tools.i18n.retriever;

import eu.oberon.oss.tools.i18n.InternalMessageBundleResolver;
import eu.oberon.oss.tools.i18n.MessageBundleResolver;
import lombok.extern.log4j.Log4j2;
import nl.altindag.log.LogCaptor;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Locale;
import java.util.stream.Stream;

import static eu.oberon.oss.tools.i18n.retriever.Test.*;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class MessageProcessorTest {
    private static final String EM_DT = "The key=test.key, the value=test.value (default)";
    private static final String EM_FR = "The key=test.key, the value=test.value (fr)";
    private static final String EM_NL = "The key=test.key, the value=test.value (nl)";
    private static final String EM_DE = "The key=test.key, the value=test.value (de)";
    private static final String EM_EN = "The key=test.key, the value=test.value (en)";

    private static final String LT_DT = "";
    private static final String LT_FR = "fr";
    private static final String LT_NL = "nl";
    private static final String LT_DE = "de";
    private static final String LT_EN = "en";

    private MessageBundleResolver resolver;
    private MessageProcessor<Test> messageProcessor;

    private final LogCaptor logCaptor = LogCaptor.forClass(MessageProcessorTest.class);

    static Stream<Arguments> testMessageProcessorSimpleMessage() {
        return Stream.of(
                Arguments.of(LT_DT, T1, EM_DT),
                Arguments.of(LT_EN, T1, EM_EN),
                Arguments.of(LT_NL, T1, EM_NL),
                Arguments.of(LT_DE, T1, EM_DE),
                Arguments.of(LT_FR, T1, EM_FR)
        );
    }


    static Stream<Arguments> testMessageProcessorException() {
        return Stream.of(
                Arguments.of(LT_DT, T3, EM_DT, TestException1.class),
                Arguments.of(LT_EN, T3, EM_EN, TestException1.class),
                Arguments.of(LT_NL, T3, EM_NL, TestException1.class),
                Arguments.of(LT_DE, T3, EM_DE, TestException1.class),
                Arguments.of(LT_FR, T3, EM_FR, TestException1.class)
        );
    }

    static Stream<Arguments> testMessageProcessorLogging() {
        return Stream.of(
                Arguments.of(LT_DT, T2, EM_DT, Level.TRACE),
                Arguments.of(LT_EN, T2, EM_EN, Level.TRACE),
                Arguments.of(LT_NL, T2, EM_NL, Level.TRACE),
                Arguments.of(LT_DE, T2, EM_DE, Level.TRACE),
                Arguments.of(LT_FR, T2, EM_FR, Level.TRACE)
        );
    }

    static Stream<Arguments> testMessageProcessorForLogAndException() {
        return Stream.of(
                Arguments.of(LT_DT, T4, EM_DT, Level.WARN, TestException2.class),
                Arguments.of(LT_EN, T4, EM_EN, Level.WARN, TestException2.class),
                Arguments.of(LT_NL, T4, EM_NL, Level.WARN, TestException2.class),
                Arguments.of(LT_DE, T4, EM_DE, Level.WARN, TestException2.class),
                Arguments.of(LT_FR, T4, EM_FR, Level.WARN, TestException2.class)
        );
    }


    @BeforeEach
    void setUp() {
        resolver = new InternalMessageBundleResolver("test-bundles/message-processor-bundle");
        messageProcessor = new MessageProcessor<>(resolver);
    }

    @ParameterizedTest
    @MethodSource
    void testMessageProcessorSimpleMessage(String languageTag, Test enumValue, String expectedMessage) {
        assertNotNull(resolver);
        resolver.loadMessageResourceBundle(languageTag);
        String formatMessage = messageProcessor.formatMessage(enumValue, "test.key", "test.value");

        assertFalse(formatMessage.isEmpty());
        assertEquals(expectedMessage, formatMessage);
    }

    @ParameterizedTest
    @MethodSource
    void testMessageProcessorException(String languageTag, Test enumValue, String expectedMessage,
                                       Class<? extends Exception> expectedExceptionClass) {
        assertNotNull(resolver);
        resolver.loadMessageResourceBundle(languageTag);
        Exception cause = new IOException("cause");

        Exception exception;
        // Test message only

        exception = messageProcessor.createExceptionWithMessage(enumValue, "test.key", "test.value");
        assertEquals(exception.getMessage(), expectedMessage);
        assertEquals(exception.getClass(), expectedExceptionClass);

        // Test with message and cause.
        exception = messageProcessor.createExceptionWithCause(enumValue, cause, "test.key", "test.value");
        assertEquals(cause, exception.getCause());
        assertEquals(expectedMessage, exception.getMessage());

        // Test with message, cause and flags
        exception = messageProcessor.createExceptionWithFullParameters(enumValue, cause, true, false, "test.key", "test.value");
        assertEquals(cause, exception.getCause());
        assertEquals(expectedMessage, exception.getMessage());

        // Test exception class matches
        assertEquals(enumValue.getException(), expectedExceptionClass);
    }

    @ParameterizedTest
    @MethodSource
    void testMessageProcessorLogging(String languageTag, Test enumValue, String expectedMessage, Level logLevel) {
        MessageBundleResolver tempResolver = new InternalMessageBundleResolver("test-bundles/message-processor-bundle");
        assertNotNull(tempResolver);

        MessageProcessor<Test> tmpMessageProcessor = new MessageProcessor<>(tempResolver, Locale.forLanguageTag(languageTag));
        tmpMessageProcessor.logMessage(enumValue, LOGGER, "test.key", "test.value");
        assertTrue(logCaptor.getLogs().contains(expectedMessage));
    }


    @ParameterizedTest
    @MethodSource
    void testMessageProcessorForLogAndException(String languageTag, Test enumValue, String expectedMessage, Level logLevel,
                                                Class<? extends Exception> expectedExceptionClass) {
        assertNotNull(resolver);
        resolver.loadMessageResourceBundle(languageTag);

        messageProcessor.logMessage(enumValue, LOGGER, "test.key", "test.value");
        assertTrue(logCaptor.getLogs().contains(expectedMessage));

        Exception exception = messageProcessor.createExceptionWithMessage(enumValue, "test.key", "test.value");
        assertEquals(exception.getMessage(), expectedMessage);
        assertEquals(exception.getClass(), expectedExceptionClass);
    }
}