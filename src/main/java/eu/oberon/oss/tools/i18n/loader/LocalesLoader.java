package eu.oberon.oss.tools.i18n.loader;

import eu.oberon.oss.tools.cc.CountryCodeTable;
import eu.oberon.oss.tools.cc.CountryCodeTableEntry;
import lombok.extern.log4j.Log4j2;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;
import java.util.regex.Matcher;

import static eu.oberon.oss.tools.cc.CountryCodeTableLookupKeys.ISO3166_ALPHA_2;


/**
 * Enables loading of resource bundle properties from a directory
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
@Log4j2
public class LocalesLoader {
    private static final CountryCodeTable COUNTRY_CODE_TABLE;

    static {
        try {
            COUNTRY_CODE_TABLE = CountryCodeTable.getDefaultInstance();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load country code table", e);
        }
    }

    private LocalesLoader() {

    }

    /**
     * Reads the specified directory and creates a set of locales based on the filenames in the directory.
     *
     * @param baseName  The basename of the resource bundle property file(s) in the specified directory
     * @param directory The directory to read the files from.
     *
     * @return A set of Locale(s) for which a representation was found the directory
     *
     * @since 1.0.0
     */
    public static Set<Locale> loadLocales(String baseName, File directory) {

        Set<Locale> locales = new HashSet<>();

        PropertyFileFilter fileFilter = new PropertyFileFilter(baseName);

        final File[] files = directory.listFiles(fileFilter);
        if (files != null) {
            if (files.length == 0) {
                throw new IllegalStateException("No files in directory " + directory);
            }
            for (File file : files) {
                if ((baseName + ".properties").contentEquals(file.getName())) {
                    processData(locales, new String[0], baseName);
                }
                Matcher matcher = fileFilter.getPattern().matcher(file.getName());
                //noinspection ResultOfMethodCallIgnored - we need to invoke the matcher to be able to use it.
                matcher.matches();
                processData(locales, matcher.group(1).split("_"), baseName);
            }
        }
        return locales;
    }

    private static void processData(final Set<Locale> locales, final String[] strings, final String baseName) {
        Builder builder = new Builder();
        switch (strings.length) {
            case 0:
                LOGGER.info("Default property file is present for basename '{}'", baseName);
                break;
            case 1:
                // language
                builder.setLanguage(strings[0]);
                break;
            case 2:
                builder.setLanguage(strings[0]);
                setRegionOrScript(builder, strings[1]);
                break;
            case 3:
                builder.setLanguage(strings[0]);
                setRegionOrScript(builder, strings[1], strings[2]);
                break;
            case 4:
                // language + "_" + script + "_" + country + "_" + variant
                builder.setLanguage(strings[0]);
                builder.setScript(strings[1]);
                builder.setRegion(strings[2]);
                builder.setVariant(strings[3]);
                break;
            default:
                throw new IllegalArgumentException();
        }
        final Locale locale = builder.build();
        locales.add(locale);
    }

    private static void setRegionOrScript(Builder builder, String string) {
        CountryCodeTableEntry entry = COUNTRY_CODE_TABLE.findEntry(string, ISO3166_ALPHA_2);
        if (entry != null) {
            // language + "_" + country
            builder.setRegion(string);
        } else {
            // language + "_" + script
            builder.setScript(string);
        }
    }

    private static void setRegionOrScript(Builder builder, @NotNull String string1, @NotNull String string2) {
        CountryCodeTableEntry entry1 = COUNTRY_CODE_TABLE.findEntry(string1, ISO3166_ALPHA_2);
        CountryCodeTableEntry entry2 = COUNTRY_CODE_TABLE.findEntry(string2, ISO3166_ALPHA_2);
        if (entry1 != null && entry2 != null) {
            throw new IllegalArgumentException(
                    String.format("parameter 1 %s and parameter 2 %s are both ISO3166 Alpha 2 country codes ", string1, string2));
        }

        if (entry1 != null) {
            // language + "_" + country + "_" + variant
            builder.setRegion(string1);
            builder.setVariant(string2);
        } else if (entry2 != null) {
            // language + "_" + script + "_" + country
            builder.setScript(string1);
            builder.setRegion(string2);
        }
    }
}
