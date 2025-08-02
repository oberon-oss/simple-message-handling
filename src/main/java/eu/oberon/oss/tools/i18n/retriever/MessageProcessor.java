package eu.oberon.oss.tools.i18n.retriever;

import eu.oberon.oss.tools.i18n.MessageBundleResolver;
import eu.oberon.oss.tools.i18n.formatter.MessagesHelper;
import eu.oberon.oss.tools.i18n.formatter.MessagesHelperImpl;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageProcessor<O extends Enum<O> & MessageKeyRetriever<O, String>> {
    private final Map<O, MessagesHelper> processedMessages = new ConcurrentHashMap<>();
    private final MessageBundleResolver resolver;

    @Getter
    private final Locale locale;

    public MessageProcessor(MessageBundleResolver resolver) {
        this.resolver = resolver;
        resolver.loadDefaultProperties();
        this.locale = resolver.getCurrentActiveLocale();
    }

    public MessageProcessor(MessageBundleResolver resolver, Locale locale) {
        this.resolver = resolver;
        resolver.loadMessageResourceBundle(locale);
        this.locale = resolver.getCurrentActiveLocale();
    }

    private MessagesHelper getOrCreateMessageHelper(O enumEntry) {
        if (!processedMessages.containsKey(enumEntry)) {
            MessagesHelper helper;
            String formatString = resolver.getString(enumEntry.getMessageKey());
            if (enumEntry.getException() != null && enumEntry.getLevel() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getLevel(), enumEntry.getException());
            } else if (enumEntry.getException() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getException());
            } else if (enumEntry.getLevel() != null) {
                helper = new MessagesHelperImpl(formatString, enumEntry.getLevel());
            } else {
                helper = new MessagesHelperImpl(formatString);
            }
            processedMessages.put(enumEntry, helper);
        }
        return processedMessages.get(enumEntry);
    }

    public void logMessage(O enumEntry, Logger logger, Object... replacements) {
        getOrCreateMessageHelper(enumEntry).logMessage(logger, replacements);
    }

    public Exception createExceptionWithMessage(O enumEntry, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionWithMessage(replacements);
    }

    public Exception createExceptionWithCause(O enumEntry, Throwable cause, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionWithCause(cause, replacements);
    }

    public Exception createExceptionWithFullParameters(O enumEntry, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                                                       Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createExceptionFullParameters(cause, enableSuppression, writableStackTrace, replacements);
    }

    public String formatMessage(O enumEntry, Object... replacements) {
        return getOrCreateMessageHelper(enumEntry).createFormattedMessage(replacements);
    }

}
