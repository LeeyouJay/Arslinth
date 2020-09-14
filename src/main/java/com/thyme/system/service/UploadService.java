package com.thyme.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/21
 */
public interface UploadService {
    List<String> uploadExcel(MultipartFile file, String id);

    String uploadImg(MultipartFile file,String pdName) throws IOException;
}
