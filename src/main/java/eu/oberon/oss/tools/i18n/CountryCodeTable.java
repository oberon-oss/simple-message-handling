package eu.oberon.oss.tools.i18n;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static eu.oberon.oss.tools.i18n.CountryCodeTableLookupKeys.NAME;

/**
 * Allows lookup up country information from a code table.
 */
@Log4j2
public final class CountryCodeTable {

    private static final String DEFAULT_FIELD_SEPARATOR = ";";

    private final Map<CountryCodeTableLookupKeys, Map<String, CountryCodeTableEntry>> lookupMap;

    /**
     * Performs a lookup operation
     */
    public CountryCodeTableEntry findEntry(@NotNull String countryCode, @NotNull CountryCodeTableLookupKeys lookupKey) {
        return lookupMap.get(lookupKey).get(countryCode);
    }

    public int getEntryCount() {
        return lookupMap.get(NAME).size();
    }

    private static CountryCodeTable defaultCountryCodeTable;

    /**
     * Returns the default provided country code table.
     * <p>
     * The default country code table is only loaded once. Multiple calls to this method will yield the same country
     * code table instance
     *
     * @return The default Country code table provided by this class.
     *
     * @since 1.0.0
     */
    public static CountryCodeTable getDefaultInstance() throws IOException {
        if (defaultCountryCodeTable == null) {
            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("country-codes.csv")) {
                defaultCountryCodeTable = new CountryCodeTable(inputStream, DEFAULT_FIELD_SEPARATOR);
            }
        }
        return defaultCountryCodeTable;
    }

    /**
     * Loads a user supplied country table from the specified file.
     *
     * @param fromFile The file to read from.
     *
     * @return The Country code table loaded from the input file
     *
     * @throws IOException if an error occurred opening or reading from the file
     * @since 1.0.0
     */
    public static CountryCodeTable getInstance(File fromFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(fromFile)) {
            return getInstance(inputStream);
        }
    }

    /**
     * Loads a user supplied country table from the specified input stream.
     *
     * @param inputStream The input stream to read from.
     *
     * @return The Country code table loaded from the input stream.
     *
     * @throws IOException if an error occurred reading from the input stream.
     * @since 1.0.0
     */
    public static CountryCodeTable getInstance(InputStream inputStream) throws IOException {
        return new CountryCodeTable(inputStream, DEFAULT_FIELD_SEPARATOR);
    }

    private CountryCodeTable(InputStream inputStream, String fieldSeparator) throws IOException {
        Map<CountryCodeTableLookupKeys, Map<String, CountryCodeTableEntry>> work = new EnumMap<>(CountryCodeTableLookupKeys.class);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] fields = line.split(fieldSeparator);

                if (fields.length < 4) {
                    throw new CountryCodeTableException("Line " + lineCount + " does not have 4 columns");
                } else if (fields.length > 4) {
                    LOGGER.warn("Line {} contains additional ({}) field(s), which will be ignored.", lineCount, fields.length - 4);
                }
                try {
                    addCountryCodeTableEntry(work, new CountryCodeTableEntryImpl(fields[0], fields[1], fields[2], fields[3]));
                } catch (Exception e) {
                    throw new CountryCodeTableException("Error loading table", e);
                }
            }
        }
        lookupMap = Map.copyOf(work);
    }

    private void addCountryCodeTableEntry(Map<CountryCodeTableLookupKeys, Map<String, CountryCodeTableEntry>> work, CountryCodeTableEntry countryCodeTableEntry) {
        for (CountryCodeTableLookupKeys lookupKey : CountryCodeTableLookupKeys.values()) {
            work.putIfAbsent(lookupKey, new HashMap<>());
            String targetLookupKey = switch (lookupKey) {
                case NAME -> countryCodeTableEntry.countryName();
                case ISO3166_ALPHA_2 -> countryCodeTableEntry.iso3166Alpha2Code();
                case ISO3166_ALPHA_3 -> countryCodeTableEntry.iso3166Alpha3Code();
                case UNM49 -> countryCodeTableEntry.unm49Code();
            };
            work.get(lookupKey).putIfAbsent(targetLookupKey, countryCodeTableEntry);
        }
    }
}
