package com.neusoft.bookstore.util;

/**
 * @author 煤小二
 * @Date 2020/5/15 8:56
 */
public class GoodsInfoExcetion extends  RuntimeException {


    public GoodsInfoExcetion() {
        super();
    }

    public GoodsInfoExcetion(String message) {

        super(message);
    }

    public GoodsInfoExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public GoodsInfoExcetion(Throwable cause) {
        super(cause);
    }

    public GoodsInfoExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
