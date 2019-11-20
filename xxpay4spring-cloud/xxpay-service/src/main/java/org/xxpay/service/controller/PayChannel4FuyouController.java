package org.xxpay.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.common.constant.PayEnum;
import org.xxpay.common.util.MyBase64;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.XXPayUtil;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayChannel;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.service.channel.fuyou.FuyouConfig;
import org.xxpay.service.channel.fuyou.FuyouPayUtil;
import org.xxpay.service.channel.fuyou.FuyouRequest;
import org.xxpay.service.service.MchInfoService;
import org.xxpay.service.service.PayChannelService;
import org.xxpay.service.service.PayOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PayChannel4FuyouController {

    private final MyLog _log = MyLog.getLog(PayChannel4FuyouController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private FuyouConfig fuyouConfig;

    @Autowired
    private MchInfoService mchInfoService;

    private static String url = "http://epay.hyhope.top/portal";
    private static String customer_no = "100000000065113";
    private static String pwd = "11122a6d6e9a4f85660aba3b3d1b665e67g98453627b8a323a94a314a476gac6";

    @RequestMapping(value = "/pay/channel/fuyou_wap")
    public String doFuyouWapReq(@RequestParam String jsonParam) {
        String logPrefix = "【富友WAP支付下单】";
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
        String payOrderId = payOrder.getPayOrderId();
        String mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();
        MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
        String resKey = mchInfo == null ? "" : mchInfo.getResKey();
        if("".equals(resKey)) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        PayChannel payChannel = payChannelService.selectPayChannel(channelId, mchId);
        String str = payChannel.getParam();
        fuyouConfig.init(payChannel.getParam());


        FuyouRequest fuyouRequest = new FuyouRequest();
        fuyouRequest.setService(fuyouConfig.getService());
        fuyouRequest.setCustomer_no(customer_no);
        fuyouRequest.setNotify_url(fuyouConfig.getNotify_url());
        fuyouRequest.setReturn_url(fuyouConfig.getReturn_url());

        fuyouRequest.setCharset(fuyouConfig.getCharset());
        fuyouRequest.setTitle(payOrder.getSubject());
        fuyouRequest.setBody(payOrder.getBody());
        fuyouRequest.setOrder_no(payOrder.getPayOrderId());
        fuyouRequest.setTotal_fee(payOrder.getAmount().toString());
        fuyouRequest.setPayment_type(fuyouConfig.getPayment_type());
        fuyouRequest.setPaymethod(fuyouConfig.getPaymethod());
        fuyouRequest.setDefaultbank(fuyouConfig.getDefaultbank());
        fuyouRequest.setAccess_mode(fuyouConfig.getAccess_mode());
        fuyouRequest.setSeller_email(fuyouConfig.getSeller_email());
        fuyouRequest.setBuyer_id("123456789");


        String sign = "";
//        Map<String,String> parmsMap = new HashMap<>();
//        try {
//            parmsMap = FuyouPayUtil.convertBean(fuyouRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final List<Map.Entry<String, String>> list = FuyouPayUtil.sortMap(parmsMap);

//        parmsMap = FuyouPayUtil.sortMapByKey(parmsMap);


        String parmsString = null;
        try {
            parmsString = FuyouPayUtil.ObjectToString(fuyouRequest);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            sign = FuyouPayUtil.MD5(parmsString + pwd);
        }catch (Exception e){
            e.printStackTrace();
        }

        fuyouRequest.setSign(sign);
        fuyouRequest.setSign_type(fuyouConfig.getSign_type());

        String payUrl = null;
        try {
            Map<String,String> requestMap = FuyouPayUtil.ObjectToString2(fuyouRequest);
            payUrl = FuyouPayUtil.httpFromPost(url,requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _log.info("{}生成跳转路径：payUrl={}", logPrefix, payUrl);
        payOrderService.updateStatus4Ing(payOrderId, null);
        _log.info("{}生成请求支付宝数据,req={}", logPrefix, JSON.toJSONString(fuyouRequest));
        _log.info("###### 商户统一下单处理完成 ######");
        Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
        map.put("payOrderId", payOrderId);
        map.put("payUrl", payUrl);
        return XXPayUtil.makeRetData(map, resKey);
    }

//    public static getSignString(){
//
//    }


}
