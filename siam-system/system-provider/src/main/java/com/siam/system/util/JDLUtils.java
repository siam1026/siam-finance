package com.siam.system.util;

import com.alibaba.fastjson.JSONObject;
import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.*;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import com.lop.open.api.sdk.request.ECAP.EcapV1OrdersCreateLopRequest;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersCreateLopResponse;
import com.siam.package_common.util.DateUtilsPlus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 京东物流开放 API
 */
@Slf4j
@Component
@Configuration
public class JDLUtils {

    @Value("${jdl.key:siam1026@163.com}")
    private String key;
    @Value("${jdl.secret:siam1026@163.com}")
    private String secret;
    @Value("${jdl.accessToken:siam1026@163.com}")
    private String accessToken;
    @Value("${jdl.customerCode:siam1026@163.com}")
    private String customerCode;
    @Value("${jdl.domain:siam1026@163.com}")
    private String domain;
    @Value("${jdl.api_url.ordersCreate:siam1026@163.com}")
    private String ordersCreate;
    @Value("${jdl.api_url.precheck:siam1026@163.com}")
    private String precheck;

    public LopPlugin commonParams() {
        //设置插件，必须的操作，不同类型的应用入参不同，请看入参注释，公共参数按顺序分别为AppKey、AppSecret、AccessToken
        //使用开放平台ISV/自研商家应用调用接口
        LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(key, secret, accessToken);
        //LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin("35e0e97c7af74575b7969a6db58c1a6a", "ae76e7f2a1274b3093013c969c9722ac", "d9dccd683f944ca78dfa31547c6c93ad");
        return lopPlugin;
    }

    public static JSONObject jsonDataAnalysis(JSONObject params, JSONObject result) {
        log.info("\n京东物流API接口请求参数={}\n京东物流API接口返回参数={}", params, result);
        JSONObject result_data = new JSONObject();
        int code=result.getInteger("code");
        if (code == 0) {
            JSONObject data = result.getJSONObject("data");
            result_data.put("success", result.getBoolean("success"));
            result_data.put("code", code);
            result_data.put("message", result.getString("msg"));
            result_data.put("data", data);
            return result_data;
        }
        result_data.put("success", false);
        result_data.put("code", result.getInteger("code"));
        result_data.put("message", result.getString("msg") + "（" + result.getString("subMsg") + "）");
        return result_data;
    }

    /**
     * 下单前置校验 (commonCheckPreCreateOrderV1)
     * 1、接口概述：下单前想确认京东的配送服务能否满足需求，可以使用这个接口进行必要的信息校验。
     * 2、适用场景：
     * ① 了解京东可以支持揽收、派送地址范围；
     * ② 可下单产品、 预估时效、运费等信息。
     * 3、功能描述：
     * ① 校验配送范围：只需输入发货和收货地址，接口会确认这些地址是否在京东物流的服务范围内。校验通过后，接口还会返回揽收时间范围、支持的产品以及预计送达时间；
     * ② 查询费用信息：若入参传入了「cargoes-货品信息」和「productsReq-产品信息」，则接口校验客户编码下的产品信息，校验通过后出参返回揽收时间范围、支持的产品、预计送达时间以及预估运费；如青龙业主编码（k码）配置的折扣类型是固定折扣，支持查询折后价格。（价格会根据货品重量，选择的产品，增值产品的信息不同而有差异）
     *
     * @param senderContact 寄件人信息
     * @param receiverContact 收件人信息
     * @param orderOrigin 订单来源
     * @param productCode 主产品编码
     * @param goods 商品信息；商品的基础信息，orderOrigin为 2 时必填；orderOrigin为 0 和 1 时不传此参数；此参数用于计算费用
     * @return
     */
    /*public JSONObject commonCheckPreCreateOrderV1(Contact senderContact, Contact receiverContact, int orderOrigin, String productCode, List<CommonGoodsInfo> goods) throws LopException {
        //入参对象（请记得更换为自己要使用的接口入参对象）
        EcapV1OrdersPrecheckLopRequest request = new EcapV1OrdersPrecheckLopRequest();

        //设置入参（请记得更换为自己要使用的接口入参）
        CommonCreateOrderRequest requestDTO = new CommonCreateOrderRequest();
        requestDTO.setOrderOrigin(orderOrigin);
        if(orderOrigin==1||orderOrigin==2){
            requestDTO.setCustomerCode("021K3671004");
        }

        CommonProductInfo productInfo = new CommonProductInfo();
        productInfo.setProductCode(productCode);
        if(!goods.isEmpty()&&orderOrigin==2){
            requestDTO.setGoods(goods);
        }

        requestDTO.setProductsReq(productInfo);
        requestDTO.setReceiverContact(receiverContact);
        requestDTO.setSenderContact(senderContact);
        request.setRequest(requestDTO);

        LopPlugin lopPlugin = commonParams();
        request.addLopPlugin(lopPlugin);
        EcapV1OrdersPrecheckLopResponse response = client.execute(request);
        System.out.println(response.getMsg());
        //jsonDataAnalysis(JSONObject.parseObject(request.get()),response.getResult());
        return null;
    }*/

