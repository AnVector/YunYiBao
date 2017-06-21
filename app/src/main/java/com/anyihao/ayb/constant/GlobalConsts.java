package com.anyihao.ayb.constant;

/**
 * author: Administrator
 * date: 2017/2/16 001613:54.
 * email:looper@126.com
 */

public class GlobalConsts {

    //Umeng appkey
    public static final String UMENG_APPKEY = "580498fae0f55a28f400166a";
    //Umeng Message Secret
    public static final String UMENG_MESSAGE_SECRET = "24c18d51d216d379ca4d8e30b6334d0f";
    //App Database name
    public static final String DB_NAME = "ayb_db";
    //微信开发平台APPID
    public static final String WX_APP_ID = "wxc6a6d430c1f2c793";
    //测试用UID
    public static final String Test_UID = "6hck13vi9q48hkcv8ovo";
    //蚂蚁金服开放平台APPID
    public static final String ALIPAY_APP_ID = "2016101702214300";
    //蚂蚁金服开放平台商户私钥，pkcs8格式
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA_PRIVATE =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALtILnky5MQVw4eMTwkflSMpDdq5q0xs5VIei69ybmBI0wpSFyE28lSW/pP2OmtQ0qKGjCyqZG6aIVNBDOHTjwY4350V8wsfa8j5N/9makF/10eejZpa9zmf6ITRs+fdbGh6Y4Au/cXUXpsJksvryrDtr/HMz3rzeiudHjLTFPRDAgMBAAECgYEAqo8ZK+2qo92CPh8NF6z4oJXR9UGkfKyryDbjVfwsA2ovMLYENI6a8Wi2HsBfAb8QpU1NuZvQbV3XPb//YGbQV/XWz/tJsEMxDiGbW2jD9S9UdwgjJO0878CxYYqj81jC/CYnyD754iSLhuFbJM3FMdsoP/vWUPnJTC1/uBN3YakCQQDn3wTKXfnrQVm74xH60+kdZk4vNS9ywBZSn/lyiMGKa661EyuJQ18WizlVqUmt3a32l9ckIQ5xKmWQbTIzQ/K9AkEAzsVRsvl5g1JBAgXpeCp32mGb47gCqT3991PZhl+2tlj2kcD4tJgJLxiOSPp2lZIvdshhZVGDKOcCrH1e/WZy/wJAUjPMfPnoGjEm4OdVfnkWEegtG6tdUO8sespgIuy8wJgAbg2Hx7fsxA9DmkzT5CHNBLk7+oEFn7UKILO1slsKeQJAfXdADuDQef30UlzyASeL2Gh4JmKmwrlKHMS1bpMvlFBBNcopX7QNhpVY6TGJuVKeGG6YotkmRDCA79eXRx3eUwJBALpnMTcw2aNlWzIww2JHB4aP9WtvhZxxD6LiRE3iV8I0AqspAQc2dAowNm+fqtLU6hKG+Cw8kvC/5DAIpPZI+Xk=";

    // 商户PID
    public static final String PARTNER = "2088421685287993";
    // 商户收款账号
    public static final String SELLER = "18958100066@189.cn";
    // API address
    public static final String USER_LOGIN = "http://192.168.58.21:8080/login?";
    public static final String GET_SERVER_INFO = "http://192.168.58.21:8080/list?";
    public static final String PREFIX_URL = "http://122.224.91.147:8888/vrsws/vappws.json";
}
