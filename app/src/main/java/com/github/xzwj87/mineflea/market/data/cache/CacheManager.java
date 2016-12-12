package com.github.xzwj87.mineflea.market.data.cache;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jason on 11/5/16.
 */

@Singleton
public class CacheManager {
    private static final String TAG = CacheManager.class.getSimpleName();

    private static Map<File,Long> sCacheTime = new HashMap<>();

    @Inject
    public CacheManager(){}


    public boolean exist(File file){
        if(file == null) return false;

        return file.exists();
    }

    public void writeToFile(File file, String content,boolean isUpdate){
        if(file == null || content == null) return;

        if(isUpdate){
            updateFileContent(file,content);
            return;
        }

        if(!file.exists()){
            //file.mkdir();
            setCachedTime(file);
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void updateFileContent(File file,String content){
        if(file == null || content == null) return;

        setCachedTime(file);

        try {
            FileWriter writer = new FileWriter(file,false);
            writer.write(content);

            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeImgToFile(File src, File dest){
        if(src == null || dest == null) return;

        Log.v(TAG,"writeImgToFile()");

        try {
            FileInputStream fis = new FileInputStream(src);
            FileOutputStream fos = new FileOutputStream(dest);

            FileChannel fisChannel = fis.getChannel();
            FileChannel fosChannel = fos.getChannel();

            fosChannel.transferFrom(fisChannel,0,fisChannel.size());

            fisChannel.close();
            fosChannel.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public String readFromFile(File file){
        if(file == null) return "";

        StringBuilder contentBuilder = new StringBuilder();

        if(file.exists()){
            String line;

            try{
                FileReader reader = new FileReader(file);
                BufferedReader bufferReader = new BufferedReader(reader);

                while((line = bufferReader.readLine()) != null){
                    contentBuilder.append(line + "\n");
                }

                bufferReader.close();
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return contentBuilder.toString();
    }

    public boolean cleanDir(File dir){
        if(dir == null) return true;

        boolean success = false;

        if(dir.exists()){
            for(File file:dir.listFiles()){
                success = file.delete();
            }
        }

        return success;
    }

    public long getCachedTime(File file){
        if(sCacheTime.get(file) != null){
            return sCacheTime.get(file);
        }

        return System.currentTimeMillis();
    }

    public void setCachedTime(File file){
        sCacheTime.put(file,System.currentTimeMillis());
    }
}
