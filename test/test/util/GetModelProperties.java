package test.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.homlin.utils.Util;

public class GetModelProperties {

	public static void main(String[] args) {
		print();
	}

	public static void print() {
		while (true) {
			try {
				System.out.print("请输入对象类名：模块.类[.别名](sys.User[.alias])\n=:");
				BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
				String clazz = strin.readLine();
				if (";".equals(clazz)) {
					return;
				}
				String alias = "";
				if (!StringUtils.startsWithIgnoreCase(clazz, "com.homlin")) {
					String[] ns = clazz.split("\\.");
					clazz = String.format("com.homlin.module.%s.model.%s", ns[0], ns[1]);
					if (ns.length > 2) {
						alias = ns[2] + ".";
					}
				}
				PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(Class.forName(clazz));
				List<String> list = new ArrayList<String>();
				for (PropertyDescriptor pd : pds) {
					if ("class".equals(pd.getName())) {
						continue;
					}
					list.add(alias + pd.getName());
				}
				String properties = StringUtils.join(list, ",");
				System.out.println(properties);
				Util.copyToClipboard(properties);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
