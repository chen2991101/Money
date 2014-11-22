package com.hao.money.util;

/**
 * 正则表达式验证
 * Created by hao on 2014/11/13.
 */
public class TestUtil {
    private static final String moneyMatch = "^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$";//金额的正则

    /**
     * 验证金额是否正确
     *
     * @param money
     * @return
     */
    public static boolean testMoney(String money) {
        System.out.println(money.matches(moneyMatch));
        return money.matches(moneyMatch);
    }
}
