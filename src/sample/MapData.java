package sample;

import java.util.HashMap;

public class MapData {
    public static HashMap<String, String> map_tableTitle = new HashMap();

    static {
        map_tableTitle.put("ID", "ID");
        map_tableTitle.put("STOCK_ID", "股票ID");
        map_tableTitle.put("NAME", "股票名稱");
        map_tableTitle.put("BUY", "買入");
        map_tableTitle.put("SELL", "賣出");
        map_tableTitle.put("TAX", "手續費+交易稅");
        map_tableTitle.put("NUMBER_OF_SHARES", "交易股數");
        map_tableTitle.put("PROFIT_LOSS", "損益");
        map_tableTitle.put("PERCENT_PROFIT_LOSS", "損益(%)");
        map_tableTitle.put("TIME", "記入日期");
        map_tableTitle.put("REMARKS", "買/賣ID");
    }
}
