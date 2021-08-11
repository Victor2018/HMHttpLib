package com.victor.hm.library.data;

import com.victor.hm.library.util.TextUtils;

import java.io.File;

public class FormInfo {
    public File file;
    public String fileKey;
    public String fileType;

    public static String getPictureType (String path) {
        String type = "image/jpeg";
        if (TextUtils.isEmpty(path)) {
            return type;
        }
        return type;
    }
}
