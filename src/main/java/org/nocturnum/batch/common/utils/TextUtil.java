package org.nocturnum.batch.common.utils;

import java.util.regex.Pattern;

public class TextUtil {

    public static final int CHARSET_UTF8 = 1;
    public static final int CHARSET_NON_UTF8 = 0;

    /**
     * 개요:자바스크립트 태그 무력화
     */
    public static String removeScript(String s) {

        if (s == null || s.equals("")) {
            return "";
        }

        s = s.replaceAll("<[sS][cC][rR][iI][pP][tT]>", "&lt;script&gt;");
        s = s.replaceAll("</[sS][cC][rR][iI][pP][tT]>", "&lt;/script&gt;");

        return s;
    }

    /**
     * 개요:XSS(크로스 사이트 스크립팅 방지)
     */
    public static String xss(String value) {

        if (value == null || value.equals("")) {
            return "";
        }

        String ret = value;

        // < -> &lt;
        ret = TextUtil.replaceString(ret, "<", "&lt;");
        // > -> &gt;
        ret = TextUtil.replaceString(ret, ">", "&gt;");
        // "\t" -> &nbsp;&nbsp;&nbsp;
        ret = TextUtil.replaceString(ret, "\t", "&nbsp;&nbsp;&nbsp;");
        // "\"" -> &#34;
        ret = TextUtil.replaceString(ret, "\"", "&#34;");

        return ret.trim();
    }

    /**
     * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다. 대소문자를 구분하지 않고 변환한다.
     *
     * @param word
     * @param regex       자바 정규식 표현
     * @param replacement
     * @return string
     */
    public static String replaceString(String word, String regex, String replacement) {
        return replaceString(word, regex, replacement, true);
    }

    /**
     * 문자열 중의 특정 문자열을 원하는 문자열로 바꾼다. caseSensitive:true -> 대소문자를 구분하지 않는다.
     * caseSensitive:false -> 대소문자를 구분한다.
     *
     * @param word
     * @param regex         자바 정규식 표현
     * @param replacement
     * @param caseSensitive
     * @return string
     */
    public static String replaceString(String word, String regex, String replacement, boolean caseSensitive) {

        if (word == null) {
            throw new RuntimeException("Parameter 'word' can not be null.");
        }

        if ("".equals(word)) {
            return "";
        }

        if (regex == null) {
            throw new RuntimeException("Parameter 'regex' can not be null.");
        }

        if (replacement == null) {
            throw new RuntimeException("Parameter 'replacement' can not be null.");
        }

        Pattern pattern = null;
        if (caseSensitive) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regex);
        }

        return pattern.matcher(word).replaceAll(replacement);
    }

    /**
     * 문자열의 지정된 갯수를 원하는 문자열로 변환한다. 예) replaceString("abcde", -3, "*") -> ab***
     * replaceString("abcde", 3, "*") -> ***de 실제 문자열 길이를 초과하면 모든 문자를 치환한다.
     *
     * @param word
     * @param length      치환할 문자갯수(양수이면 앞에서 부터, 음수이면 뒤에서부터 치환함)
     * @param replacement 치환할 문자열
     * @return string
     */
    public static String replaceString(String word, int length, String replacement) {

        String regexp = null;
        int real_length = (Math.abs(length) > word.length() ? word.length() : Math.abs(length));
        if (length >= 0) {
            regexp = "^.{" + real_length + "}";
        } else {
            regexp = ".{" + real_length + "}$";
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < real_length; i++) {
            buf.append(replacement);
        }
        Pattern pattern = Pattern.compile(regexp);

        return pattern.matcher(word).replaceAll(buf.toString());
    }

}
