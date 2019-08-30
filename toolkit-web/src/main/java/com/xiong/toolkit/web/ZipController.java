package com.xiong.toolkit.web;


import com.google.common.collect.Lists;
import com.xiong.toolkit.lib.utils.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("zip")
public class ZipController {

    @GetMapping("export")
    public void export(HttpServletResponse response){

        String fileName = "测试";

        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");

        try (ZipOutputStream zipOutput = new ZipOutputStream(response.getOutputStream())) {
            Path zipPath = Files.createTempDirectory("test");

            Path contentPath = zipPath.resolve("test.json");

            FileUtils.writeStringToFile(contentPath.toFile(), "hello world", StandardCharsets.UTF_8);

            ZipUtil.compressWithDefaultDir(zipPath.toFile(), zipOutput);

            zipOutput.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("import")
    public void importFile(@RequestParam("multipartFile") MultipartFile multipartFile){

        if(null == multipartFile){
            return;
        }

        try {
            Path unzipFilePath = Files.createTempDirectory("unzip");

            File zipTempFile = File.createTempFile(UUID.randomUUID().toString(), multipartFile.getOriginalFilename());
            multipartFile.transferTo(zipTempFile);
            ZipFile zipFile = new ZipFile(zipTempFile);

            List<File> allFiles = ZipUtil.readFilesFromZip(zipFile, unzipFilePath);

            System.out.println(allFiles.size());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
