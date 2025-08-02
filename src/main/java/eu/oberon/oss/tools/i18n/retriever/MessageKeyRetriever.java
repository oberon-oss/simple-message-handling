package eu.oberon.oss.tools.i18n.retriever;


import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

public interface MessageKeyRetriever<E extends Enum<E> & MessageKeyRetriever<E,O>, O> {
    O getMessageKey();
    @Nullable Level getLevel();
    @Nullable Class<? extends Exception> getException();
}
