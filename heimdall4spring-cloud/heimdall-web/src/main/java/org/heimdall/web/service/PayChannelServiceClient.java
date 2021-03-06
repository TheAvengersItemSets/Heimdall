package org.heimdall.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.heimdall.common.util.MyBase64;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.heimdall.org
 */
@Service
public class PayChannelServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "selectPayChannelFallback")
    public String selectPayChannel(String jsonParam) {
        return restTemplate.getForEntity("http://HEIMDALL-SERVICE/pay_channel/select?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String selectPayChannelFallback(String jsonParam) {
        return "error";
    }

}