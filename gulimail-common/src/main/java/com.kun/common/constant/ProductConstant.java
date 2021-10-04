package com.kun.common.constant;

public class ProductConstant {
    public  enum  AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性"),
        DEFAULT_IMG(1, "默认图片"), NOT_DEFAULT_IMG(0, "非默认图片"),;

        private  int code;
        private  String msg;

        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }
        public  int getCode(){
            return  code;
        }
        public String getMsg(){
            return  msg;
        }
    }
}
