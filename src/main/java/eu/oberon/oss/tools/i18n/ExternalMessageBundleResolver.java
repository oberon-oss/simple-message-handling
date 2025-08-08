package eu.oberon.oss.tools.i18n;

import eu.oberon.oss.tools.i18n.loader.LocalesLoader;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Implementation of the {@link MessageBundleResolver} interface that allows loading resource bundles from
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
@Log4j2
public class ExternalMessageBundleResolver implements MessageBundleResolver {

    private ResourceBundle resourceBundle;

    @Getter
    private final String baseName;

    @Getter
    private final File directory;

    @Getter
    private Locale currentActiveLocale;

    private final URLClassLoader loader;

    private final Set<Locale> locales;

    /**
     * Loads the resource bundle date with the specified base name from the provided directory.
     *
     * @param baseName  The basename of the resource bundle.
     * @param directory The directory to load from.
     *
     * @throws IOException              for errors accessing or loading the data.
     * @throws IllegalArgumentException if the specified directory does not exist, is not a directory or cannot be read
     *                                  from.
     */
    public ExternalMessageBundleResolver(@NotNull String baseName, @NotNull File directory) throws IOException {
        if (!directory.exists()) {
            throw new IllegalArgumentException("'" + directory + "' does not exist");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("'" + directory + "' is not a directory");
        }

        if (!directory.canRead()) {
            throw new IllegalArgumentException("'" + directory + "' is not readable");
        }

        loader = new URLClassLoader(new URL[]{directory.toURI().toURL()});

        this.baseName = baseName;
        this.directory = directory;
        this.locales = Set.copyOf(LocalesLoader.loadLocales(this.baseName, this.directory));
        loadMessageResourceBundleForDefaultLocale();
    }

    @Override
    public boolean isLocaleAvailable(Locale locale) {
        return locales.contains(locale);
    }

    @Override
    public void loadDefaultProperties() {
        loadMessageResourceBundle(Locale.ROOT);
    }

    @Override
    public Locale loadMessageResourceBundleForDefaultLocale() {
        return loadMessageResourceBundle(Locale.getDefault());
    }

    @Override
    public boolean isLocaleAvailable(String languageTag) {
        return isLocaleAvailable(Locale.forLanguageTag(languageTag));
    }

    @Override
    public Locale loadMessageResourceBundle(@NotNull String languageTag) {
        return loadMessageResourceBundle(Locale.forLanguageTag(languageTag));
    }

    @Override
    public Locale loadMessageResourceBundle(@NotNull Locale locale) {
        if (locales.contains(locale)) {
            resourceBundle = ResourceBundle.getBundle(baseName, locale, loader);
        } else if (locales.contains(Locale.forLanguageTag(locale.getLanguage()))) {
            resourceBundle = ResourceBundle.getBundle(baseName, Locale.forLanguageTag(locale.getLanguage()), loader);
        } else {
            resourceBundle = ResourceBundle.getBundle(baseName, Locale.ROOT, loader);
            LOGGER.warn("Locale '{}' is not available, reverted to default bundle content", locale);
        }
        currentActiveLocale = resourceBundle.getLocale();
        return currentActiveLocale;
    }

    @Override
    public String getString(@NotNull String key) {
        return resourceBundle.getString(key);
    }

}
