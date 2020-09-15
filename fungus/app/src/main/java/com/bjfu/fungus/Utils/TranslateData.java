package com.bjfu.fungus.Utils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * 提供各种数据转化功能
 */
public class TranslateData {
    public static HashMap<String,String> byteToMap(byte[] msgObj)
    {
        HashMap<String,String> result = new HashMap<>();
        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msgObj);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            result = (HashMap<String, String>) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
