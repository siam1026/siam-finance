package com.siam.system.modular.mod_finance.api;

import com.siam.package_common.util.HttpUtils;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.system.modular.mod_finance.entity.ConvertibleBond;
import com.siam.system.modular.mod_finance.vo.Jisilu;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金模块相关api
 */
@Service
public class ConvertibleBondApiService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 艾科瑞特 - 基金列表查询
     *
     * @return
     * @author JiangP
     */
    public List<ConvertibleBond> getConvertibleBondList() {
        List<ConvertibleBond> list = new ArrayList<>();

        Jisilu jisilu = baseList();
        List<Jisilu.ConvertibleBondInfo> contentList = jisilu.getRows();
        contentList.forEach(convertibleBondInfo -> {
            Jisilu.Cell cell = convertibleBondInfo.getCell();
            ConvertibleBond convertibleBond = new ConvertibleBond();
            convertibleBond.setName(cell.getBond_nm());
            convertibleBond.setCode(cell.getBond_id());
            convertibleBond.setLastestPrice(cell.getPrice());
            convertibleBond.setBondPy(cell.getBond_py());
            convertibleBond.setIncreaseRt(cell.getIncrease_rt());
            convertibleBond.setStockId(cell.getStock_id());
            convertibleBond.setStockNm(cell.getStock_nm());
            convertibleBond.setStockPy(cell.getStock_py());
            convertibleBond.setSprice(cell.getSprice());
            convertibleBond.setSincreaseRt(cell.getSincrease_rt());
            convertibleBond.setPb(cell.getPb());
            convertibleBond.setConvertPrice(cell.getConvert_price());
            convertibleBond.setConvertValue(cell.getConvert_value());
            convertibleBond.setPremiumRt(cell.getPremium_rt());
            convertibleBond.setRatingCd(cell.getRating_cd());
            convertibleBond.setPutConvertPrice(cell.getPut_convert_price());
            convertibleBond.setForceRedeemPrice(cell.getForce_redeem_price());
            convertibleBond.setConvertAmtRatio(cell.getConvert_amt_ratio());
            convertibleBond.setFundRt(cell.getFund_rt());
            convertibleBond.setShortMaturityDt(cell.getShort_maturity_dt());
            convertibleBond.setYearLeft(cell.getYear_left());
            convertibleBond.setCurrIssAmt(cell.getCurr_iss_amt());
            convertibleBond.setVolume(cell.getVolume());
            convertibleBond.setTurnoverRt(cell.getTurnover_rt());
            convertibleBond.setYtmRt(cell.getYtm_rt());
            convertibleBond.setConvertPriceTips(cell.getConvert_price_tips());
            list.add(convertibleBond);
        });

        //将列表数据存入redis
        redisUtils.set("convertibleBondList", JsonUtils.toJson(list));

        //TODO(MARK) - 测试使用
        /*List<Map<String, Object>> mapList = (List) JsonUtils.toObject(redisUtils.get("convertibleBondList").toString(), List.class);
        try {
            list = BeanUtils.mapsToObjects(mapList, ConvertibleBond.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        return list;
    }

    /**
     * 艾科瑞特 - 基金列表查询 - 基础代码块
     * 1）请求时需要更新Cookie，否则只能查询到前30条记录
     *
     * @return
     * @author JiangP
     */
    public Jisilu baseList() {
        String host = "https://www.jisilu.cn/data/cbnew/cb_list_new/?___jsl=LST___t=1644756144074";
        String path = "";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Cookie", "kbz_newcookie=1; kbzw_r_uname=syjp; kbzw__user_login=7Obd08_P1ebax9aX6tvg0ZiyoOjC49Tr6OfN18mimNyRrtjX3pTRxauw2cyporDH15rdqafdlNKarN6pod-Nso_rytvV0KOSppitraqZmqekt7e_1KLA59vZzeDapJ6nnJeKzcrl2-Lv1JCvyJmmmaecsoLNsM6tp6GBsdHk5drA3s7Cy-qQrKqqppSmgZzEvb3GuKOC4sri3JO_xtPM46KVrOHe5s_bkLCuoaiPpJetq6OgqozKw9zC6eCirZSnj6ev; kbzw__Session=053fat999hkm34mv6q39rebqj0; Hm_lvt_164fe01b1433a19b507595a43bf58262=1644743316,1644750105,1644756118; Hm_lpvt_164fe01b1433a19b507595a43bf58262=1644756118");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("is_search", "N");
        querys.put("market_cd[]", "shmb");
        querys.put("market_cd[]", "shkc");
        querys.put("market_cd[]", "szmb");
        querys.put("market_cd[]", "szcy");
        querys.put("listed", "N");
        querys.put("qflag", "N");
        querys.put("rp", "50");
        querys.put("page", "1");

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, new HashMap<>());
            System.out.println(response.toString());
            //获取response的body
            String entity = EntityUtils.toString(response.getEntity());
            System.out.println(entity);
            Jisilu jisilu = (Jisilu) JsonUtils.toObject(entity, Jisilu.class);
            return jisilu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}