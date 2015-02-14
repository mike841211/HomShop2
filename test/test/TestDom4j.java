package test;

//先加入dom4j.jar包 
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.utils.JacksonUtil;

/**
 * @Title: TestDom4j.java
 * @Package
 * @Description: 解析xml字符串
 * @author 无处不在
 * @date 2012-11-20 下午05:14:05
 * @version V1.0
 */
public class TestDom4j {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// 下面是需要解析的xml字符串例子
		String xmlString = "<xml><OpenId><![CDATA[oDF3iY9P32sK_5GgYiRkjsCo45bk]]></OpenId><AppId><![CDATA[wxf8b4f85f3a794e77]]></AppId>" +
				"<TimeStamp>1393400471</TimeStamp><MsgType><![CDATA[request]]></MsgType><FeedBackId>7197417460812502768</FeedBackId>" +
				"<TransId><![CDATA[1900000109201402143240185685]]></TransId><Reason><![CDATA[质量问题]]></Reason>" +
				"<Solution><![CDATA[换货]]></Solution><ExtInfo><![CDATA[备注 12435321321]]></ExtInfo>" +
				"<AppSignature><![CDATA[d60293982cc7c97a5a9d3383af761db763c07c86]]></AppSignature><SignMethod><![CDATA[sha1]]></SignMethod>" +
				"<PicInfo><item><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfodiaUAmxr4hOP34C6R4nGgebMalKuY3H35riaZ5vtzJh25tp7vBUwWxw/0]]></PicUrl></item><item><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfn3y72eHKRSAwVz1PyIcUSjBrDzXAibTiaAdrTGb4eBFbib9ibFaSeic3OIg/0]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item></PicInfo></xml>";

		Document doc = null;

