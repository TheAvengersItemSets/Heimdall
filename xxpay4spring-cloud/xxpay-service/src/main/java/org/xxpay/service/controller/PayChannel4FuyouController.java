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

import java.util.Map;
import java.util.UUID;

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
        //fuyouConfig.init(payChannel.getParam());

        FuyouRequest fuyouRequest = new FuyouRequest();
        fuyouRequest.setService(fuyouConfig.getService());
        fuyouRequest.setCustomer_no(UUID.randomUUID().toString().substring(0,10));
        fuyouRequest.setNotify_url(payOrder.getNotifyUrl());
        fuyouRequest.setReturn_url(payOrder.getNotifyUrl());
        fuyouRequest.setSign(payOrder.getBody());
        fuyouRequest.setSign_type(fuyouConfig.getSign_type());
        fuyouRequest.setCharset(fuyouConfig.getCharset());
        fuyouRequest.setTitle(fuyouConfig.getTitle());
        fuyouRequest.setBody(fuyouConfig.getBody());
        fuyouRequest.setOrder_no(fuyouConfig.getOrder_no());
        fuyouRequest.setTotal_fee(fuyouConfig.getTotal_fee());
        fuyouRequest.setPayment_type(fuyouConfig.getPayment_type());
        fuyouRequest.setPaymethod(fuyouConfig.getPaymethod());
        fuyouRequest.setDefaultbank(fuyouConfig.getDefaultbank());
        fuyouRequest.setAccess_mode(fuyouConfig.getAccess_mode());
        fuyouRequest.setSeller_email(fuyouConfig.getSeller_email());
        fuyouRequest.setBuyer_id(fuyouConfig.getBuyer_id());


        String payUrl = null;
        try {
            payUrl = FuyouPayUtil.httpPost(url,JSON.toJSONString(fuyouRequest));
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
