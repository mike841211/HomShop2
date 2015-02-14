package test.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import com.homlin.module.AppConstants;

/**
 * hibernate.cfg.xml 配置，用于Open HQL Editor调试HQL
 * 
 * @author Administrator
 * 
 */
public class HibernateConfigurationMapping {

	public static void main(String[] args) throws Exception {
		System.out.println("=============== START ===============");
		// System.out.println(System.getProperty("user.dir"));
		String path;
		// path = "E:\\Java\\projects\\HomApp\\HomApp\\src\\com\\homlin\\module";
		System.out.println(AppConstants.class.getResource("../../module").toString());
		path = AppConstants.class.getResource("../../module").toString().substring(6);

		StringBuffer data = new StringBuffer();
		String temp;
		File root = new File(path);
		for (File module : root.listFiles()) {
			File model = new File(path + "/" + module.getName() + "/model");
			File[] files = model.listFiles();
			if (files == null) {
				continue;
			}
			temp = "\n\n\t\t<!-- " + module.getName() + " -->";
			System.out.print(temp);
			data.append(temp);
			for (File file : files) {
				if (file.isFile()) {
					// <mapping class="com.homlin.module.sys.model.User" />
					temp = "\n\t\t<mapping class=\"com.homlin.module." + module.getName() + ".model." + file.getName().split("\\.")[0] + "\" />";
					System.out.print(temp);
					data.append(temp);
				}
			}
		}
		StringSelection selection = new StringSelection(data.toString() + "\n");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("\n\n=============== 已复制到剪贴板 ===============");

		System.out.println("=============== OK ===============");
	}

}
