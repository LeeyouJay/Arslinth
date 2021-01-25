package com.thyme.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.utils.excel.ExportExcelData;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.Type;

import java.util.List;


/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/17
 */

public interface ProductService {

    List<Product> getProducts(String productName,String typeId);

    int addProduct(Product product);

    int updateProduct(Product product);

    int deleteProduct(String id);

    List<Product> getAllProducts();

    Product findProductById(String id);

    List<Type> getTypeList();

    int addType(Type type);

    int deleteType(String id);

    int updateType(Type type);

    int updateProductType(String id, String type);

    int updateStatus(String id, boolean isShow);

    int updateUnit(String unit, String numUnit,String id);

    List<ExportExcelData> forExport();
}
