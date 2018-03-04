package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 28-04-2016.
 */
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    //File sd = Environment.getExternalStorageDirectory();
    //File data = Environment.getDataDirectory();

    private List<String> fileList;

    //private static final String OUTPUT_ZIP_FILE = "Folder.zip";

    private String SOURCE_FOLDER;// = "D:\\Reports"; // SourceFolder path

    public ZipUtils(){}

    public ZipUtils(String sourceFolder){
        fileList = new ArrayList<String>();
        SOURCE_FOLDER=sourceFolder;
    }

    public String zipIt(String zipFile,Context context){

        byte[] buffer = new byte[1024];
        String source = "";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try{
            try{
                source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("/") + 1, SOURCE_FOLDER.length());
            }catch (Exception e){
                source = SOURCE_FOLDER;
            }
            //fos = new FileOutputStream(zipFile);new File(sd + "/"+backupFromFolder)
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            //System.out.println("Output to Zip : " + zipFile);
            Toast.makeText(context, "Output to Zip : " + zipFile, Toast.LENGTH_LONG).show();

            FileInputStream in = null;

            for (String file : this.fileList){
                //System.out.println("File Added : " + file);
                Toast.makeText(context, "File Added : " + file, Toast.LENGTH_LONG).show();
                //ZipEntry ze = new ZipEntry(source + File.separator + file);
                ZipEntry ze = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                zos.putNextEntry(ze);
                try{
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0){
                        zos.write(buffer, 0, len);
                    }
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }finally{
                    in.close();
                }
            }

            zos.closeEntry();
            //System.out.println("Folder successfully compressed");
            Toast.makeText(context, "Folder successfully compressed", Toast.LENGTH_SHORT).show();

            return "Folder successfully compressed";

        }catch (IOException ex){
            ex.printStackTrace();
            return ex.getMessage();
        }finally{
            try{
                zos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void generateFileList(File node){
        // add file only
        if (node.isFile()){
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()){
            String[] subNote = node.list();
            for (String filename : subNote){
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file){
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }

    public static void unZip(File zipfile, File directory) throws IOException {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File file = new File(directory, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                InputStream in = zfile.getInputStream(entry);
                try{
                    copy(in, file);
                } finally {
                    in.close();
                }
            }
        }
    }
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
        } finally {
            out.close();
        }
    }
}