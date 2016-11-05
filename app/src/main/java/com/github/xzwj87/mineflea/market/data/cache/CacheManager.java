package com.github.xzwj87.mineflea.market.data.cache;

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
        return file.exists();
    }

    public void writeToFile(File file, String content){
        if(!file.exists()){
            long current = System.currentTimeMillis();
            sCacheTime.put(file,current);
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void writeImgToFile(File src, File dest){
        if(src == null || dest == null) return;

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
        boolean success = false;

        if(dir.exists()){
            for(File file:dir.listFiles()){
                success = file.delete();
            }
        }

        return success;
    }

    public long getCachedTime(File file){
        return sCacheTime.get(file);
    }

    public void setCachedTime(File file){
        sCacheTime.put(file,System.currentTimeMillis());
    }
}
