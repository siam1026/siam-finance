package com.siam.system.modular.mod_finance.api;

import com.siam.package_common.util.*;
import com.siam.system.modular.mod_finance.vo.SinaStock;
import com.siam.system.modular.mod_finance.vo.YiYuanData;
import com.siam.system.modular.mod_finance.entity.Stock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 股票/可转债模块相关api
 */
@Slf4j
@Service
@Data
public class StockApiService {

    @Value(value = "${yiYuanData.appcode}")
    private String yiYuanData_appcode;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 新浪 - 获取单个股票实时信息
     *
     * @return
     * @author JiangP
     */
    public SinaStock getSingleInfo(String stockCode, String stockName, String market) {
        SinaStock sinaStock;
        if(market != null){
            //sh--上海证券交易所，sz--深圳证券交易所
            String codeStr = market + stockCode;
            //1、通过代码进行匹配
            sinaStock = baseGetSingleInfo(codeStr);
        }else{
            //sh--上海证券交易所，sz--深圳证券交易所
            String shCodeStr = "sh" + stockCode;
            String szCodeStr = "sz" + stockCode;

            //1、通过代码进行匹配
            sinaStock = baseGetSingleInfo(shCodeStr);
            if(sinaStock == null){
                sinaStock = baseGetSingleInfo(szCodeStr);
            }else{
                //2、通过名称进行匹配
                String name = sinaStock.getName().replace("ST", "").replace("*ST", "");
                if(!stockName.contains(name)){
                    sinaStock = baseGetSingleInfo(szCodeStr);
                }
            }
        }
        return sinaStock;
    }

    /**
     * 新浪 - 获取单个股票实时信息 - 基础代码块
     *
     * @return
     * @author JiangP
     */
    public SinaStock baseGetSingleInfo(String stockCode) {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet("https://hq.sinajs.cn/list="+ stockCode);
        //解决接口返回Kinsoku jikou desu!问题
        httpGet.setHeader("Referer", "http://vip.stock.finance.sina.com.cn/");

        CloseableHttpResponse response = null;

        //使用HttpClient发起请求，获取response
        try {
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                if(!content.equals("jsonpgz();")){
                    String text = content.replace("var hq_str_" + stockCode + "=\"", "").replace("\";", "").replace("\n", "");
                    if(!text.isEmpty()){
                        String[] array = text.split(",");
                        SinaStock sinaStock = new SinaStock();
                        if(array.length == 33) {
                            //沪深，33位
                            sinaStock.setName(array[0]);
                            sinaStock.setPrice(BigDecimal.valueOf(Double.valueOf(array[3])));
                            sinaStock.setDate(DateUtilsPlus.strToDate(array[30] + " " + array[31]));
                            sinaStock.setYesterdayPrice(BigDecimal.valueOf(Double.valueOf(array[2])));
                            sinaStock.setResponseMessage(content);
                        }else if(array.length == 39){
                            //北交所，39位
                            sinaStock.setName(array[0]);
                            sinaStock.setPrice(BigDecimal.valueOf(Double.valueOf(array[3])));
                            sinaStock.setDate(DateUtilsPlus.strToDate(array[30] + " " + array[31]));
                            sinaStock.setYesterdayPrice(BigDecimal.valueOf(Double.valueOf(array[2])));
                            sinaStock.setResponseMessage(content);
                        }else if(array.length == 19){
                            //港股，19位
                            sinaStock.setName(array[1]);
                            sinaStock.setPrice(BigDecimal.valueOf(Double.valueOf(array[6])));
                            sinaStock.setDate(DateUtilsPlus.strToDate(array[17] + " " + array[18]));
                            sinaStock.setYesterdayPrice(BigDecimal.valueOf(Double.valueOf(array[3])));
                            sinaStock.setResponseMessage(content);
                        }else{
                            sinaStock = null;
                            log.debug("\n\n》》》 baseGetSingleInfo - stockCode = " + stockCode);
                        }
                        return sinaStock;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 易源数据 - 股票列表查询
     *
     * @return
     * @author JiangP
     */
    public List<Stock> getStockList() {
        List<Stock> list = new ArrayList<>();

        int page = 1;
        while (true){
            YiYuanData yiYuanData = baseList(null, String.valueOf(page));
            List<YiYuanData.StockInfo> contentList = yiYuanData.getContentlist();
            contentList.forEach(stockInfo -> {
                Stock stock = new Stock();
                BeanUtils.copyProperties(stockInfo, stock);
                stock.setProfitFour(stockInfo.getProfit_four());
                stock.setListingDate(DateUtilsPlus.parseDate(stockInfo.getListing_date()));
                list.add(stock);
            });
            if(yiYuanData.getAllPages().equals(page)){
                break;
            }
            page++;
            /*if(page == 6){
                break;
            }*/
        }

        //将列表数据存入redis
        redisUtils.set("stockList", JsonUtils.toJson(list));

        //TODO(MARK) - 测试使用
        /*List<Map<String, Object>> mapList = (List) JsonUtils.toObject(redisUtils.get("stockList").toString(), List.class);
        try {
            list = BeanUtils.mapsToObjects(mapList, Stock.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        return list;
    }

    /**
     * 易源数据 - 股票列表查询 - 基础代码块
     *
     * @return
     * @author JiangP
     */
    public YiYuanData baseList(String market, String page) {
        String host = "https://ali-stock.showapi.com";
        String path = "/stocklist";
        String method = "GET";
        String appcode = yiYuanData_appcode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("market", market);
        querys.put("page", page);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            String entity = EntityUtils.toString(response.getEntity());
            System.out.println(entity);
            Map map = JsonUtils.toMap(entity);
            YiYuanData yiYuanData = (YiYuanData) JsonUtils.toObject(JsonUtils.toJson(map.get("showapi_res_body")), YiYuanData.class);
            return yiYuanData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}