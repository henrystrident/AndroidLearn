package com.bjfu.fungus.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通过拍照时的日期和事件生成照片路径
 */
public class GeneratePhotoPath {

    public static String getName() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

}
