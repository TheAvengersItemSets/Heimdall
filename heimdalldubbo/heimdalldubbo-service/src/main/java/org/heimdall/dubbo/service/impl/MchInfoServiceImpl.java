package org.heimdall.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.heimdall.common.domain.BaseParam;
import org.heimdall.common.enumm.RetEnum;
import org.heimdall.common.util.JsonUtil;
import org.heimdall.common.util.MyLog;
import org.heimdall.common.util.ObjectValidUtil;
import org.heimdall.common.util.RpcUtil;
import org.heimdall.dal.dao.model.MchInfo;
import org.heimdall.dubbo.api.service.IMchInfoService;
import org.heimdall.dubbo.service.BaseService;

import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
@Service(version = "1.0.0")
public class MchInfoServiceImpl extends BaseService implements IMchInfoService {

    private static final MyLog _log = MyLog.getLog(MchInfoServiceImpl.class);

    @Override
    public Map selectMchInfo(String jsonParam) {
        BaseParam baseParam = JsonUtil.getObjectFromJson(jsonParam, BaseParam.class);
        Map<String, Object> bizParamMap = baseParam.getBizParamMap();
        if (ObjectValidUtil.isInvalid(bizParamMap)) {
            _log.warn("查询商户信息失败, {}. jsonParam={}", RetEnum.RET_PARAM_NOT_FOUND.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_NOT_FOUND);
        }
        String mchId = baseParam.isNullValue("mchId") ? null : bizParamMap.get("mchId").toString();
        if (ObjectValidUtil.isInvalid(mchId)) {
            _log.warn("查询商户信息失败, {}. jsonParam={}", RetEnum.RET_PARAM_INVALID.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_INVALID);
        }
        MchInfo mchInfo = super.baseSelectMchInfo(mchId);
        if(mchInfo == null) return RpcUtil.createFailResult(baseParam, RetEnum.RET_BIZ_DATA_NOT_EXISTS);
        String jsonResult = JsonUtil.object2Json(mchInfo);
        return RpcUtil.createBizResult(baseParam, jsonResult);
    }
}