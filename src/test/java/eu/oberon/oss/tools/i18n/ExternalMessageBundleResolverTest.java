package eu.oberon.oss.tools.i18n;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExternalMessageBundleResolverTest {

    private MessageBundleResolver resolver;

    @BeforeEach
    void setUp() throws IOException {
        resolver = new ExternalMessageBundleResolver("Default",
                new File("src/test/resources/set-1/"));
    }

    public static Stream<Arguments> testLocales() {
        return Stream.of(
                Arguments.of(true, Locale.forLanguageTag("nl-NL"), "nl", "key1a", "NL_nl standaard key1 waarde", "nl waarde voor taal"),
                Arguments.of(true, Locale.forLanguageTag("nl"), "nl", "key1", "NL standaard key1 waarde", null),
                Arguments.of(true, Locale.forLanguageTag("en"), "en", "key1", "en key1 value", null),
                Arguments.of(false, Locale.forLanguageTag("fr"), Locale.ROOT.getLanguage(), "key1", "default key1 value", null)
        );
    }

    @Test
    void testLocaleLoadingMethods() {
        Locale.setDefault(Locale.ENGLISH);

        Locale locale1 = resolver.loadMessageResourceBundleForDefaultLocale();
        Locale locale2 = resolver.loadMessageResourceBundle(Locale.ENGLISH);
        Locale locale3 = resolver.loadMessageResourceBundle(Locale.ENGLISH.toLanguageTag());
        assertNotNull(locale1);
        assertNotNull(locale2);
        assertNotNull(locale3);
        assertEquals(locale1, locale2);
        assertEquals(locale2, locale3);
    }

    @ParameterizedTest
    @MethodSource
    void testLocales(boolean isAvailable, Locale locale, String language, String key, String bundleValue, String languageValue) {
        resolver.loadMessageResourceBundle(locale);
        assertEquals(isAvailable, resolver.isLocaleAvailable(locale));
        assertEquals(isAvailable, resolver.isLocaleAvailable(locale.toLanguageTag()));
        assertEquals(language, resolver.getCurrentActiveLocale().getLanguage());
        assertEquals(bundleValue, resolver.getString(key));

        resolver.loadMessageResourceBundle(language);
        assertEquals(isAvailable, resolver.isLocaleAvailable(locale));
        assertEquals(language, resolver.getCurrentActiveLocale().getLanguage());
        assertEquals(languageValue == null ? bundleValue : languageValue, resolver.getString(key));

        assertEquals("Only exists as default.", resolver.getString("key2"));
    }

    @Test
    void currentLocaleTest() {
        Locale currentLocale = resolver.getCurrentActiveLocale();
        assertNotNull(currentLocale);
        assertEquals(Locale.getDefault().getLanguage(), currentLocale.getLanguage());
        assertNotNull(resolver.getString("key1"));
    }

    @Test
    void testDefaultLocale() {
        resolver.loadDefaultProperties();
        Locale locale1 = resolver.getCurrentActiveLocale();
        Locale locale2 = resolver.loadMessageResourceBundle(Locale.ROOT);
        assertEquals(locale1, locale2);
    }

    @Test
    void testWithNonExistingDirectory() {
        Path path = Paths.get("src/test/resources/does-not-exist");
        File file = new File("src/test/resources/does-not-exist");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ExternalMessageBundleResolver("Default", file)
        );
        assertEquals("'" + path + "' does not exist", exception.getMessage());
    }

    @Test
    void testWithEmptyDirectory() {
        Path path = Paths.get("src/test/resources/set-2");
        File file = new File("src/test/resources/set-2");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new ExternalMessageBundleResolver("Default", file)
        );
        assertEquals("No files in directory " + path, exception.getMessage());
    }

    @Test
    void testWithNonDirectoryEntry() {
        Path path = Paths.get("src/test/resources/log4j2-test.xml");
        File file = new File("src/test/resources/log4j2-test.xml");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ExternalMessageBundleResolver("Default", file)
        );
        assertEquals("'" + path + "' is not a directory", exception.getMessage());
    }

}