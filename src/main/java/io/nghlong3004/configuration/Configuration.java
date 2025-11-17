package io.nghlong3004.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Configuration {

    private final Properties properties;
    private static final String RESOURCE_PATH = "/configuration.properties";

    public static Configuration getInstance() {
        return Holder.INSTANCE;
    }

    private Configuration() {
        properties = new Properties();
        try (InputStream inputStream = Configuration.class.getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                String msg = RESOURCE_PATH + " not found in resource folder";
                log.error(msg);
                throw new IOException(msg);
            }
            properties.load(inputStream);
            log.debug("Read properties from {}", RESOURCE_PATH);
        } catch (IOException e) {
            log.error("{} error close file: message {}", RESOURCE_PATH, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getFps() {
        return Integer.parseInt(getPropertyValue("game.fps"));
    }

    public int getUps() {
        return Integer.parseInt(getPropertyValue("game.ups"));
    }

    public int getOriginalTileSize() {
        return Integer.parseInt(getPropertyValue("game.original_tile_size"));
    }

    public int getMaxScreenColumn() {
        return Integer.parseInt(getPropertyValue("game.max_screen_column"));
    }

    public int getMaxScreenRow() {
        return Integer.parseInt(getPropertyValue("game.max_screen_row"));
    }

    public float getScale() {
        return Float.parseFloat(getPropertyValue("game.scale"));
    }

    public String getServerUrl(int serverNumber) {
        String key = "server.%d.url".formatted(serverNumber);
        return getPropertyValue(key);
    }

    public int getServerCount() {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            String url = properties.getProperty("server." + i + ".url");
            if (url != null && !url.isBlank()) {
                count++;
            }
            else {
                break;
            }
        }
        return count > 0 ? count : 3;
    }

    private String getPropertyValue(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            value = "";
            log.warn("Missing property: {} ", key);
        }
        return value;
    }

    private static class Holder {
        private static final Configuration INSTANCE = new Configuration();
    }

}
