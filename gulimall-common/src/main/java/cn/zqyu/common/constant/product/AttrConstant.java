package cn.zqyu.common.constant.product;

import cn.hutool.core.util.StrUtil;

public class AttrConstant {

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "base", "基本属性"),
        ATTR_TYPE_SALE(0, "sale", "销售属性");

        private final int code;
        private final String label;
        private final String msg;

        AttrEnum(int code, String label, String msg) {
            this.code = code;
            this.label = label;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public String getLabel() {
            return label;
        }

        public static int getCode(String label) {
            if (StrUtil.isBlank(label)) {
                return -1;
            }
            AttrEnum[] attrEnums = values();
            for (AttrEnum attrEnum : attrEnums) {
                if (attrEnum.getLabel().equals(label)) {
                    return attrEnum.getCode();
                }
            }
            return -1;
        }
    }
}
