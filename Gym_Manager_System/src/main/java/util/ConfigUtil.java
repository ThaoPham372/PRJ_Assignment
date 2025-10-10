package util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties PROPS = new Properties();
    static {
        try (InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (Exception ignored) {
        }
    }

    public static String get(String key) {
        return PROPS.getProperty(key);
    }
}