    /**
     * 1、接口概览：用于京东快递和快运的通用下单接口。
     * 2、适用场景：适用于多种业务场景，包括但不限于：
     * ① 京东快递B2C下单（月结下单场景）；
     * ② 京东快递C2C下单（现结下单场景）；
     * ③ 京东快运B2C下单（月结下单场景）；
     * ④ 京东快运C2C下单（现结下单场景）；
     * ⑤ 京东快递C2B下单  (线上洗护，回收等取件场景）;
     * 3、功能描述：该接口为用户提供了四项核心功能：
     * ① 客户系统向京东物流下发订单；
     * ② 为订单分配运单号；（订单号需要唯一，同一订单号重复调用时会返回相同结果）
     * ③ 京东物流提供预估的时效、费用等信息。（此信息为预估信息，实际以账单为准）
     * ④ 支持带运单号下单，（①预制条码下单（需要找京东营业部要预制条码的物料） ②获取运单号接口获取到的运单号，接口链接：https://cloud.jdl.com/#/open-business-document/api-doc/267/1288）
     *
     * @param senderContact   寄件人信息
     * @param receiverContact 收件人信息
     * @param orderOrigin     订单来源
     * @param productCode     主产品编码
     * @param goods           商品信息；商品的基础信息，orderOrigin为 2 时必填；orderOrigin为 0 和 1 时不传此参数；此参数用于计算费用
     * @return
     */
    public JSONObject commonCreateOrderV1(String orderId, Contact senderContact,
                                          Contact receiverContact,
                                          int orderOrigin, String productCode, List<CommonGoodsInfo> goods,
                                          CommonCargoInfo commonCargoInfo, int pickupType, String pickupStartTime, String pickupEndTime,
                                          int deliveryType, String expectDeliveryStartTime, String expectDeliveryEndTime, String remark) throws LopException {
        //入参对象（请记得更换为自己要使用的接口入参对象）
        EcapV1OrdersCreateLopRequest request = new EcapV1OrdersCreateLopRequest();
        CommonCreateOrderRequest requestDTO = new CommonCreateOrderRequest();
        requestDTO.setOrderId(orderId);
        requestDTO.setReceiverContact(receiverContact);
        requestDTO.setSenderContact(senderContact);

        //产品信息:京东标快还是京东特快
        CommonProductInfo commonProductInfo = new CommonProductInfo();
        commonProductInfo.setProductCode(productCode);//京东标快
        requestDTO.setProductsReq(commonProductInfo);

        //下单来源:
        requestDTO.setOrderOrigin(orderOrigin);//1:电商平台的商家（即京东物流的签约商家）发给C端用户的快递服务，或者企业发C端、B端，但重量（泡重比）小于30kg的业务
        //客户编码 ，orderOrigin为 1 或者 2 时必填；orderOrigin为0时不要传此参数；与京东物流签约后生成，长度1-32
        if (orderOrigin == 1 || orderOrigin == 2) {
            requestDTO.setCustomerCode(customerCode);
        }
        requestDTO.setSettleType(3);//设置付款方式，3为月结

        List<CommonCargoInfo> commonCargoInfos = Arrays.asList(commonCargoInfo);
        requestDTO.setCargoes(commonCargoInfos);

        if (orderOrigin == 1) {
            CommonChannelInfo commonChannelInfo = new CommonChannelInfo();
            commonChannelInfo.setChannelCode("0030001");
            requestDTO.setCommonChannelInfo(commonChannelInfo);
        }

        requestDTO.setPickupType(pickupType);
        requestDTO.setDeliveryType(deliveryType);
        requestDTO.setPickupStartTime(DateUtilsPlus.timestampExample(pickupStartTime));
        requestDTO.setPickupEndTime(DateUtilsPlus.timestampExample(pickupEndTime));
        requestDTO.setExpectDeliveryStartTime(DateUtilsPlus.timestampExample(expectDeliveryStartTime));
        requestDTO.setExpectDeliveryEndTime(DateUtilsPlus.timestampExample(expectDeliveryEndTime));
        requestDTO.setRemark(remark);
        request.setRequest(requestDTO);

        //设置接口域名(有的对接方案同时支持生产和沙箱环境，有的仅支持生产，具体以对接方案中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 预发环境域名：https://uat-api.jdl.com
        //DefaultDomainApiClient对象全局只需要创建一次
        DefaultDomainApiClient client = new DefaultDomainApiClient(domain, 500, 15000);

        LopPlugin lopPlugin = commonParams();
        request.addLopPlugin(lopPlugin);

        EcapV1OrdersCreateLopResponse response = client.execute(request);
        log.info("京东下单接口返回信息：{}", response.getMsg());
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(request.getRequest()), JSONObject.class);
        JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(response.getResult()), JSONObject.class);
        JSONObject jsonDataAnalysis = jsonDataAnalysis(params, result);
        JSONObject resultObj = new JSONObject();
        resultObj.put("params", params);
        resultObj.put("result", jsonDataAnalysis);
        return resultObj;
    }

    @Test
    public void main() {

        //示例为调用京东快递下单前置校验接口
        try {
            //commonCheckPreCreateOrderV1("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}