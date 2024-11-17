package com.siam.system.modular.mod_ocr;

import com.siam.package_common.util.GsonUtils;
import com.siam.system.modular.mod_finance.utils.Base64Util;
import com.siam.system.modular.mod_finance.utils.FileUtil;
import com.siam.system.modular.mod_finance.utils.HttpUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

/**
* 通用文字识别（高精度版）
*/
@Service
public class OCRParseService {

    /**
     * 获取解析出来的结果
     *
     * @return
     * @author JiangP
     */
    public List<Map<String, Object>> selectRecordList() throws InterruptedException {
        //实现过程：把要解析的图片扔到同一个文件夹，然后程序读取该文件夹下所有文件，根据买入时间升序排列输出
        List<Map<String, Object>> list = new ArrayList<>();
        String filepath = "C:\\Users\\Administrator\\Desktop\\fund\\"; //D盘下的file文件夹的目录
        File file = new File(filepath); //File类型可以是文件也可以是文件夹
        File[] fileList = file.listFiles(); //将该目录下的所有文件放置在一个File类型的数组中
        for (File dbFile : fileList) {
            Map<String, Object> map = fundOCR(dbFile.getAbsolutePath());
            list.add(map);
            /*System.out.println("买入金额：" + map.get("买入金额"));
            System.out.println("确认份额：" + map.get("确认份额"));
            System.out.println("确认净值：" + map.get("确认净值"));
            System.out.println("手续费：" + map.get("手续费"));
            System.out.println("买入时间：" + map.get("买入时间"));
            System.out.println("\n");*/
            Thread.sleep(500);
        }

        //按照买入时间对集合元素进行升序排列
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                System.out.println(o1.get("买入时间").toString() + " -- " + o2.get("买入时间").toString());
                boolean isPrevent = new Date(o1.get("买入时间").toString()).before(new Date(o2.get("买入时间").toString()));
                if(isPrevent){
                    return -1;
                }else{
                    return 1;
                }
                /*return 0;*/
            }
        });
        System.out.println(list);
        return list;
    }

    public Map<String, Object> fundOCR(String filePath){
        Map<String, Object> resultMap = new HashMap<>();
        String result = accurateBasic(filePath);
        Map<String, Object> map = GsonUtils.toMap(result);
        //获取属性列表
        List<Map<String, String>> list = (List<Map<String, String>>) map.get("words_result");
        String preWords = ""; //上一个属性名称
        for (Map<String, String> item : list) {
            String words = item.get("words");
            if(preWords.equals("买入金额")){
                words = words.replaceAll(",", "").replace("元", "");
                resultMap.put("买入金额", words);

            }else if(preWords.equals("确认份额")){
                words = words.replaceAll(",", "").replace("份", "");
                resultMap.put("确认份额", words);

            }else if(preWords.equals("确认净值")){
                words = words.replaceAll(",", "");
                resultMap.put("确认净值", words);

            }else if(preWords.equals("手续费")){
                words = words.replaceAll(",", "").replace("元", "");
                resultMap.put("手续费", words);

            }else if(preWords.equals("买入时间")){
                words = words.replaceAll("-", "/");
                words = words.substring(0, 10) + " " + words.substring(10); //substring()函数截取时不包含尾部、不包含首部

                //TODO(MARK)-异常情况处理
                if(words.equals("2020/11/06 14:4444")){
                    words = "2020/11/06 14:44:44";
                }else if(words.equals("2020/05/19 12:4207")){
                    words = "2020/05/19 12:42:07";
                }

                resultMap.put("买入时间", words);
            }
            preWords = words;
        }
        return resultMap;
    }

    /**
    * 重要提示代码中所需工具类
    * FileUtil,Base64Util,HttpUtil,GsonUtils请从
    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
    * 下载
    */
    public String accurateBasic(String filePath) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {
            // 本地文件路径
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}