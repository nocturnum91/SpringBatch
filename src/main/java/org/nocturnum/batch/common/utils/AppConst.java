package org.nocturnum.batch.common.utils;

public final class AppConst {

    public enum Return {
        RTN_CNT("RTN_CNT"), RTN_STATUS("RTN_STATUS"), RTN_MESSAGE("RTN_MESSAGE"), RTN_MAP("MAP"), RTN_LIST("LIST");

        private String key;

        private Return(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

    public enum Request {
        REQ_PARAMETER("parameterMap"), REQ_ARRAYLIST("arrayList"), REQ_MAP_ID("MAP_ID"), REQ_MAP_ID2("MAP_ID2"),
        REQ_MAP_ID3("MAP_ID3"), REQ_STATUS("STATUS"), REQ_INSERT("INSERT"), REQ_UPDATE("UPDATE"), REQ_DELETE("DELETE");

        private String key;

        private Request(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum Value {
        BLANK("", ""), ZERO("0", "0"), STATUS_SUCCESS("S", "정상 처리 되었습니다."),
        STATUS_FAIL("F", "처리 중 오류가 발생하였습니다. \n잠시 후 다시 시도해 주세요"), STATUS_ERROR("E", "처리 중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요."),
        STATUS_EMPTY("0", "조회 결과가 없습니다."), BOOLEAN_Y("Y", "Y"), BOOLEAN_N("N", "N");

        private String code;
        private String desc;

        private Value(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}
