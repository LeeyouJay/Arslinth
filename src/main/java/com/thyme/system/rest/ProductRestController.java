package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.bussiness.Product;
import com.thyme.system.entity.bussiness.ProductImg;
import com.thyme.system.entity.bussiness.Type;
import com.thyme.system.service.ImageService;
import com.thyme.system.service.ProductService;
import com.thyme.system.service.UploadService;
import com.thyme.system.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Arslinth
 * @ClassName ProductRestController
 * @Description TODO
 * @Date 2020/6/17
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductRestController {

    private final ProductService productService;

    private final UploadService uploadService;

    private final ImageService imageService;

    @Value("${imgPath}")
    private String imgPath;

    @GetMapping("/allProducts")
    public ApiResponse getAllProducts(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("products",productService.getAllProducts());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/products")
    public ApiResponse getProducts(@RequestBody SearchVO searchVO){

        JSONObject jsonObject = new JSONObject();
        List<Product> products = productService.getProducts(searchVO.getSearchName());
        jsonObject.put("products",products);
        jsonObject.put("total",products.size());

        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addProduct")
    @ResponseBody
    public ApiResponse addProduct(@RequestBody Product product){
        int i = productService.addProduct(product);
        if ( i == 0)
            return ApiResponse.ofMessage(403,"存在相同品种名称！");
        else if (i == 1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加出现异常！");
    }

    @PostMapping("/updateProduct")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse updateProduct(@RequestBody Product product){
        int i = productService.updateProduct(product);
        if ( i == 0)
            return ApiResponse.ofMessage(403,"已存在相同品种名称！");
        else if (i == 1)
            return ApiResponse.success("修改成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/deleteProduct")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteProduct(@RequestParam("id") String id ){
        int i = productService.deleteProduct(id);
        if (i==0)
            return ApiResponse.ofMessage(403,"此品种仍有入库数据，操作拒绝！");
        else if (i==1)
            return ApiResponse.success("删除成功！");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @GetMapping("/getType")
    public ApiResponse getTypeList(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeList",productService.getTypeList());
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addType")
    @ResponseBody
    public ApiResponse addType(@RequestBody Type type){

        int i = productService.addType(type);
        if ( i == 0)
            return ApiResponse.ofMessage(403,"存在相类型名称！");
        else if (i == 1)
            return ApiResponse.success("添加成功！");
        else
            return ApiResponse.fail("添加出现异常！");
    }

    @GetMapping("/deleteType")
    public ApiResponse deleteType(@RequestParam("id") String id){
        int i = productService.deleteType(id);
        if (i==0)
            return ApiResponse.ofMessage(403,"找不到对应id");
        else if (i==1)
            return ApiResponse.success("删除成功！");
        else if(i==2)
            return ApiResponse.fail("请先修改仍在使用该类型的品种信息");
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @PostMapping("/updateType")
    @ResponseBody
    public ApiResponse updateType(@RequestBody Type type){
        int i = productService.updateType(type);
        if ( i == 0)
            return ApiResponse.ofMessage(403,"已存在相同名称！");
        else if (i == 1)
            return ApiResponse.success("字段更改为："+type.getTypeName());
        else
            return ApiResponse.fail("修改出现异常！");
    }

    @PostMapping("/updateImage")
    @ResponseBody
    public ApiResponse updateImage(MultipartFile file,String pdName) {

        try {
            String url = uploadService.uploadImg(file,pdName);
            ProductImg img = new ProductImg(pdName, url);
            int i = imageService.updateUrl(img);
            if(i>0)
                return ApiResponse.success("图片修改成功！");
            else
                return ApiResponse.fail("图片修改出现异常！");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("上传出现异常！");
        }

    }
    @PostMapping("/addImage")
    @ResponseBody
    public ApiResponse addImage(MultipartFile file,String pdName) {
        try {
            String url = uploadService.uploadImg(file,pdName);
            ProductImg img = new ProductImg(pdName, url);
            int i = imageService.addImgUrl(img);
            if(i>0)
                return ApiResponse.success("图片添加成功！");
            else
                return ApiResponse.fail("存在相同品种名称！");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("上传出现异常！");
        }

    }

    @PostMapping("/richImg")
    @ResponseBody
    public String richImg(MultipartFile file,@RequestParam("id") String id){
        JSONObject jsonObject = null;
        String uuid = UUIDUtils.getSixteenUUID();
        Product product = productService.findProductById(id);
        try {
            String src = uploadService.uploadImg(file,product.getPdName()+uuid );
            jsonObject = new JSONObject();
            JSONObject map = new JSONObject();
            jsonObject.put("code",0);
            jsonObject.put("msg","上传成功！");
            map.put("src",imgPath+src);
            map.put("title",product.getPdName());
            jsonObject.put("data",map);
        } catch (IOException e) {
            jsonObject.put("code",1);
            jsonObject.put("msg","上传失败！");
            jsonObject.put("data",null);
            e.printStackTrace();
        }

        return jsonObject.toJSONString();
    }



    @GetMapping("/updateProductType")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse updateProductType(@RequestParam("id") String id,@RequestParam("type") String type){
        int i = productService.updateProductType(id, type);
        if(i==1){
            return ApiResponse.success("类型更改为：");
        }else
            return ApiResponse.fail("更改出现异常！");
    }

    @GetMapping("/changeStatus")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    @ResponseBody
    public ApiResponse updateStatus(@RequestParam("id")String id ,@RequestParam("isShow")Boolean isShow){
        int i = productService.updateStatus(id, isShow);
        if(i==1){
            return ApiResponse.success("变更成功！");
        }else
            return ApiResponse.fail("更改出现异常！");
    }

}
