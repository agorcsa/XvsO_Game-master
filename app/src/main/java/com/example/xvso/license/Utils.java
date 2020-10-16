package com.example.xvso.license;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String readFileFromAssets(Context context, String fileName) {
        String content = "";
        try {
            InputStream stream = context.getAssets().open(fileName);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            content = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
