package com.homlin.module.commom.sms;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import cn.emay.sdk.client.api.Client;

public class EmaySMSClient {

	private static Client client = null;

	private EmaySMSClient() {
	}

	public synchronized static Client getClient(String softwareSerialNo, String key) {
		if (client == null) {
			try {
				client = new Client(softwareSerialNo, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public synchronized static Client getClient() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("smsconfig");
		if (client == null) {
			try {
				client = new Client(bundle.getString("softwareSerialNo"), bundle.getString("key"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public static void main(String str[]) {
		EmaySMSClient.getClient();
	}
}
