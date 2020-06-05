package Whist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Property {

    private static Properties properties = new Properties();

    public static void readPropertyFile(String fileName) throws IOException {
        Properties newProperties = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader(fileName);
            newProperties.load(inStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        properties = newProperties;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static int getProperty(String propertyType) {
        //System.out.println(properties.getProperty(propertyType));
        return Integer.parseInt(properties.getProperty(propertyType));
    }

    public static boolean ifPropertyExist(String property){

        return properties.getProperty(property) != null;
    }

}
