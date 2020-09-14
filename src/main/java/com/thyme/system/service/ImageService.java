package com.thyme.system.service;

import com.thyme.system.entity.bussiness.ProductImg;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/7/5
 */
public interface ImageService {

    List<ProductImg> findImgByName(String pdName);

    int updateUrl(ProductImg productImg);

    int addImgUrl(ProductImg productImg);
}
