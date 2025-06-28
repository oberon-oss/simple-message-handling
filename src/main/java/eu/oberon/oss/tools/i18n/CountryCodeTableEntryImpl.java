package eu.oberon.oss.tools.i18n;

import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

@Log4j2
public record CountryCodeTableEntryImpl(String countryName, String iso3166Alpha2Code, String iso3166Alpha3Code,
                                        String unm49Code) implements CountryCodeTableEntry {

    public CountryCodeTableEntryImpl {
        boolean errorFound = false;

        if (countryName.isBlank()) {
            LOGGER.warn("Country name cannot be blank");
            errorFound = true;
        }

        if (!Pattern.matches("[A-Z]{2}", iso3166Alpha2Code)) {
            LOGGER.warn("Invalid ISO 3166 Alpha 2 code: '{}'", iso3166Alpha2Code);
            errorFound = true;
        }

        if (!Pattern.matches("[A-Z]{3}", iso3166Alpha3Code)) {
            LOGGER.warn("Invalid ISO 3166 Alpha 3 code: '{}'", iso3166Alpha3Code);
            errorFound = true;
        }

        if (!Pattern.matches("\\d{3}", unm49Code)) {
            LOGGER.warn("Invalid UNM49 code: '{}'", unm49Code);
            errorFound = true;
        }

        if (errorFound) {
            throw new IllegalStateException("1 or more errors found");
        }
    }
}
