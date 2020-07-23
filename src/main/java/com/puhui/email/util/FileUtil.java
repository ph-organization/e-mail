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
/**
 * 将文件资源保存在D:\\upload下   (一般保存在服务器上)
 */
public class FileUtil {

    public static void fileUpload( MultipartFile multipartFile) {
        try {
            //上传文件到指定位置
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(new File(
                            "D:\\upload\\"+multipartFile.getOriginalFilename())));
            System.out.println(multipartFile.getName());
            out.write(multipartFile.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("文件上传失败" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("文件上传失败" + e.getMessage());
        }
        log.info("文件上传成功");
    }
}
