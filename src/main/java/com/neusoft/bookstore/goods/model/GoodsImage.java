package com.neusoft.bookstore.goods.model;

import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsImage extends BaseModel {
    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("图片路径")
    private String skuImagesPath;
}
