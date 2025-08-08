package eu.oberon.oss.tools.i18n;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Handles loading of resource bundles inside of project jar/war/ear file.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class InternalMessageBundleResolver implements MessageBundleResolver {

    private final String baseName;
    private ResourceBundle resourceBundle;

    /**
     * Creates a new resolver for the provided basename and locale.
     *
     * @param baseName The basename of the resource bundle to load
     * @param locale   The locale to select for the resource bundle
     *
     * @throws MissingResourceException if the specified locale can not be loaded.
     * @since 1.0.0
     */
    public InternalMessageBundleResolver(@NotNull String baseName, @NotNull Locale locale) {
        this.baseName = baseName;
        loadMessageResourceBundle(locale);
    }

    /**
     * Loads the specified basename locale's default properties
     *
     * @param baseName The basename of the resource bundle to load
     *
     * @throws MissingResourceException if the specified locale can not be loaded.
     * @since 1.0.0
     */
    public InternalMessageBundleResolver(@NotNull String baseName) {
        this.baseName = baseName;
        loadDefaultProperties();
    }

    @Override
    public void loadDefaultProperties() {
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.ROOT);
    }

    @Override
    public boolean isLocaleAvailable(String languageTag) {
        return isLocaleAvailable(Locale.forLanguageTag(languageTag));
    }

    @Override
    public boolean isLocaleAvailable(Locale locale) {
        return ResourceBundle.getBundle(baseName, locale).getLocale().equals(locale);
    }

    @Override
    public Locale loadMessageResourceBundleForDefaultLocale() {
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
        return resourceBundle.getLocale();
    }

    @Override
    public Locale loadMessageResourceBundle(@NotNull String languageTag) {
        return loadMessageResourceBundle(Locale.forLanguageTag(languageTag));
    }

    @Override
    public Locale loadMessageResourceBundle(@NotNull Locale locale) {
        resourceBundle = ResourceBundle.getBundle(baseName, locale);
        return resourceBundle.getLocale();
    }

    @Override
    public Locale getCurrentActiveLocale() {
        return resourceBundle.getLocale();
    }

    @Override
    public String getString(@NotNull String key) {
        return resourceBundle.getString(key);
    }
}
