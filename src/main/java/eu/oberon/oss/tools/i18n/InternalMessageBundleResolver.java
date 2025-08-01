package eu.oberon.oss.tools.i18n;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

public class InternalMessageBundleResolver implements MessageBundleResolver {

    private final String baseName;
    private ResourceBundle resourceBundle;

    public InternalMessageBundleResolver(@NotNull String baseName, @NotNull Locale locale)  {
        this.baseName = baseName;
        loadMessageResourceBundle(locale);
    }

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
