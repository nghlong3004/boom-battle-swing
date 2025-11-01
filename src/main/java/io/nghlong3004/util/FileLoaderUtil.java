package io.nghlong3004.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class FileLoaderUtil {

    public static int[][] loadFileMapData(String filePath) {
        try (InputStream inputStream = FileLoaderUtil.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("File" + filePath + " not found!");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String raw = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                return parse2D(raw);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[][] parse2D(String raw) {
        return raw.lines()
                  .map(line -> Arrays.stream(line.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray())
                  .toArray(int[][]::new);
    }

}
