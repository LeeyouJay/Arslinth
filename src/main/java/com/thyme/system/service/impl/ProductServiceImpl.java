package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.dao.ProductDao;
import com.thyme.system.dao.ProductImgDao;
import com.thyme.system.dao.StockDao;
import com.thyme.system.dao.TypeDao;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.ProductImg;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.entity.bussiness.Type;
import com.thyme.system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Arslinth
 * @ClassName ProductServiceImpl
 * @Description TODO
 * @Date 2020/6/17
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    private final StockDao stockDao;

    private final TypeDao typeDao;

    private final ProductImgDao productImgDao;
    @Override
    public List<Product> getProducts(String productName) {
        return productDao.getProducts(productName);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addProduct(Product product) {
        try {
            Product productById = productDao.findProductByName(product.getPdName());
            if(productById !=null){
                return 0;
            }
            ProductImg one = productImgDao.selectOne(new QueryWrapper<ProductImg>().eq("pd_name", product.getPdName()));
            if(one==null)
                productImgDao.insert(ProductImg.builder().pdName(product.getPdName()).build());
            return productDao.addProduct(Product.builder()
                    .id(UUIDUtils.getUUID())
                    .pdName(product.getPdName())
                    .type(product.getType())
                    .characters(product.getCharacters())
                    .height(product.getHeight())
                    .period(product.getPeriod())
                    .price(product.getPrice())
                    .cost(product.getCost())
                    .num(product.getNum())
                    .yield(product.getYield()).build());
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int updateProduct(Product product) {
        try {
            Product productByName = productDao.findProductByName(product.getPdName());
            Product productById = productDao.findProductById(product.getId());
            if (productByName!=null && !productByName.getId().equals(product.getId()))
                return 0;
            else {
                ProductImg one = productImgDao.selectOne(new QueryWrapper<ProductImg>().eq("pd_name", productById.getPdName()));
                if (one!=null)
                    productImgDao.updatePdName(product.getPdName(),one.getPdName());
                productDao.updateProduct(product);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int deleteProduct(String id) {
        try {
            List<Stock> stocks = stockDao.findStockByProductId(id);
            if (stocks.size() > 0)
                return 0;
            else{
                Product productById = productDao.findProductById(id);
                productImgDao.delete(new QueryWrapper<ProductImg>().eq("pd_name",productById.getPdName()));
                productDao.deleteProduct(id);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    public Product findProductById(String id) {
        return productDao.findProductById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public List<Type> getTypeList() {
        return typeDao.selectList(new QueryWrapper<Type>());
    }

    @Override
    public int addType(Type type) {
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq("type_name",type.getTypeName());
        Type one = typeDao.selectOne(query);
        if(one != null)
            return 0;
        return typeDao.insert(Type.builder()
                .id(UUIDUtils.getUUID())
                .typeName(type.getTypeName())
                .build());
    }

    @Override
    public int deleteType(String id) {
        List<Product> products = typeDao.checkStockByType(id);
        if (products.size()>0 ||"33ff9fb9e11a454dbaa388e8332a8ac4".equals(id)){//品种创建时默认的类型id
            return 2;
        }
        return typeDao.deleteById(id);
    }

    @Override
    public int updateType(Type type) {
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq("type_name",type.getTypeName());
        Type one = typeDao.selectOne(query);
        if(one != null)
            return 0;
        return typeDao.updateById(type);
    }

    @Override
    public int updateProductType(String id, String type) {
        return productDao.updateProductType(id,type);
    }

    @Override
    public int updateStatus(String id, boolean isShow) {
        return productDao.updateStatus(isShow,id);
    }
}
