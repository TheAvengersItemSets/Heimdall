package org.heimdall;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: xxpay支付核心服务,包括:各支付渠道接口,通知处理
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.heimdall.org
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HeimdallServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(HeimdallServiceApplication.class).web(true).run(args);
	}

}
