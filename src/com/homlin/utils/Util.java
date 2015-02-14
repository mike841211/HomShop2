package com.homlin.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.homlin.app.exception.MessageException;

public class Util {

	private Util() {
	}

	/**
	 * 调试跟踪控制台输出
	 */
	public static void trace(Object... objects) {
		// System.out.println();
		// System.out.println("========== TRACE STRAT ==========");
		for (Object object : objects) {
			System.out.println(object);
		}
		// System.out.println("==========  TRACE END  ==========");
		// System.out.println();
	}

	public static void traceAsJson(Object... objects) {
		// System.out.println();
		// System.out.println("========== TRACE STRAT ==========");
		for (Object object : objects) {
			System.out.println(JacksonUtil.toJsonString(object));
		}
		// System.out.println("==========  TRACE END  ==========");
		// System.out.println();
	}

	/**
	 * 控制台输出当前时间戳
	 */
	public static void traceTime() {
		trace(getDateTimeNow());
	}

	/**
	 * 复制数据到剪贴板
	 * 
	 * @param data
	 */
	public static void copyToClipboard(String data) {
		copyToClipboard(data, false);
	}

	public static void copyToClipboard(String data, boolean sop) {
		if (sop) {
			System.out.println(data);
		}
		StringSelection selection = new StringSelection(data);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("\n=============== 已复制到剪贴板 ===============\n");
	}

	// ===========================

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Timestamp getDateTimeNow() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public static String md5(String password) {
		return DigestUtils.md5Hex(password);
	}

	public static String md5(String password, String salt) {
		return md5(password + "{" + salt + "}");
	}

	// public static String getRemoteIP() {
	// HttpServletRequest request = ServletActionContext.getRequest();
	// return IPUtil.getRemoteIP(request);
	// }

	/**
	 * 生成36位UUID
	 * 
	 * @return
	 */
	public static String generateKey() {
		return generateKey(true);
	}

	/**
	 * 生成32位UUID
	 * 
	 * @param replace
	 * @return
	 */
	public static String generateKey(boolean replace) {
		String key = java.util.UUID.randomUUID().toString();
		if (replace) {
			key = key.replace("-", "");
		}
		return key;
	}

	/**
	 * 去除重复项，a,b,c,d,d,c,e => a,b,c,d,e
	 * 
	 * @param arrayString
	 * @return
	 */
	public static String ArrayStringUnique(String arrayString) {
		Set<String> set = new HashSet<String>();
		String[] strings = arrayString.split(",");
		for (String string : strings) {
			set.add(string);
		}

		return StringUtils.join(set, ",");
	}

	/**
	 * 过滤HTML代码
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;

		Pattern p_html1;
		Matcher m_html1;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static boolean isEmpty(String str) {
		// return StringUtils.isEmpty(str);
		return str == null || str.trim().equals("") || str.trim().toLowerCase().equals("null");
	}

	// ********************************日期、字符串转换***************************************//
	public static Date stringToDate(String str, String fmt) {
		java.util.Date date = null;
		if (str == null || str.trim().equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			date = sdf.parse(str.trim());
		} catch (Exception exception) {
		}
		return date;
	}

	public static String dateToString(Date date, String fmt) {
		String strDate = null;
		if (date == null) {
			return strDate;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			strDate = sdf.format(date);
			return strDate;
		}
	}

	// ---

	public static String getDateTimeString(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getNowDateTimeString() {
		return dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	public static String getNowDateString() {
		return dateToString(new Date(), "yyyy-MM-dd");
	}

	// ********************************文件处理***************************************//
	private static boolean delFile(File file) {
		if (file.isFile()) {
			if (!file.delete()) {
				return false;
			}
		} else {
			File[] files = file.listFiles();
			if (files.length <= 0) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					if (!delFile(files[i])) {
						return false;
					}
				}
			}
			file.delete();
		}
		return true;
	}

	public static boolean delFile(String filename, boolean deldir) {
		File file = new File(filename);
		if (!file.exists()) {
			return true;
		}
		if (file.isDirectory()) {
			return true;
		}
		boolean b = delFile(file);
		if (!deldir) {
			return b;
		} else {
			File pfile = file.getParentFile();
			if (pfile.listFiles().length <= 0) {
				return pfile.delete();
			} else {
				return true;
			}
		}
	}

	public static boolean delFile(String filename) {
		return delFile(filename, false);
	}

