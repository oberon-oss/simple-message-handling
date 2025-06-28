package eu.oberon.oss.tools.i18n;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageBundleResolverImplTest {

    private MessageBundleResolver resolver;

    @BeforeEach
    void setUp() throws IOException {
        resolver = new MessageBundleResolverImpl("Default",
                                                 new File("src/test/resources/set1/"));
    }

    public static Stream<Arguments> testLocales() {
        return Stream.of(
                Arguments.of(true,Locale.forLanguageTag("nl-NL"),"nl","key1a","NL_nl standaard key1 waarde","nl waarde voor taal"),
                Arguments.of(true,Locale.forLanguageTag("nl"),"nl","key1","NL standaard key1 waarde",null),
                Arguments.of(true,Locale.forLanguageTag("en"),"en","key1","en key1 value",null),
                Arguments.of(false,Locale.forLanguageTag("fr"),Locale.ROOT.getLanguage(),"key1","default key1 value",null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testLocales(boolean isAvailable, Locale locale, String language, String key, String bundleValue, String languageValue) {
        resolver.loadMessageResourceBundle(locale);
        assertEquals(isAvailable,resolver.isLocaleAvailable(locale));
        assertEquals(language, resolver.getCurrentLocale().getLanguage());
        assertEquals(bundleValue, resolver.getString(key));

        resolver.loadMessageResourceBundle(language);
        assertEquals(isAvailable,resolver.isLocaleAvailable(locale));
        assertEquals(language, resolver.getCurrentLocale().getLanguage());
        assertEquals(languageValue == null ? bundleValue : languageValue, resolver.getString(key));

        assertEquals("Only exists as default.", resolver.getString("key2"));
    }

    @Test
    void defaultLocalTest() {
        Locale currentLocale = resolver.getCurrentLocale();
        assertNotNull(currentLocale);
        assertEquals(Locale.getDefault().getLanguage(), currentLocale.getLanguage());
        assertNotNull(resolver.getString("key1"));
    }
}