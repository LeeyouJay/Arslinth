package com.thyme.common.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.dao.ProductDao;
import com.thyme.system.dao.ProductImgDao;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.ProductImg;
import com.thyme.system.entity.bussiness.Stock;
import com.thyme.system.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Arslinth
 * @ClassName ExcelListener
 * @Description TODO
 * @Date 2020/6/21
 */
public class ExcelListener extends AnalysisEventListener<Stock> {

    private String principalId;

    private StockService stockService;

    private ProductDao productDao;

    private ProductImgDao productImgDao;

    private String errorMessage ;

    private List<String> errorMessages = new ArrayList<>();

    public ExcelListener(String principalId, StockService stockService, ProductDao productDao, ProductImgDao productImgDao) {
        this.principalId = principalId;
        this.stockService = stockService;
        this.productDao = productDao;
        this.productImgDao = productImgDao;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);


    @Override
    public void invoke(Stock stock, AnalysisContext analysisContext) {

        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(stock));

        Product productById = productDao.findProductByName(stock.getPdName());
        stock.setPrincipalId(principalId);
        stock.setSingle(false);
        if(productById == null){
            String productId = UUIDUtils.getUUID();
            stock.setProductId(productId);
            productImgDao.insert(ProductImg.builder().pdName(stock.getPdName()).build());
            Product product = Product.builder()
                    .id(productId)
                    .price(stock.getPrice())
                    .cost(stock.getCost())
                    .pdName(stock.getPdName())
                    .num(stock.getCount())
                    .unit(stock.getStandards())
                    .characters("")
                    .build();
            if(stock.getUnit()!=0)
                product.setNumUnit(stock.getCount()/stock.getUnit());

            productDao.addProduct(product);
        }else{
            Product product = Product.builder()
                    .pdName(stock.getPdName())
                    .num(stock.getCount())
                    .cost(stock.getCost())
                    .price(stock.getPrice())
                    .unit(stock.getStandards())
                    .build();
            if(stock.getUnit()!=0)
                product.setNumUnit(stock.getCount()/stock.getUnit());

            productDao.updateNumByName(product);
            stock.setProductId(productById.getId());
        }
        stockService.addStock(stock);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("所有数据解析完成！");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        LOGGER.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            LOGGER.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
            int rowIndex = excelDataConvertException.getRowIndex()+1;
            int columnIndex = excelDataConvertException.getColumnIndex()+1;
            errorMessage ="第"+rowIndex+"行，第"+columnIndex+"列解析异常";
            errorMessages.add(errorMessage);
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
