package eu.oberon.oss.tools.i18n;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Locale;

public interface MessageBundleResolver {

    boolean isLocaleAvailable(String laguageTag);

    Locale loadMessageResourceBundle() throws MalformedURLException;

    Locale loadMessageResourceBundle(@NotNull String languageTag);

    Locale loadMessageResourceBundle(@NotNull Locale locale);

    String getString(String key);

    boolean isLocaleAvailable(Locale locale);

    Locale getCurrentLocale();
}
