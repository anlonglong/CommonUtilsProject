package com.cyou.xiyou.cyou.common.util;

public class HexUtil
{
    /**
     * 将字节数组转换成16进制字符串
     * @param bytes 目标字节数组
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[])
    {
        return bytesToHex(bytes, null);
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param bytes 目标字节数组
     * @param split 16进制字符串的分隔符
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[], String split)
    {
        return bytesToHex(bytes, 0, bytes.length, split);
    }

    /**
     * 将字节数组中指定区间的字节转换成16进制字符串
     * @param bytes 目标字节数组
     * @param start 起始位置（包括该位置）
     * @param end 结束位置（不包括该位置）
     * @param split 16进制字符串的分隔符
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[], int start, int end, String split)
    {
        StringBuilder sb = new StringBuilder();
        boolean hasSplit = split != null && !split.isEmpty();
        end = Math.min(end, bytes.length);

        for(int i = start; i < end; i++)
        {
            if(hasSplit && i > start)
            {
                sb.append(split);
            }

            sb.append(byteToHex(bytes[i]));
        }

        return sb.toString();
    }

    /**
     * 将单个字节码转换成16进制字符串
     * @param b 目标字节
     * @return 转换结果
     */
    public static String byteToHex(byte b)
    {
        String hex = Integer.toHexString(0xFF & b);
        return b >= 0 && b <= 15? '0' + hex: hex;
    }
}