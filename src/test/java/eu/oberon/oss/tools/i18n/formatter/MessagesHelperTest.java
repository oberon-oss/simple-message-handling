package eu.oberon.oss.tools.i18n.formatter;

import eu.oberon.oss.tools.i18n.formatter.exceptions.CauseOnlyException;
import eu.oberon.oss.tools.i18n.formatter.exceptions.MessageForCauseException;
import eu.oberon.oss.tools.i18n.formatter.exceptions.MessageFullConstructionException;
import eu.oberon.oss.tools.i18n.formatter.exceptions.MessageOnlyException;
import lombok.extern.log4j.Log4j2;
import nl.altindag.log.LogCaptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static eu.oberon.oss.tools.i18n.formatter.FormatStringType.STRING_FORMAT;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"resource", "ThrowableNotThrown"})
@Log4j2
class MessagesHelperTest {
    private static final String TEMPLATE = "Example non-log/non-exception message %s";

    @Test
    void testBasicFormatter() {
        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE);
        assertNotNull(helper);

        assertEquals(STRING_FORMAT, helper.getMessageDefinition().getFormatStringType());
        String message = helper.createFormattedMessage("test");
        assertEquals("Example non-log/non-exception message test", message);
    }

    @Test
    void testLogMessageFormatter() {
        Logger testLogger = LogManager.getLogger(MessagesHelperImpl.class);
        LogCaptor logCaptor = LogCaptor.forClass(MessagesHelperImpl.class);

        MessagesHelper<Logger, Level> helper = new MessagesHelperImpl(TEMPLATE, Level.INFO);
        helper.logMessage(testLogger, "test");
        assertTrue(logCaptor.getLogs().contains("Example non-log/non-exception message test"));
    }

    @Test
    void testNonLoggerMessageFormatter() {
        Logger testLogger = LogManager.getLogger(MessagesHelperImpl.class);
        MessagesHelper<Logger, Level> helper = new MessagesHelperImpl(TEMPLATE);
        assertThrows(MessagesException.class, () -> helper.logMessage(testLogger, "test"));
    }

    @Test
    void testLoggerWithMoreSpecificLogLevel() {
        Logger testLogger = LogManager.getLogger(MessagesHelperImpl.class);
        LogCaptor logCaptor = LogCaptor.forClass(MessagesHelperImpl.class);

        MessagesHelper<Logger, Level> helper = new MessagesHelperImpl(TEMPLATE, Level.TRACE);
        helper.logMessage(testLogger, "test");
        assertFalse(logCaptor.getLogs().contains("Example non-log/non-exception message test"));
    }

    @Test
    void testMessageOnlyExceptionFormatter() {
        MessagesHelper<Logger, Level> helper = new MessagesHelperImpl(TEMPLATE, MessageOnlyException.class);
        assertNotNull(helper);
        Exception exception = helper.createExceptionWithMessage("test");
        assertEquals("Example non-log/non-exception message test", exception.getMessage());
    }

    @Test
    void testMessageForCauseExceptionFormatter() {
        MessagesHelper<Logger, Level> helper = new MessagesHelperImpl(TEMPLATE, MessageForCauseException.class);
        assertNotNull(helper);
        Exception cause = new IOException("io exception");
        Exception exception = helper.createExceptionWithCause(cause, "test");
        assertEquals("Example non-log/non-exception message test", exception.getMessage());
    }

    @Test
    void testMessageFullConstructorExceptionFormatter() {
        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE, MessageFullConstructionException.class);
        Exception cause = new IOException("io exception");
        Exception exception = helper.createExceptionFullParameters(cause, true, true, "test");
        assertEquals("Example non-log/non-exception message test", exception.getMessage());
    }

    @Test
    void testForMessageWithoutValidConstructor() {
        assertThrows(MessagesException.class, () -> new MessagesHelperImpl(TEMPLATE, CauseOnlyException.class));
    }

    @Test
    void testNotStringConstructor() {
        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE, MessageFullConstructionException.class);
        assertThrows(MessagesException.class, () -> helper.createExceptionWithMessage("test"));
    }

    @Test
    void testNotCauseConstructor() {
        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE, MessageFullConstructionException.class);
        Exception cause = new IOException("io exception");
        assertThrows(MessagesException.class, () -> helper.createExceptionWithCause(cause, "test"));
    }

    @Test
    void testNotFullConstructor() {
        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE, MessageOnlyException.class);
        Exception cause = new IOException("io exception");
        assertThrows(MessagesException.class, () -> helper.createExceptionFullParameters(cause, true, true, "test"));
    }

    @Test
    void testAllConstructorsWithInvalidNumberOfReplacementVariables() {
        IOException exception = new IOException("io exception");

        MessagesHelper<Logger,Level> helper = new MessagesHelperImpl(TEMPLATE, MessageOnlyException.class);
        assertThrows(MessagesException.class, helper::createExceptionWithMessage);

        MessagesHelper<Logger,Level> helper2 = new MessagesHelperImpl(TEMPLATE, MessageForCauseException.class);
        assertThrows(MessagesException.class, () -> helper2.createExceptionWithCause(exception));

        MessagesHelper<Logger,Level> helper3 = new MessagesHelperImpl(TEMPLATE, MessageFullConstructionException.class);
        assertThrows(MessagesException.class, () -> helper3.createExceptionFullParameters(exception, true, true));
    }
}