	// public static String getFolder() {
	// String folder = AppConstants.UPLOAD_FOLDER;
	// if (folder.substring(0, 1).equals("*")) {
	// folder = folder.replace("\\", "/");
	// SimpleDateFormat sf = new SimpleDateFormat(folder.substring(1));
	// folder = sf.format(Calendar.getInstance().getTimeInMillis());
	// }
	// if (folder.substring(0, 1).equals("/")) {
	// folder = folder.substring(1);
	// }
	// return folder;
	// }
	//
	// /**
	// * 获取拓展名
	// *
	// * @param filename
	// * @return
	// */
	// public static String getExt(String filename) {
	// String ext = "";
	// if (filename != null && filename.length() > 0) {
	// int i = filename.lastIndexOf(".");
	// if (i > -1 && i < filename.length() - 1) {
	// ext = filename.substring(i + 1);
	// }
	// }
	// return ext;
	// }
	//
	// /**
	// * 获取源文件名
	// *
	// * @param filename
	// * @return
	// */
	// public static String getName(String filename) {
	// String name = "";
	// if (filename != null && filename.length() > 0) {
	// int i = filename.lastIndexOf(".");
	// if (i > -1 && i < filename.length() - 1) {
	// name = filename.substring(0, i);
	// } else {
	// name = filename;
	// }
	// }
	// return name;
	// }
	//
	// /**
	// * 获取新文件名
	// *
	// * @param filename
	// * @return
	// */
	// public static String getSaveName(String filename, String nameType) {
	// String newName = "";
	// String root = AppConstants.UPLOAD_ROOT;
	// String ext = getExt(filename);
	// if (StringUtils.isNotEmpty(ext)) {
	// if (nameType.equals("oldname")) {
	// String oldname = getName(filename);
	// int i = 1;
	// while (new File(root, getFolder() + "/" + filename).exists()) {
	// filename = oldname + "(" + i++ + ")" + "." + ext;
	// }
	// newName = filename;
	// } else if (nameType.equals("uuid")) {
	// newName = UUID.randomUUID().toString() + "." + ext;
	// } else if (nameType.equals("datetimernd")) {
	// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	// newName = sf.format(Calendar.getInstance().getTimeInMillis()) + (int) (Math.random() * 89999 + 10000) + "." + ext;
	// } else if (nameType.equals("timestamp")) {
	// newName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + "." + ext;
	// } else {
	// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	// newName = sf.format(Calendar.getInstance().getTimeInMillis()) + (int) (Math.random() * 89999 + 10000) + "." + ext;
	// }
	// }
	// return newName;
	// }

	// ********************************单据号处理***************************************//
	/**
	 * 字符串加一
	 * 
	 * @param string
	 * @return
	 */
	public static String increase(String string) {
		if (string == null) {
			return "";
		}
		String newString = "";
		int n = 0;
		char[] ch = string.toCharArray();
		boolean bool = true;
		for (int i = ch.length - 1; i >= 0; i--) {
			if (ch[i] == 122 || ch[i] == 90 || ch[i] == 57) {
				ch[i] = 48;
			} else {
				int k = (int) ch[i];
				if ((int) ch[i] >= 48 && (int) ch[i] < 57) {
					ch[i] = (char) (k + 1);
					bool = false;
				}
				if ((int) ch[i] >= 65 && (int) ch[i] < 90) {
					ch[i] = (char) (k + 1);
					bool = false;
				}
				if ((int) ch[i] >= 97 && (int) ch[i] < 122) {
					ch[i] = (char) (k + 1);
					bool = false;
				}
			}
			n++;
			newString = String.valueOf(ch[i]) + newString;
			if (!bool) { // 不进位
				break;
			}
		}
		newString = string.substring(0, string.length() - n) + newString;
		if (newString.compareTo(string) < 0) {
			newString = "1" + newString;
		}
		return newString;
	}

	public static void mapToExcel(HttpServletResponse response, final String title, List<Map<String, Object>> list
			, final Map<String, String> columnNames, final Map<Integer, Integer> columnWidths) throws Exception {
		if (list.size() == 0) {
			throw new MessageException("没有找到数据");
		}
		// 输出文件
		try {
			File file = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".xls");
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("Sheet1", 0);
			// 标题样式
			jxl.write.WritableCellFormat titleFormat = new jxl.write.WritableCellFormat();
			titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			titleFormat.setAlignment(Alignment.CENTRE); // 水平对齐
			titleFormat.setWrap(true); // 是否换行
			titleFormat.setBackground(Colour.GRAY_25);// 背景色暗灰-25%

			// 列标题
			int i = 0;
			for (String key : columnNames.keySet()) {
				sheet.addCell(new Label(i++, 0, columnNames.get(key), titleFormat));
			}

			// 列宽
			if (columnWidths != null) {
				for (int key : columnWidths.keySet()) {
					sheet.setColumnView(key, columnWidths.get(key));
				}
			}

			int r = 1;
			for (Map<String, Object> item : list) {
				sheet.setRowView(r, 300);
				int c = 0;
				for (String key : columnNames.keySet()) {
					try {
						sheet.addCell(new Label(c++, r, item.get(key).toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				r++;
			}
			// 写入数据并关闭文件
			book.write();
			book.close();

			// --------
			// 下载文件
			String fileName = title + Calendar.getInstance().getTimeInMillis() + ".xls"; // 文件的默认保存名
			fileName = new String(fileName.getBytes("gbk"), "iso8859-1"); // 中文问题
			// 读到流中
			InputStream inStream = new FileInputStream(file);
			// 设置输出的格式
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 循环取出流中的数据
			byte[] b = new byte[4096];
			int len;
			try {
				while ((len = inStream.read(b)) > 0) {
					response.getOutputStream().write(b, 0, len);
				}
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// --- 删除临时文件
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否微信浏览器
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWeixinBrowser(HttpServletRequest request) {
		String UserAgent = request.getHeader("User-Agent");
		// UserAgent = "Mozilla/5.0(iphone;CPU iphone OS 5_1_1 like Mac OS X) AppleWebKit/534.46(KHTML,like Geocko) Mobile/9B206 MicroMessenger/5.0";
		return UserAgent.indexOf("MicroMessenger") > -1;
	}

	/**
	 * 是否支持微信支付，仅限微信浏览器5.0及以上版本
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWxpayJsApiSupported(HttpServletRequest request) {
		String UserAgent = request.getHeader("User-Agent");
		int index = UserAgent.indexOf("MicroMessenger");
		if (index > -1) {
			try {
				int major = Integer.valueOf(UserAgent.substring(index + 15, index + 16)).intValue();
				return major >= 5;
			} catch (Exception e) {
			}
		}
		return false;
	}

}
