package com.xiong.toolkit.lib.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final int BUFFER = 10240;

    public static void compressWithDefaultDir(File file, ZipOutputStream zipOut) throws IOException{
        compress(file, zipOut, "");
    }


    public static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException{
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException{
        File[] files = dir.listFiles();

        if(null == files || files.length == 0){
            return;
        }

        for (File file : files) {
            compress(file, zipOut, baseDir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir)  throws IOException{
        if (!file.exists()){
            return;
        }

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        ZipEntry entry = new ZipEntry(baseDir + file.getName());
        zipOut.putNextEntry(entry);
        int count;
        byte[] data = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zipOut.write(data, 0, count);
        }
    }

    /**
     * read all files from a zip file
     * @param zipFile zip file
     * @param unzipFilePath zip file unzip path
     * @return all files
     * @throws IOException I/O exception
     */
    public static List<File> readFilesFromZip(ZipFile zipFile, Path unzipFilePath) throws IOException{

        if(null == zipFile){
            return Lists.newArrayList();
        }

        List<File> files = Lists.newArrayList();

//        Files.createDirectories(unzipFilePath);

        Enumeration zipEntries = zipFile.entries();

        while (zipEntries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

            if (zipEntry.isDirectory()) {
                continue;
            }

            String fileName = zipEntry.getName();
            Path filePath = unzipFilePath.resolve(fileName);
            Path parentDirectorPath = filePath.getParent();
            if (!parentDirectorPath.toFile().exists()) {
                Files.createDirectories(parentDirectorPath);
            }

            InputStream inputStream = zipFile.getInputStream(zipEntry);
            Files.copy(inputStream, filePath);
            files.add(filePath.toFile());
        }

        return files;
    }
}