package com.timecat.data.bmob.data.own;

import java.util.Random;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/31
 * @description 兑换码
 * @usage null
 */
public class MiaoKey {

    public static String genRandomNum() {
        return genRandomNum(16);
    }

    /**
     * 生成随机密码
     *
     * @return 密码的字符串
     */
    public static String genRandomNum(int pwd_len) {
        int i; // 生成的随机数
        int count = 0; // 生成的密码的长度
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                      'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
                      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder pwd = new StringBuilder();
        Random r = new Random();
        while (count < pwd_len) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(str.length - 1)); // 生成的数最大为密码长度-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }
}
