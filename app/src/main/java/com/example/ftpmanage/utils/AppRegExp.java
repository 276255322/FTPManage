package com.example.ftpmanage.utils;

import java.util.regex.Pattern;

public class AppRegExp {

    /// <summary>电子邮件校验</summary>
    public static final String RegExpStr_Email = "^\\w+((-\\w+)|(\\.\\w+))*\\@\\w+((\\.|-)\\w+)*\\.\\w+$";

    /// <summary>网址校验</summary>
    public static final String RegExpStr_Url = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+(\\\\?{0,1}(([A-Za-z0-9-~]+\\\\={0,1})([A-Za-z0-9-~]*)\\\\&{0,1})*)$";

    /// <summary>域名校验</summary>
    public static final String RegExpStr_Domain = "^([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /// <summary>邮编校验</summary>
    public static final String RegExpStr_Zip = "\\d{6}";

    /// <summary>身份证校验</summary>
    public static final String RegExpStr_Ssn = "\\d{18}|\\d{15}";

    /// <summary>严格15位身份证校验(15位)</summary>
    public static final String RegExpStr_IDCard15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

    /// <summary>严格18位身份证校验(18位)</summary>
    public static final String RegExpStr_IDCard18 = "^[1-9][0-7]\\d{4}((\\d{4}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(\\d{4}(0[13578]|1[02])31)|(\\d{4}02(0[1-9]|1\\d|2[0-8]))|(\\d{2}([13579][26]|[2468][048]|0[48])0229))\\d{3}(\\d|X|x)$";

    /// <summary>IP校验</summary>
    public static final String RegExpStr_Ip = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";

    /// <summary>日期校验</summary>
    public static final String RegExpStr_Date = "^(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\\/|-|\\.)(?:0?2\\1(?:29))$)|(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(\\/|-|\\.)(?:(?:(?:0?[13578]|1[02])\\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\\2(?:0?[1-9]|1\\d|2[0-8]))$";

    /// <summary>url校验</summary>
    public static final String RegExpStr_ReUrl = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$";

    /// <summary>颜色值十六进格式制校验 校验格式： #ff56ee</summary>
    public static final String RegExpStr_Color = "^#[0-9a-fA-F]{6}$";

    /// <summary>颜色值十进制格式校验 校验格式： 128,233,144</summary>
    public static final String RegExpStr_ColorTen = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\,(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\,(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";

    /// <summary>整数校验</summary>
    public static final String RegExpStr_Int = "^\\d{1,}$";

    /// <summary>数字校验</summary>
    public static final String RegExpStr_Demical = "^-?(0|\\d+)(\\.\\d+)?$";

    /// <summary>非负整数（正整数 + 0）校验</summary>
    public static final String RegExpStr_NotMinus = "^\\d+$";

    /// <summary>正整数校验</summary>
    public static final String RegExpStr_Plus = "^[0-9]*[1-9][0-9]*$";

    /// <summary>非正整数（负整数 + 0）校验</summary>
    public static final String RegExpStr_NotPlus = "^((-\\d+)|(0+))$";

    /// <summary>负整数校验</summary>
    public static final String RegExpStr_Minus = "^-[0-9]*[1-9][0-9]*$";

    /// <summary>非负浮点数（正浮点数 + 0）校验</summary>
    public static final String RegExpStr_NotMinusFr = "^\\d+(\\.\\d+)?$";

    /// <summary>正浮点数校验</summary>
    public static final String RegExpStr_PlusFr = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";

    /// <summary>非正浮点数（负浮点数 + 0）校验</summary>
    public static final String RegExpStr_NotPlusFr = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";

    /// <summary>负浮点数校验</summary>
    public static final String RegExpStr_MinusFr = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";

    /// <summary>浮点数校验</summary>
    public static final String RegExpStr_Fraction = "^(-?\\d+)(\\.\\d+)?$";

    /// <summary>由26个英文字母组成的字符串校验</summary>
    public static final String RegExpStr_26Letter = "^[A-Za-z]+$";

    /// <summary>由26个英文字母的大写组成的字符串校验</summary>
    public static final String RegExpStr_26UpperLetter = "^[A-Z]+$";

    /// <summary>由26个英文字母的小写组成的字符串校验</summary>
    public static final String RegExpStr_26LowerLetter = "^[a-z]+$";

    /// <summary>由数字和26个英文大小写字母组成的字符串校验</summary>
    public static final String RegExpStr_Num26Letter = "^[A-Za-z0-9]+$";

    /// <summary>由数字和26个英文大小写字母和“-”组成的字符串校验</summary>
    public static final String RegExpStr_Num26LetterAnd = "^[A-Za-z0-9-]+$";

    /// <summary>由数字、26个英文大小写字母或者中文组成的字符串校验</summary>
    public static final String RegExpStr_Num26LeAndCn = "^[\\u4e00-\\u9fa5A-Za-z0-9]+$";

    /// <summary>由数字、下划线、26个英文大小写字母或者中文组成的字符串校验</summary>
    public static final String RegExpStr_Num26LeAndCna = "^[\\u4e00-\\u9fa5A-Za-z0-9_]+$";

    /// <summary>由数字、26个英文字母或者下划线组成的字符串校验</summary>
    public static final String RegExpStr_SNum26LeLine = "^[A-Za-z0-9_]+$";

    /// <summary>首字符必须是字母后续字符由数字、26个英文字母或者下划线组成的字符串校验</summary>
    public static final String RegExpStr_S26NumLeLine = "^[A-Za-z][A-Za-z0-9_]+$";

    /// <summary>手机号字符串效验</summary>
    public static final String RegExpStr_MOBILEPHONE = "^0?(1[34578][0-9])[0-9]{8}$";

    /// <summary>固定电话效验</summary>
    public static final String RegExpStr_PHONE = "^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";

    //效验字符串是否匹配指定的正则表达式，如果匹配则返回true,否则返回false;
    public static boolean isReg(String regx, String s){
        return Pattern.matches(regx,  s);
    }

    //效验字符串是否不匹配指定的正则表达式，如果不匹配则返回true,否则返回false;
    public static boolean isNotReg(String regx, String s){
        return !isReg(regx,  s);
    }


}
