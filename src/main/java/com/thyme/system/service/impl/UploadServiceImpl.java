package com.thyme.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.common.utils.excel.ExcelListener;
import com.thyme.system.dao.ProductDao;
import com.thyme.system.dao.ProductImgDao;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.service.StockService;
import com.thyme.system.service.UploadService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Arslinth
 * @ClassName UploadServiceImpl
 * @Description TODO
 * @Date 2020/6/21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadServiceImpl implements UploadService {

    private final StockService stockService;

    private final ProductDao productDao;

    private final ProductImgDao productImgDao;

    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    // 文件的真实路径
    @Value("${file.uploadFolder}")
    private String realBasePath;
    @Value("${file.accessPath}")
    private String accessPath;

    @Override
    public List<String> uploadExcel(MultipartFile file, String id) {
        try {
            final ExcelListener excelListener = new ExcelListener(id, stockService, productDao,productImgDao);
            EasyExcel.read(file.getInputStream(), Stock.class, excelListener).headRowNumber(2).sheet().doRead();
            List<String> messages = excelListener.getErrorMessages();
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String uploadImg(MultipartFile file,String pdName) throws IOException {

        InputStream inputStream = file.getInputStream();

        // 文件唯一的名字
        //String fileName = UUIDUtils.getUUID()+".jpg";
        String fileName = pdName+".jpg";

        // 域名访问的相对路径（通过浏览器访问的链接-虚拟路径）
        String saveToPath = accessPath;
        // 真实路径，实际储存的路径
        String realPath = realBasePath;
        // 储存文件的物理路径，使用本地路径储存
        String filepath = realPath + fileName;
        logger.info("上传图片名为：" + fileName + "--虚拟文件路径为：" + saveToPath + "--物理文件路径为：" + realPath);
        // 判断有没有对应的文件夹
        File destFile = new File(filepath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        //缩小并保存图片
        Thumbnails.of(inputStream).outputQuality(1f).size(550, 700).toFile(destFile);

        // 输出流 输出到文件
//        OutputStream outputStream = new FileOutputStream(destFile);
//        // 缓冲区
//        byte[] bs = new byte[1024];
//        int len = -1;
//        while ((len = inputStream.read(bs)) != -1) {
//            outputStream.write(bs,0,len);
//        }
//        inputStream.close();
//        outputStream.close();
        // 返回虚拟路径，给链接访问
        return saveToPath + fileName;
    }
}
