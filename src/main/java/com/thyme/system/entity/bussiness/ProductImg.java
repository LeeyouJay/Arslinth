package com.thyme.system.entity.bussiness;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;

/**
 * @author Arslinth
 * @ClassName ProductImg
 * @Description TODO
 * @Date 2020/7/5
 */
@Data
@Builder
public class ProductImg {

    private String  pdName;

    private String url;

    public ProductImg() {
    }

    public ProductImg(String pdName, String url) {
        this.pdName = pdName;
        this.url = url;
    }
}
