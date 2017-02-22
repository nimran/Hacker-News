package info.hackernews.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A static class which takes care of
 * storing any form of data throughout the session
 * which can be used to cache the data to
 * avoid unnecessary service calls during same session
 */

public class AppStack {

    private static Map<String, Object> mapValues = new HashMap<>();

    public Object getStackValue(String key) {
        return mapValues.get(key);
    }

    public String getStackString(String key) {
        String val = null;
        Object obj = mapValues.get(key);
        if(obj instanceof String) {
            val = obj.toString();
        }
        return val;
    }

    public Boolean getStackBoolean(String key) {
        Boolean val = false;
        Object obj = mapValues.get(key);
        if(obj instanceof Boolean) {
            val = (Boolean) obj;
        }
        return val;
    }

    public int getStackInt(String key) {
        int val = 0;
        Object obj = mapValues.get(key);
        if(obj instanceof Integer) {
            val = (int) obj;
        }
        return val;
    }



    public void setStackValue(String key, Object stackValue) {
        mapValues.put(key, stackValue);
    }
}

