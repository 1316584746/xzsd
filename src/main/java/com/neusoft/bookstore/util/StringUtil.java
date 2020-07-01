package com.neusoft.bookstore.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName StringUtil
 * @Author yins
 * @Date 2018/08/01 18:08
 */

public class StringUtil {
    /**
     * 部门：软件开发事业部
     * 功能：生成code
     * 描述：年月日时分秒毫秒17位（20180801181002002）+num位随机数
     * 作成者：yins
     * 作成时间：2018-8-1
     */
    public static String getCommonCode(int num){
        String prefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix =radmonkey(num);
        return prefix + suffix;
    }

    /*
     * @param count :需要产生随机数的个数
     */
    public static String radmonkey(int count){
        StringBuffer sbf=new StringBuffer();
        for (int i = 0; i <count; i++) {
            int num=(int)(Math.random()*10);
            sbf.append(num);
        }

        return sbf.toString();
    }

    public static void main(String[] args) {
        System.out.println(getCommonCode(2));
    }
}