		// 将字符串转为XML
		try {
			doc = DocumentHelper.parseText(xmlString);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		System.out.println("根节点：" + rootElt.getName());
		// System.out.println(rootElt.elementTextTrim("OpenId"));

		// Map<String, Object> paymentData = new HashMap<String, Object>();
		TbWxPayfeedback payfeedback = new TbWxPayfeedback();
		payfeedback.setAppId(rootElt.elementTextTrim("AppId"));
		// payfeedback.setAppSignature(rootElt.elementTextTrim("AppSignature"));
		payfeedback.setExtInfo(rootElt.elementTextTrim("ExtInfo"));
		payfeedback.setFeedBackId(rootElt.elementTextTrim("FeedBackId"));
		// payfeedback.setMsgType(rootElt.elementTextTrim("MsgType"));
		payfeedback.setOpenId(rootElt.elementTextTrim("OpenId"));
		payfeedback.setReason(rootElt.elementTextTrim("Reason"));
		// payfeedback.setSignMethod(rootElt.elementTextTrim("SignMethod"));
		payfeedback.setSolution(rootElt.elementTextTrim("Solution"));
		payfeedback.setTimeStamp(Integer.valueOf(rootElt.elementTextTrim("TimeStamp")));
		payfeedback.setTransId(rootElt.elementTextTrim("TransId"));
		payfeedback.setRawXmlData(xmlString);
		Element PicInfo = rootElt.element("PicInfo");
		if (PicInfo != null) {
			List<Element> items = PicInfo.elements();
			for (int i = 0; i < items.size(); i++) {
				String name = "picUrl" + i;
				String value = items.get(i).elementTextTrim("PicUrl");
				try {
					BeanUtils.setProperty(payfeedback, name, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		System.err.println(JacksonUtil.toJsonString(payfeedback));

		// /*
		// * Test2 test = new Test2(); test.readStringXml(xmlString);
		// */
		// Map map = readStringXmlOut(xmlString);
		// Iterator iters = map.keySet().iterator();
		// while (iters.hasNext()) {
		// String key = iters.next().toString(); // 拿到键
		// String val = map.get(key).toString(); // 拿到值
		// System.out.println(key + "=" + val);
		// }
	}

	@SuppressWarnings({ "rawtypes" })
	public void readStringXml(String xml) {
		Document doc = null;
		try {

			// 读取并解析XML文档
			// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
			//
			// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
			// Document document = reader.read(new File("User.hbm.xml"));
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML

			Element rootElt = doc.getRootElement(); // 获取根节点
			System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

			Iterator iter = rootElt.elementIterator("head"); // 获取根节点下的子节点head

			// 遍历head节点
			while (iter.hasNext()) {

				Element recordEle = (Element) iter.next();
				String title = recordEle.elementTextTrim("title"); // 拿到head节点下的子节点title值
				System.out.println("title:" + title);

				Iterator iters = recordEle.elementIterator("script"); // 获取子节点head下的子节点script

				// 遍历Header节点下的Response节点
				while (iters.hasNext()) {

					Element itemEle = (Element) iters.next();

					String username = itemEle.elementTextTrim("username"); // 拿到head下的子节点script下的字节点username的值
					String password = itemEle.elementTextTrim("password");

					System.out.println("username:" + username);
					System.out.println("password:" + password);
				}
			}
			Iterator iterss = rootElt.elementIterator("body"); // /获取根节点下的子节点body
			// 遍历body节点
			while (iterss.hasNext()) {

				Element recordEless = (Element) iterss.next();
				String result = recordEless.elementTextTrim("result"); // 拿到body节点下的子节点result值
				System.out.println("result:" + result);

				Iterator itersElIterator = recordEless.elementIterator("form"); // 获取子节点body下的子节点form
				// 遍历Header节点下的Response节点
				while (itersElIterator.hasNext()) {

					Element itemEle = (Element) itersElIterator.next();

					String banlce = itemEle.elementTextTrim("banlce"); // 拿到body下的子节点form下的字节点banlce的值
					String subID = itemEle.elementTextTrim("subID");

					System.out.println("banlce:" + banlce);
					System.out.println("subID:" + subID);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * @description 将xml字符串转换成map
	 * @param xml
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map readStringXmlOut(String xml) {
		Map map = new HashMap();
		Document doc = null;
		try {
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml);
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 拿到根节点的名称
			System.out.println("根节点：" + rootElt.getName());

			// 获取根节点下的子节点head
			Iterator iter = rootElt.elementIterator("head");
			// 遍历head节点
			while (iter.hasNext()) {

				Element recordEle = (Element) iter.next();
				// 拿到head节点下的子节点title值
				String title = recordEle.elementTextTrim("title");
				System.out.println("title:" + title);
				map.put("title", title);
				// 获取子节点head下的子节点script
				Iterator iters = recordEle.elementIterator("script");
				// 遍历Header节点下的Response节点
				while (iters.hasNext()) {
					Element itemEle = (Element) iters.next();
					// 拿到head下的子节点script下的字节点username的值
					String username = itemEle.elementTextTrim("username");
					String password = itemEle.elementTextTrim("password");

					System.out.println("username:" + username);
					System.out.println("password:" + password);
					map.put("username", username);
					map.put("password", password);
				}
			}

			// 获取根节点下的子节点body
			Iterator iterss = rootElt.elementIterator("body");
			// 遍历body节点
			while (iterss.hasNext()) {
				Element recordEless = (Element) iterss.next();
				// 拿到body节点下的子节点result值
				String result = recordEless.elementTextTrim("result");
				System.out.println("result:" + result);
				// 获取子节点body下的子节点form
				Iterator itersElIterator = recordEless.elementIterator("form");
				// 遍历Header节点下的Response节点
				while (itersElIterator.hasNext()) {
					Element itemEle = (Element) itersElIterator.next();
					// 拿到body下的子节点form下的字节点banlce的值
					String banlce = itemEle.elementTextTrim("banlce");
					String subID = itemEle.elementTextTrim("subID");

					System.out.println("banlce:" + banlce);
					System.out.println("subID:" + subID);
					map.put("result", result);
					map.put("banlce", banlce);
					map.put("subID", subID);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	public static void main2(String[] args) {
		// 下面是需要解析的xml字符串例子
		String xmlString = "<html>" + "<head>" + "<title>dom4j解析一个例子</title>"
				+ "<script>" + "<username>yangrong</username>"
				+ "<password>123456</password>" + "</script>" + "</head>"
				+ "<body>" + "<result>0</result>" + "<form>"
				+ "<banlce>1000</banlce>" + "<subID>36242519880716</subID>"
				+ "</form>" + "</body>" + "</html>";

		/*
		 * Test2 test = new Test2(); test.readStringXml(xmlString);
		 */
		Map map = readStringXmlOut(xmlString);
		Iterator iters = map.keySet().iterator();
		while (iters.hasNext()) {
			String key = iters.next().toString(); // 拿到键
			String val = map.get(key).toString(); // 拿到值
			System.out.println(key + "=" + val);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main3(String[] args) {
		// 下面是需要解析的xml字符串例子
		String xmlString = "<xml>"
				+ "<OpenId><![CDATA[oBZG-uAxgUR_EJTc3Xkwb5JmPYww]]></OpenId>"
				+ "<AppId><![CDATA[wxf13c857d6ac62f3e]]></AppId>"
				+ "<IsSubscribe>1</IsSubscribe>"
				+ "<TimeStamp>1397212780</TimeStamp>"
				+ "<NonceStr><![CDATA[mwrpLdlhSyObn2nV]]></NonceStr>"
				+ "<AppSignature><![CDATA[c3ccd1520757de07b3af9c4bfab277065420e8ae]]></AppSignature>"
				+ "<SignMethod><![CDATA[sha1]]></SignMethod>"
				+ "<SignMethod2><SignMethod2><![CDATA[sha1]]></SignMethod2></SignMethod2>"
				+ "</xml>";

		Document doc = null;

		// 将字符串转为XML
		try {
			doc = DocumentHelper.parseText(xmlString);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 拿到根节点的名称
		System.out.println("根节点：" + rootElt.getName());
		System.out.println(rootElt.elementTextTrim("OpenId"));

		// Map<String, Object> paymentData = new HashMap<String, Object>();

		List<Element> elements = rootElt.elements();
		for (Element element : elements) {
			System.out.println(element.getName() + "=" + element.getTextTrim());
			// paymentData.put(element.getName(), element.getTextTrim());
		}

		// System.err.println(JacksonUtil.toJsonString(paymentData));

		// /*
		// * Test2 test = new Test2(); test.readStringXml(xmlString);
		// */
		// Map map = readStringXmlOut(xmlString);
		// Iterator iters = map.keySet().iterator();
		// while (iters.hasNext()) {
		// String key = iters.next().toString(); // 拿到键
		// String val = map.get(key).toString(); // 拿到值
		// System.out.println(key + "=" + val);
		// }
	}

}