package eu.oberon.oss.tools.i18n;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Contract for classes that act as message bundle resolvers
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface MessageBundleResolver {

    /**
     * Loads the default properties for the resource bundle.
     * NB: note that default here refers to the value {@link Locale#ROOT}, not the default locale that is in
     * effect on the system where the call is being made. For loading the default locale, you can use the
     * {@link #loadMessageResourceBundleForDefaultLocale()} method.
     *
     * @since 1.0.0
     */
    void loadDefaultProperties();

    /**
     * Returns a flag indicating of a locale for the
     *
     * @param languageTag a string representing the language tag of the {@link Locale} to check for.
     *
     * @return <b>True</b> if the locale is available, or <b>false</b> if it is not.
     *
     * @since 1.0.0
     */
    boolean isLocaleAvailable(String languageTag);

    /**
     * Returns a flag indicating of a locale for the
     *
     * @param locale the actual {@link Locale} to check for.
     *
     * @return <b>True</b> if the locale is available, or <b>false</b> if it is not.
     *
     * @since 1.0.0
     */
    boolean isLocaleAvailable(Locale locale);

    /**
     * Attempts to load the resource bundle for the default locale that is in effect on the system where the call is
     * being made.
     *
     * @return The locale that was loaded.
     *
     * @since 1.0.0
     */
    Locale loadMessageResourceBundleForDefaultLocale();

    /**
     * Attempts to load the locale specified by the language tag.
     *
     * @param languageTag The language tag of the locale to load.
     *
     * @return The actual loaded locale.
     */
    Locale loadMessageResourceBundle(@NotNull String languageTag);

    /**
     * Attempts to load the locale specified by locale.
     *
     * @param locale The locale to load
     *
     * @return The actual loaded locale.
     */
    Locale loadMessageResourceBundle(@NotNull Locale locale);

    /**
     * Returns the currently active locale.
     *
     * @return The locale currently in use/active.
     *
     * @since 1.0.0
     */
    Locale getCurrentActiveLocale();

    /**
     * Returns the value associated with the specified key from the resource bundle.
     *
     * @param key The key to look for.
     *
     * @return The value associated with the key
     *
     * @throws MissingResourceException if the key was not found in the resource bundle.
     * @since 1.0.0
     */
    String getString(@NotNull String key);
}
