package easyny.ludashen.com.easyny.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Tool {
    public static boolean saveFile(Context context,String fielName, String info) {
        try {
            FileOutputStream fos = context.openFileOutput(fielName, Context.MODE_PRIVATE);
            fos.write(info.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readinfo(Context context,String path) {
        try {
            FileInputStream fis = context.openFileInput(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String temp = reader.readLine();
            return temp;
        } catch (Exception e) {
            return null;
        }
    }
    public static void deleteFile(Context context,String path){
        File file=context.getFileStreamPath(path);
        if (file.exists())
            file.delete();
    }
    public static Boolean fileExists(Context context,String path){
        File file=context.getFileStreamPath(path);
        return file.exists();
    }

}