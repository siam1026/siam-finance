package com.siam.system.modular.mod_finance.api;

import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.JsonUtils;
import com.siam.system.modular.mod_finance.vo.TTFund;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 黄金模块相关api
 */
@Service
public class GoldApiService {

    /**
     * 天天基金 - 获取单个基金实时信息 - 转化为支付宝黄金价格
     *
     * @return
     * @author JiangP
     */
    public TTFund getSingleInfo(String fundCode) {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet("https://fundgz.1234567.com.cn/js/"+ fundCode +".js?rt=1463558676006");

        CloseableHttpResponse response = null;

        //使用HttpClient发起请求，获取response
        try {
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                if(!content.equals("jsonpgz();")){
                    String jsonText = content.replace("jsonpgz({", "{").replace("});", "}");
                    TTFund ttFund = (TTFund) JsonUtils.toObject(jsonText, TTFund.class);
                    ttFund.setJzrqDate(DateUtilsPlus.strToDate(ttFund.getJzrq()));
                    ttFund.setGztimeDate(DateUtilsPlus.strToDate(ttFund.getGztime()));
                    //金价转换，转换规则：相应基金的单位净值 * 285
                    ttFund.setGsz(ttFund.getGsz().multiply(BigDecimal.valueOf(285)).setScale(4, BigDecimal.ROUND_HALF_UP));
                    return ttFund;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}