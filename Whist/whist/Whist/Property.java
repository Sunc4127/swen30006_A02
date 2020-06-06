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

    /**get int property*/
    public static int getProperty(String propertyType) {
        //System.out.println(properties.getProperty(propertyType));
        return Integer.parseInt(properties.getProperty(propertyType));
    }

    /** get the property with the type of int array*/
    public static int[] getPropertyArray(String property){
        String propertyString = properties.getProperty(property);
        String[] propertyStringArray = propertyString.substring(1,propertyString.length()-1).split(",");

        int[] propertyArray = new int[propertyStringArray.length];

        for(int i = 0; i< propertyStringArray.length; i++){
            propertyArray[i] = Integer.parseInt(propertyStringArray[i]);
        }

        return propertyArray;
    }

    /** if a property exists in properties file*/
    public static boolean ifPropertyExist(String property){
        return properties.getProperty(property) != null;
    }


}

