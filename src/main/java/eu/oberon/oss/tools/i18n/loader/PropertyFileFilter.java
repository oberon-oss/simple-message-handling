package eu.oberon.oss.tools.i18n.loader;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

@Log4j2
class PropertyFileFilter implements FileFilter {
    @Getter
    private final Pattern pattern;

    PropertyFileFilter(String baseName) {
        pattern = Pattern.compile(baseName + "_?(.*?)" + "\\.properties");
    }

    @Override
    public boolean accept(File pathname) {
        boolean match = pattern.matcher(pathname.getName()).matches();
        LOGGER.info("File '{}': {} for pattern '{}'", pathname, match ? "Matches" : "Doesn't match", pattern);
        return pattern.matcher(pathname.getName()).matches();
    }
}
