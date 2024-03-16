package org.nocturnum.batch.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ParameterMap extends HashMap<String, Object> {

    public ParameterMap() {
        super();
    }

    private Map<String, Object> userMap = new HashMap<String, Object>();

    public ParameterMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (entry.getValue() instanceof String[]) {
                String[] valueArray = (String[]) entry.getValue();
                if (valueArray.length == 1) {
                    super.put(key, valueArray[0]);
                } else { //동일 name의 패러미터가 2개 이상일 경우
                    super.put(key, valueArray);
                }
            } else {
                super.put(key, entry.getValue());
            }
        }
    }

    public ParameterMap(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (java.util.Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (entry.getValue() instanceof String[]) {
                String[] valueArray = (String[]) entry.getValue();

                if (valueArray.length == 1) {
                    super.put(key, valueArray[0]);
                } else { //동일 name의 패러미터가 2개 이상일 경우
                    super.put(key, valueArray);
                }
            } else {
                super.put(key, entry.getValue());
            }
        }
    }

    @Override
    public Object get(Object key) {
        Object value = get(key, false);

        if (value instanceof String) {
            value = TextUtil.removeScript(value.toString());
        }
        /* null 처리 */
        if (value == null) {
            value = "";
        }
        return value;
    }

    protected Object get(Object key, boolean multi_yn) {
        Object object = super.get(key);
        if (super.containsKey(key)) {
            if (key instanceof String) {
                if (object instanceof String[] && multi_yn == false) {
                    object = ((String[]) object)[0];
                }
            }
        }
        return object;
    }

    public String getValue(String key) {
        String value = "";
        Object object = get(key);

        if (object != null) {
            value = object.toString();
        }

        value = TextUtil.removeScript(value);

        return value;
    }

    public String[] getArray(String key) {
        String[] values = null;
        Object object = get(key, true);

        if (super.containsKey(key)) {
            if (object instanceof String) {
                values = new String[1];
                values[0] = object.toString();
            } else if (object instanceof String[]) {
                values = (String[]) object;
            } else {
                values = new String[0];
            }
        } else {
            values = new String[0];
        }

        for (int i = 0; values != null && i < values.length; i++) {
            values[i] = TextUtil.xss(values[i]);
        }

        return values;
    }

    public int getInt(String key) {
        return (int) getDouble(key);
    }

    public int[] getIntArray(String key) {
        String[] arr = getArray(key);
        int[] darr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            darr[i] = (int) parseDouble(arr[i]);
        }
        return darr;
    }

    public long getLong(String key) {
        return (long) getDouble(key);
    }

    public long[] getLongArray(String key) {
        String[] arr = getArray(key);
        long[] darr = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            darr[i] = (long) parseDouble(arr[i]);
        }
        return darr;
    }

    public float getFloat(String key) {
        return (float) getDouble(key);
    }

    public float[] getFloatArray(String key) {
        String[] arr = getArray(key);
        float[] darr = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            darr[i] = (float) parseDouble(arr[i]);
        }
        return darr;
    }

    public double getDouble(String key) {
        return parseDouble(getValue(key));
    }

    public double[] getDoubleArray(String key) {

        String[] arr = getArray(key);
        double[] darr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            darr[i] = parseDouble(arr[i]);
        }
        return darr;
    }

    public double parseDouble(String value) {
        double doubleValue = 0.0D;
        if (value == null || value.equals("")) {
            value = "0.0";
        }
        try {
            if (value.indexOf(',') > -1) {
                doubleValue = Double.parseDouble(value.replaceAll(",", ""));
            } else {
                doubleValue = Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return doubleValue;
    }

    public void addProperty(String name, Object value) {
        userMap.put(name, value);
    }

}
