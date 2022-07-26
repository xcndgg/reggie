package com.xc.controller;

import com.xc.common.R;

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

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String bashPath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String origiName = file.getOriginalFilename();
        String suffix = origiName.substring((origiName.lastIndexOf(("."))));
        //重命名防止名字重复而被覆盖
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建目录对象
        File dir = new File(bashPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            file.transferTo(new File(bashPath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(bashPath+name));
            //回写数据回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1){
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
