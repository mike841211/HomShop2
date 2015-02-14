package test;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {

		System.out.println(("2014-07-09 00:00:00").replaceAll("-|\\s|:", ""));
		// // DateUtils.p
		// Util.stringToDate(str, fmt)

		// InputStream fis = UpmpConfig.class.getClassLoader().getResourceAsStream("config.properties");
		// if (null == fis) {
		// System.out.println(UpmpConfig.class.getClassLoader().getResource("").toString());
		// System.out.println(123);
		// return;
		// }
		// Properties properties = new Properties();
		// properties.load(fis);
		// System.out.println(properties.getProperty("upop.merNameh"));
		// System.out.println(new String(properties.getProperty("upop.merName").getBytes("ISO-8859-1"), "UTF-8"));

		// properties.list(System.out);
		// Properties p = PropertyHelper.getPropertyFile("config.properties");
		// p.load(fis);
		// PropertyResourceBundle props = new PropertyResourceBundle(fis);
		// System.out.println(props.getString("upop.merName"));
		// System.out.println(props.getString("upop.merCode"));
		// VERSION = props.getString(KEY_VERSION);
		// CHARSET = props.getString(KEY_CHARSET);
		// TRADE_URL = props.getString(KEY_TRADE_URL);
		// QUERY_URL = props.getString(KEY_QUERY_URL);
		// MER_ID = props.getString(KEY_MER_ID);
		// MER_BACK_END_URL = props.getString(KEY_MER_BACK_END_URL);
		// MER_FRONT_END_URL = props.getString(KEY_MER_FRONT_END_URL);
		// SIGN_TYPE = props.getString(KEY_SIGN_METHOD);
		// SECURITY_KEY = props.getString(KEY_SECURITY_KEY);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

		// java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		// java.util.Date currentTime = new java.util.Date();// 得到当前系统时间
		// String pDate = formatter.format(currentTime);
		//
		// RemoteAccessor remoteAccessor = new RemoteAccessor();
		// try {
		// String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		// "<upbp application=\"MGw.Req\" version =\"1.0.0\" sendTime =\"" + pDate + "\" sendSeqId =\"00000888888000\">" +
		// "<frontUrl>http://www.163.com</frontUrl><merchantOrderDesc></merchantOrderDesc><misc></misc>" +
		// "<gwType>01</gwType><transTimeout>ssssss</transTimeout><backUrl>http://192.168.1.156:8080/Test/Notify</backUrl>" +
		// "<merchantOrderCurrency>156</merchantOrderCurrency><merchantOrderAmt>1</merchantOrderAmt>" +
		// "<merchantOrderDesc>呵呵</merchantOrderDesc>" +
		// "<merchantId>630056832596</merchantId><merchantOrderTime>" + pDate + "</merchantOrderTime>" +
		// "<merchantOrderId>" + pDate + "</merchantOrderId><merchantUserId></merchantUserId>" +
		// "<mobileNum>15388157741</mobileNum>" +
		// "<cardNum></cardNum></upbp>";
		// BASE64Encoder encoder = new BASE64Encoder();
		// BASE64Decoder decoder = new BASE64Decoder();
		// String merchantId = encoder.encodeBuffer("630056832596".getBytes());
		// // String mm = MD5.getHashString("654321");
		// String mm = Util.md5("654321");
		// String keyPath = "d:/630056832596.pfx";
		// String privateKey = EncDecUtil.getCertKey("123456", keyPath);
		// String desKey = encoder.encodeBuffer(RSACoder.encryptByPrivateKey(mm.getBytes(), decoder.decodeBuffer(privateKey)));
		// byte[] bodyb = DesUtil2.encrypt(data.getBytes("utf-8"), mm.getBytes());
		// String miBody = encoder.encode(bodyb);
		// String xml = merchantId + "|" + desKey + "|" + miBody;
		// String re = remoteAccessor.getResponseByStream("http://58.246.136.11:8089/wapDetect/gateWay!gate.ac", "utf-8", xml);
		// System.out.println("返回结果：" + re);
		// String[] strArr = re.split("\\|");
		// byte[] de = decoder.decodeBuffer(strArr[1]);
		// byte[] b = DesUtil2.decrypt(de, mm.getBytes());
		// String content = new String(b, "utf-8");
		// System.out.println(content);
		// Document document = DocumentHelper.parseText(content);
		// Element upbp = document.getRootElement();
		// String redirectPage = upbp.elementText("gwInvokeCmd");
		// System.out.println("重定向地址：");
		// System.out.println(redirectPage);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}
