package com.it.reggie.controller;

import com.it.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonUploadAndDownlController {

    @Value("${reggir.path}")
    private String basspath;

    @PostMapping("/upload")
    public R<String> upload (MultipartFile file){

        //得到原图片格式
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成UUID
        String jpgname = UUID.randomUUID().toString()+substring;

//        如果没有文件夹则生成文件夹（真是情况可能是已日期为分类的文件夹并返回，便于下次下载）
        File file1 = new File(basspath);
        if (!file1.exists()){
            file1.mkdirs();
        }

        try {
            file.transferTo(new File(basspath+jpgname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(jpgname);
    }

    /**
     * 下载文件
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basspath+name));
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
