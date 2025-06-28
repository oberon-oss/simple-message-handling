package eu.oberon.oss.tools.i18n;

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

@Log4j2
public class MessageBundleResolverImpl implements MessageBundleResolver {

    private ResourceBundle resourceBundle;

    @Getter
    private final String baseName;

    @Getter
    private final File directory;

    @Getter
    private Locale currentLocale;

    private final URLClassLoader loader;

    private final Set<Locale> locales;

    public MessageBundleResolverImpl(@NotNull String baseName, @NotNull File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }

        if (!directory.canRead()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable");
        }

        loader = new URLClassLoader(new URL[]{directory.toURI().toURL()});

        this.baseName = baseName;
        this.directory = directory;
        this.locales = Set.copyOf(LocalesLoader.loadLocales(this.baseName, this.directory));
        loadMessageResourceBundle();
    }

    @Override
    public boolean isLocaleAvailable(Locale locale) {
        return locales.contains(locale);
    }

    public void loadDefaultProperties() {
        loadMessageResourceBundle(Locale.ROOT);
    }

    @Override
    public boolean isLocaleAvailable(String languageTag) {
        return isLocaleAvailable(Locale.forLanguageTag(languageTag));
    }

    @Override
    public Locale loadMessageResourceBundle() {
        return loadMessageResourceBundle(Locale.getDefault());
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
        currentLocale = resourceBundle.getLocale();
        return currentLocale;
    }

    @Override
    public String getString(String key) {
        return resourceBundle.getString(key);
    }

}
