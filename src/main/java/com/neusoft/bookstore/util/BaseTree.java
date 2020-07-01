package com.neusoft.bookstore.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/4/29 8:53
 * 通用树信息
 */
@Data
public class BaseTree {

    @ApiModelProperty("树节点名称")
    private  String nodeName;

    @ApiModelProperty("树节点Id:唯一标识树节点信息")
    private  String  nodeId;

    @ApiModelProperty("节点路径")
    private  String nodeUrl;

    @ApiModelProperty("该节点所有的属性信息")
    private  Object attribute;

    @ApiModelProperty("该节点下所有的子节点信息")
    private List<BaseTree> childNodes;
}
