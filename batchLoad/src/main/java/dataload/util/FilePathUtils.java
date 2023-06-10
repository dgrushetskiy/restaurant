package dataload.util;

import io.micrometer.core.instrument.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class FilePathUtils {
    public static String readFileToString(String path, Class aClazz) throws IOException {

        try (InputStream stream = aClazz.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Stream is null");
            }
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }
}