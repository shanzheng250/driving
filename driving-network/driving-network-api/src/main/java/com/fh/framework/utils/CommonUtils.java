package com.fh.framework.utils;

import com.fh.framework.constant.Constant;
import com.fh.framework.constant.NumberContant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


public class CommonUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	public static final Long SYSTEM_TIME_BS=1000L;


	/**
	 * 字符串转化为long类型数组
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static Long[] StrToLongArray(String sourceStr) {
		String[] strArray = sourceStr.split(",");
		Long[] array = new Long[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			array[i] = Long.parseLong(strArray[i]);
		}
		return array;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmptyString(String str) {
		return !isNullStr(str);
	}

	/**
	 * @param date   long型
	 * @param format 时间格式yyyy-MM-dd HH:mm:ss有用户自行制定
	 * @return 时间long型按照某个格式转化为时间字符串
	 */
	public static String getTimeLongToString(long date, String format) {
		Timestamp t = new Timestamp(date * Constant.DTIME);
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		return sDateFormat.format(t);
	}

	/**
	 * @param date long型
	 * @return 时间long型转化为字符：yyyy-MM-dd HH:mm:ss
	 */
	public static String getTimeLongToString(long date) {
		Timestamp t = new Timestamp(date * Constant.DTIME);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sDateFormat.format(t);
	}

	/**
	 * 获取当前时间的绝对秒数
	 * @return
	 */
	public static Long getCurrentTimeSeconds() {
		return System.currentTimeMillis()/Constant.DTIME;
	}
	
	public static String getTimeLongToString(String date) {
		return getTimeLongToString(Long.valueOf(date));
	}

	/**
	 * @param dateStr yyyy-MM-dd HH:mm:ss
	 * @return 时间字符型转化为long型
	 */
	public static long getTimeStringToLong(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long date = 0;
		try {
			Date d = sdf.parse(dateStr);
			date = d.getTime() / Constant.DTIME;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}

	/**
	 * @param dateStr yyyy-MM-dd HH:mm:ss
	 * @param format  指定时间格式例如：yyyy-MM-dd HH:mm:ss
	 * @return 时间字符型转化为long型
	 */
	public static long getTimeStringToLong(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long date = 0;
		try {
			Date d = sdf.parse(dateStr);
			date = d.getTime() / Constant.DTIME;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}

	/**
	 * 判断字符串是否为空或null
	 * 
	 * @param str 需要判断是否为空的字符串
	 * @return 如果字符串为空,则返回true;如果字符串非空,则返回false
	 */
	public static boolean isNullStr(String str) {
		if (str == null || "".equals(str.trim()) || "NULL".equals(str.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param str 字符
	 * @return 把字符转化为int
	 */
	public static long getStr2Long(String str) {
		Long res = 0L;
		try {
			res = Long.parseLong(str);
		} catch (Exception e) {
			res = 0L;
		}
		return res;
	}

	/**
	 * long IP转String IP 转换主机序
	 * 
	 * @param IP long ip地址
	 * @return 返回字符串ip地址
	 */
	public static String getIPLong2Str(long IP) {
		String rtn = "";
		try {
			byte[] by = new byte[NumberContant.NUMBER_4];

			InetAddress address = InetAddress.getByAddress(long2byte(by, IP, 0));
			rtn = address.getHostAddress();
			// 倒序
			StringBuffer sb = new StringBuffer();
			String[] ids = parseToken2String(rtn, ".");
			// 应该是192.168.0.223
			sb.append(ids[NumberContant.NUMBER_3]);
			sb.append(".");
			sb.append(ids[NumberContant.NUMBER_2]);
			sb.append(".");
			sb.append(ids[1]);
			sb.append(".");
			sb.append(ids[0]);
			rtn = sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rtn;
	}

	public static byte[] long2byte(byte[] out, long in, int offset) {
		if (out.length > 0) {
			out[offset] = (byte) in;
		}
		if (out.length > 1) {
			out[offset + 1] = (byte) (in >>> NumberContant.NUMBER_8);
		}
		if (out.length > NumberContant.NUMBER_2) {
			out[offset + NumberContant.NUMBER_2] = (byte) (in >>> NumberContant.NUMBER_16);
		}
		if (out.length > NumberContant.NUMBER_3) {
			out[offset + NumberContant.NUMBER_3] = (byte) (in >>> NumberContant.NUMBER_24);
		}
		if (out.length > NumberContant.NUMBER_4) {
			out[offset + NumberContant.NUMBER_4] = (byte) (in >>> NumberContant.NUMBER_32);
		}
		if (out.length > NumberContant.NUMBER_5) {
			out[offset + NumberContant.NUMBER_5] = (byte) (in >>> NumberContant.NUMBER_40);
		}
		if (out.length > NumberContant.NUMBER_6) {
			out[offset + NumberContant.NUMBER_6] = (byte) (in >>> NumberContant.NUMBER_48);
		}
		if (out.length > NumberContant.NUMBER_7) {
			out[offset + NumberContant.NUMBER_7] = (byte) (in >>> NumberContant.NUMBER_56);
		}
		return out;
	}

	public static String[] parseToken2String(String str, String delim) {
		String[] strRtn = null;
		StringTokenizer st = new StringTokenizer(str, delim);
		int length = st.countTokens();
		strRtn = new String[length];
		int i = 0;
		while (st.hasMoreTokens()) {
			String str1 = st.nextToken().trim();
			strRtn[i] = str1;
			i++;
		}
		return strRtn;
	}

	/**
	 * 判断是否是整数值
	 * 
	 * @param v
	 * @return
	 */
	public static boolean isDigit(String v) {
		return null != v && v.matches("^-?\\d+$");
	}

	/**
	 * 对含有html的特殊字符进行过滤
	 * 
	 * @param value 需要处理的字符串
	 * @return 字符串
	 */
	public static String htmlSpecialChars(String value) {
		if (value == null){
			return "";
		}
		return value.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("\'", "&#039;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	/**
	 * @param str 字符
	 * @return 把字符转化为int
	 */
	public static long getStr2BLong(String str) {

		Long res = -1L;

		try {

			res = Long.parseLong(str);

		} catch (Exception e) {

			res = -1L;

		}
		return res;
	}

	/**
	 * 对null字符串的处理为空
	 * 
	 * @param str 需要处理的字符串
	 * @return 返回结果
	 */
	public static String delNull(String str) {
		if (isNullStr(str)) {
			str = "";
		}
		return str.trim();
	}

	public static Long delNull(Long l) {
		if (l == null) {
			return -1l;
		}
		return l;

	}


	/**
	 * 判断Long是否为null或小于等于0
	 * 
	 * @param
	 * @return 如果字符串为null或小于等于0,则返回true;如果字符串非空,则返回false
	 */
	public static boolean isNullStr(Long num) {
		if (num == null || num <= 0) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public static void notNull(Object target, String mess) {
		if (target == null || ((target instanceof String) && isNullStr((String) target))
				|| (target instanceof List) && ((List) target).size() == 0
				|| (target instanceof Map) && ((Map) target).size() == 0
				|| (target instanceof Set) && ((Set) target).size() == 0) {
			throw new RuntimeException(mess);
		}
	}

	/**
	 * Object对象转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObject2String(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 
	 * 判断列表是否为空（为 null 或 没有任何内容）
	 * 
	 * @param list 待检查的列表
	 * @return 为空 == true，不为空 == false
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyList(List list) {
		return (list == null || list.isEmpty()) ? true : false;
	}

	/**
	 * @param str 字符
	 * @return 把字符转化为int
	 */
	public static int getStr2Int(String str) {
		int res = 0;
		try {
			res = Integer.parseInt(str);
		} catch (Exception e) {
			res = 0;
		}
		return res;
	}

	public static long getCurTime() {

		return System.currentTimeMillis()/SYSTEM_TIME_BS;
	}
}