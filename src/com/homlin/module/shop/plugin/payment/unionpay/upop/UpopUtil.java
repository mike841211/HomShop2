package com.homlin.module.shop.plugin.payment.unionpay.upop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.TreeMap;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class UpopUtil {

	/**
	 * 生成发送银联报文页面
	 * 
	 * @param map
	 * @param signature
	 * @return
	 */
	public static String createPayHtml(String[] valueVo, String signType) {
		return createPayHtml(valueVo, null, signType);
	}

	/**
	 * 直接跳转银行支付页面
	 * 
	 * @param map
	 * @param signature
	 * @return
	 */
	public static String createPayHtml(String[] valueVo, String bank, String signType) {

		Map<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < UpopConfig.reqVo.length; i++) {
			map.put(UpopConfig.reqVo[i], valueVo[i]);
		}

		map.put("signature", signMap(map, signType));
		map.put("signMethod", signType);
		if (bank != null && !"".equals(bank)) {
			map.put("bank", bank);
		}

		String payForm = generateAutoSubmitForm(UpopConfig.gateWay, map);

		return payForm;
	}

	public static String createBackStr(String[] valueVo, String[] keyVo) {

		Map<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < keyVo.length; i++) {
			map.put(keyVo[i], valueVo[i]);
		}
		map.put("signature", signMap(map, UpopConfig.signType));
		map.put("signMethod", UpopConfig.signType);
		return joinMapValue(map, '&');
	}

	/**
	 * 查询验证签名
	 * 
	 * @param valueVo
	 * @return 0:验证失败 1验证成功 2没有签名信息（报文格式不对）
	 */
	public static int checkSecurity(String[] valueVo) {
		Map<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < valueVo.length; i++) {
			String[] keyValue = valueVo[i].split("=");
			map.put(keyValue[0], keyValue.length >= 2 ? valueVo[i].substring(keyValue[0].length() + 1) : "");
		}
		if ("".equals(map.get("signature"))) {
			return 2;
		}
		String signature = map.get("signature");
		boolean isValid = false;
		if (UpopConfig.signType.equalsIgnoreCase(map.get("signMethod"))) {
			map.remove("signature");
			map.remove("signMethod");
			isValid = signature.equals(md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey)));
		} else {
			isValid = verifyWithRSA(map.get("signMethod"), md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey)), signature);
		}

		return (isValid ? 1 : 0);
	}

	/**
	 * 生成加密钥
	 * 
	 * @param map
	 * @param secretKey
	 *            商城密钥
	 * @return
	 */
	private static String signMap(Map<String, String> map, String signMethod) {
		if (UpopConfig.signType.equalsIgnoreCase(signMethod)) {
			return md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey));
		} else {
			return signWithRSA(md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey)));
		}
	}

	private static String signWithRSA(String signData) {
		String privateKeyPath = "D:/work/Test/data/upop_private.key";
		ObjectInputStream priObjectIs = null;
		try {
			priObjectIs = new ObjectInputStream(new FileInputStream(privateKeyPath));
			PrivateKey privateKey = PrivateKey.class.cast(priObjectIs.readObject());
			Signature dsa = Signature.getInstance(UpopConfig.signType_SHA1withRSA);
			dsa.initSign(privateKey);
			dsa.update(signData.getBytes(UpopConfig.charset));
			byte[] signature = dsa.sign();
			BASE64Encoder base64Encoder = new BASE64Encoder();
			return base64Encoder.encode(signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (priObjectIs != null) {
				try {
					priObjectIs.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static boolean verifyWithRSA(String algorithm, String signData, String signature) {
		String publicKeyPath = "D:/work/Test/data/upop.cer";
		ObjectInputStream pubObjectIs = null;
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			InputStream in = new FileInputStream(publicKeyPath);
			Certificate cert = factory.generateCertificate(in);
			PublicKey publicKey = cert.getPublicKey();
			Signature signCheck = Signature.getInstance(UpopConfig.signType_SHA1withRSA);
			signCheck.initVerify(publicKey);
			signCheck.update(signData.getBytes(UpopConfig.charset));
			BASE64Decoder base64Decoder = new BASE64Decoder();
			return signCheck.verify(base64Decoder.decodeBuffer(signature));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pubObjectIs != null) {
				try {
					pubObjectIs.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param map
	 * @param secretKey
	 *            商城密钥
	 * @return
	 */
	public static boolean checkSign(String[] valueVo, String signMethod, String signature) {

		Map<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < UpopConfig.notifyVo.length; i++) {
			map.put(UpopConfig.notifyVo[i], valueVo[i]);
		}

		if (signature == null)
			return false;
		if (UpopConfig.signType.equalsIgnoreCase(signMethod)) {
			// System.out.println(">>>" + joinMapValue(map, '&') + md5(QuickPayConf.securityKey));
			// System.out.println(">>>" + signature.equals(md5(joinMapValue(map, '&') + md5(QuickPayConf.securityKey))));
			return signature.equals(md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey)));
		} else {
			return verifyWithRSA(signMethod, md5(joinMapValue(map, '&') + md5(UpopConfig.securityKey)), signature);
		}

	}

	private static String joinMapValue(Map<String, String> map, char connector) {
		StringBuffer b = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			b.append(entry.getKey());
			b.append('=');
			if (entry.getValue() != null) {
				b.append(entry.getValue());
			}
			b.append(connector);
		}
		return b.toString();
	}

	/**
	 * get the md5 hash of a string
	 * 
	 * @param str
	 * @return
	 */
	private static String md5(String str) {

		if (str == null) {
			return null;
		}

		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance(UpopConfig.signType);
			messageDigest.reset();
			messageDigest.update(str.getBytes(UpopConfig.charset));
		} catch (NoSuchAlgorithmException e) {

			return str;
		} catch (UnsupportedEncodingException e) {
			return str;
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * Generate an form, auto submit data to the given <code>actionUrl</code>
	 * 
	 * @param actionUrl
	 * @param paramMap
	 * @return
	 */
	private static String generateAutoSubmitForm(String actionUrl, Map<String, String> paramMap) {
		StringBuilder html = new StringBuilder();
		html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
		html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(actionUrl).append("\" method=\"post\">\n");

		for (String key : paramMap.keySet()) {
			html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
		}
		html.append("</form>\n");
		return html.toString();
	}

}
