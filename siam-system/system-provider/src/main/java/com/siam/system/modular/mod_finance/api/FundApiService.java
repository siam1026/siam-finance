package com.siam.system.modular.mod_finance.api;

import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.HttpUtils;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.system.modular.mod_finance.vo.Icredit;
import com.siam.system.modular.mod_finance.vo.TTFund;
import com.siam.system.modular.mod_finance.entity.Fund;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金模块相关api
 */
@Service
public class FundApiService {

    @Value(value = "${icredit.appcode}")
    private String icredit_appcode;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 天天基金 - 获取单个基金实时信息
     * 1）当天晚上无法获取最新净值，只能取到最新估算净值
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
                    if (StringUtils.isNotBlank(jsonText)){
                        TTFund ttFund = (TTFund) JsonUtils.toObject(jsonText, TTFund.class);
                        ttFund.setJzrqDate(DateUtilsPlus.strToDate(ttFund.getJzrq()));
                        ttFund.setGztimeDate(DateUtilsPlus.strToDate(ttFund.getGztime()));
                        return ttFund;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 艾科瑞特 - 基金列表查询
     *
     * @return
     * @author JiangP
     */
    public List<Fund> getFundList() {
        List<Fund> list = new ArrayList<>();

        Icredit icredit = baseList();
        List<Icredit.FundInfo> contentList = icredit.get基金数据实体信息();
        contentList.forEach(fundInfo -> {
            Fund fund = new Fund();
            fund.setName(fundInfo.get基金简称());
            fund.setCode(fundInfo.get基金代码());
            list.add(fund);
        });

        //将列表数据存入redis
        redisUtils.set("fundList", JsonUtils.toJson(list));

        //TODO(MARK) - 测试使用
        /*List<Map<String, Object>> mapList = (List) JsonUtils.toObject(redisUtils.get("fundList").toString(), List.class);
        try {
            list = BeanUtils.mapsToObjects(mapList, Fund.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        return list;
    }

    /**
     * 艾科瑞特 - 基金列表查询 - 基础代码块
     *
     * @return
     * @author JiangP
     */
    public Icredit baseList() {
        String host = "http://iftrade.market.alicloudapi.com";
        String path = "/ai_fintech/ai_fund/fund_trade/search_fund_id_list";
        String method = "GET";
        String appcode = icredit_appcode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            String entity = EntityUtils.toString(response.getEntity());
            System.out.println(entity);
            Map map = JsonUtils.toMap(entity);
            Icredit icredit = (Icredit) JsonUtils.toObject(JsonUtils.toJson(map), Icredit.class);
            return icredit;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}