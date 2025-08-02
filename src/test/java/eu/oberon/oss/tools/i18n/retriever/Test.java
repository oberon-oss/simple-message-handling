package eu.oberon.oss.tools.i18n.retriever;

import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

public enum Test implements MessageKeyRetriever<Test, String> {
    T1("some.key"),
    T2("some.key", Level.INFO),
    T3("some.key", TestException1.class),
    T4("some.key", Level.WARN, TestException2.class);

    private final String messageKey;
    private Class<? extends Exception> exceptionClass = null;
    private Level level = null;

    Test(String messageKey) {
        this.messageKey = messageKey;
    }

    Test(String messageKey, Level level) {
        this.messageKey = messageKey;
        this.level = level;
    }

    Test(String messageKey, Class<TestException1> exceptionClass) {
        this.messageKey = messageKey;
        this.exceptionClass = exceptionClass;
    }

    Test(String messageKey, Level level, Class<TestException2> exceptionClass) {
        this.exceptionClass = exceptionClass;
        this.messageKey = messageKey;
        this.level = level;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public @Nullable Level getLevel() {
        return level;
    }

    @Override
    public @Nullable Class<? extends Exception> getException() {
        return exceptionClass;
    }


}
