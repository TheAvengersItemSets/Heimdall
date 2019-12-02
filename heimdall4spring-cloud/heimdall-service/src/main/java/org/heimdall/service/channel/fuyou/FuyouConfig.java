package org.heimdall.service.channel.fuyou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RefreshScope
@Service
public class FuyouConfig {

    //接口名称
    private String service = "online_pay";
    //商户号
    private String customer_no;
    //通知 URL
    private String notify_url="http://127.0.0.1:8092/goods/payNotify";
    //返回 URL
    private String return_url="http://127.0.0.1:8092/goods/payNotify";
    //签名
    private String sign;
    //签名方式
    private String sign_type="MD5";
    //参数编码字符集
    private String charset = "UTF-8";
    //商品名称
    private String title;
    //商品描述
    private String body;
    //商户订单号
    private String order_no;
    //交易金额
    private Double total_fee;
    //支付类型
    private String payment_type = "1";
    //支付方式
    private String paymethod = "bankPay";
    //网银代码
    private String defaultbank = "QUICKPAY";
    //接入方式
    private String access_mode = "1";
    //卖家 Email
    private String seller_email="1520821958@qq.com";
    //买家 ID
    private String buyer_id;

    public FuyouConfig init(String configParam) {
        Assert.notNull(configParam, "init fuyou config error");
        JSONObject paramObj = JSON.parseObject(configParam);
        this.customer_no = paramObj.getString("mac_pay_id");
//        this.sign = paramObj.getString("sign");
//        this.sign_type = paramObj.getString("sign_type");
//        this.title = paramObj.getString("title");
//        this.body = paramObj.getString("body");
//        this.order_no = paramObj.getString("order_no");
//        this.total_fee = Double.parseDouble(paramObj.getString("total_fee"));
//        this.seller_email = paramObj.getString("seller_email");
//        this.buyer_id = paramObj.getString("buyer_id");

        return this;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Double total_fee) {
        this.total_fee = total_fee;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getDefaultbank() {
        return defaultbank;
    }

    public void setDefaultbank(String defaultbank) {
        this.defaultbank = defaultbank;
    }

    public String getAccess_mode() {
        return access_mode;
    }

    public void setAccess_mode(String access_mode) {
        this.access_mode = access_mode;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }
}
