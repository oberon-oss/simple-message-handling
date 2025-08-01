package eu.oberon.oss.tools.i18n;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InternalMessageBundleResolverTest {
    static final String BUNDLE_NAME = "test-bundles/test-bundle";


    @Test
    void testDefaultConstructorWithNonExistentResourceBundle() {
        assertThrows(MissingResourceException.class, () -> new InternalMessageBundleResolver("does-not-exist"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"en", "de", "fr", "nl"})
    void testConstructorForLanguageTags(String languageTag) {
        assertDoesNotThrow(() -> new InternalMessageBundleResolver(BUNDLE_NAME, Locale.forLanguageTag(languageTag)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"en", "de", "fr", "nl"})
    void testConstructorForLanguageTagAvailability(@NotNull String languageTag) {
        InternalMessageBundleResolver resolver = new InternalMessageBundleResolver(BUNDLE_NAME);
        assertTrue(resolver.isLocaleAvailable(languageTag));
    }

    @Test
    void testDefaultProperties() {
        InternalMessageBundleResolver resolver = new InternalMessageBundleResolver(BUNDLE_NAME);
        assertDoesNotThrow(resolver::loadDefaultProperties);
        assertEquals(Locale.ROOT, resolver.getCurrentActiveLocale());
    }


    public static Stream<Arguments> testMessageKeys() {
        return Stream.of(
                Arguments.of("en","test-key-1", "test value 1 - en"),
                Arguments.of("de","test-key-1", "test value 1 - de"),
                Arguments.of("fr","test-key-1", "test value 1 - fr"),
                Arguments.of("nl","test-key-1", "test value 1 - nl"),
                Arguments.of("","test-key-1", "test value 1")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testMessageKeys(String languageTag,String key,String value) {
        InternalMessageBundleResolver resolver = new InternalMessageBundleResolver(BUNDLE_NAME);
        resolver.loadMessageResourceBundle(languageTag);
        assertEquals(value,resolver.getString(key));
    }

    @Test
    void testForDefaultLocale() {
        Locale.setDefault(Locale.forLanguageTag("nl"));
        InternalMessageBundleResolver resolver = new InternalMessageBundleResolver(BUNDLE_NAME);
        resolver.loadMessageResourceBundleForDefaultLocale();
        assertEquals("test value 1 - nl",resolver.getString("test-key-1"));
    }
}