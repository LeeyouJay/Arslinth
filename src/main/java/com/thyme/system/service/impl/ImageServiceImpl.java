package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.thyme.system.dao.ProductImgDao;
import com.thyme.system.entity.bussiness.ProductImg;
import com.thyme.system.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName ImageServiceImpl
 * @Description TODO
 * @Date 2020/7/5
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageServiceImpl implements ImageService {

    private final ProductImgDao productImgDao;

    @Override
    public List<ProductImg> findImgByName(String pdName) {
        QueryWrapper<ProductImg> wrapper = new QueryWrapper<>();
        wrapper.eq("pd_name",pdName);
        return productImgDao.selectList(wrapper);
    }

    @Override
    public int updateUrl(ProductImg productImg) {
        List<ProductImg> imgByName = findImgByName(productImg.getPdName());
        if (imgByName.size()>0){
            UpdateWrapper<ProductImg> wrapper = new UpdateWrapper<>();
            wrapper.eq("pd_name",productImg.getPdName());
            return productImgDao.update(productImg,wrapper);
        }
        return 0;
    }

    @Override
    public int addImgUrl(ProductImg productImg) {
        List<ProductImg> imgByName = findImgByName(productImg.getPdName());
        if (imgByName.size()>0){
            UpdateWrapper<ProductImg> wrapper = new UpdateWrapper<>();
            wrapper.eq("pd_name",productImg.getPdName());
            return productImgDao.update(productImg,wrapper);
        }else
            return productImgDao.insert(productImg);
    }
}
