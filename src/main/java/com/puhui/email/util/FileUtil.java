package com.puhui.email.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author: 邹玉玺
 * @date: 2020/7/23-9:08
 */
@Slf4j
public class FileUtil {

    public static String fileUpload( MultipartFile multipartFile) {
        String filepath="D:\\upload\\"+multipartFile.getOriginalFilename();
        File file = new File(filepath);
        File parentFile = file.getParentFile();
        try {
            //如果文件夹不存在  创建文件夹
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            //上传文件到指定位置
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(file));
            System.out.println(multipartFile.getName());
            out.write(multipartFile.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("文件上传失败" + e.getMessage());
            return "文件上传失败" + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("文件上传失败" + e.getMessage());
            return "文件上传失败" + e.getMessage();
        }
        log.info("文件上传成功");
        return filepath;
    }
}
