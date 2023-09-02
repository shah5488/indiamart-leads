package com.hywings.indiamartleads.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class FileUtility {

    /**
     * @param path
     * @return This method is used to get Properties by file path
     */
    public synchronized static Properties getProperties(String path) {

        File file = new File(path);
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            properties.load(fileInputStream);

        } catch (Exception e) {
        }

        return properties;

    }

    /**
     * @param properties
     * @param path       This method is used to store properties in file
     */
    public synchronized static void storeProperties(Properties properties, String path) {

        File file = new File(path);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            properties.store(fileOutputStream, "updating file");

        } catch (Exception e) {
        }

    }
}
