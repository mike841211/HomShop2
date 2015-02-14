package test.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class AutoCreateJavaFile {

	private static final String CLASSPATH = AutoCreateJavaFile.class.getClassLoader().getResource("").toString().substring(6);

	public static void main(String[] args) throws IOException {
		String message = "请输入对象类名：模块.类名(sys.User)\n=:";
		System.out.print(message);
		String modulepath = CLASSPATH.substring(0, CLASSPATH.indexOf("/WebRoot/")) + "/src/com/homlin/module/";
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = reader.readLine();
			if (StringUtils.isBlank(input)) {
				System.out.print(message);
				continue;
			} else if (";".equals(input)) {
				return;
			}
			String[] inputs = input.split("\\s+");
			if (inputs.length != 2) {
				System.out.print(message);
				continue;
			}
			String module = inputs[0];
			String[] arrClazz = inputs[1].split("\\s*\\,\\s*");

			for (String clazz : arrClazz) {
				createDao(modulepath, module, clazz);
				createDaoImpl(modulepath, module, clazz);
				createService(modulepath, module, clazz);
				createServiceImpl(modulepath, module, clazz);
				System.out.println("\tok");
			}
			System.out.println("finished");
		}
	}

	private static void createDao(String modulepath, String module, String clazz) throws IOException {
		createJavaFile(module, clazz, CLASSPATH + "test/util/TemplateDao.java.txt", modulepath + module + "/dao/" + clazz + "Dao.java");
		System.out.println("create dao interface finished");
	}

	private static void createDaoImpl(String modulepath, String module, String clazz) throws IOException {
		createJavaFile(module, clazz, CLASSPATH + "test/util/TemplateDaoImpl.java.txt", modulepath + module + "/dao/impl/" + clazz + "DaoImpl.java");
		System.out.println("create dao implement finished");
	}

	private static void createService(String modulepath, String module, String clazz) throws IOException {
		createJavaFile(module, clazz, CLASSPATH + "test/util/TemplateService.java.txt", modulepath + module + "/service/" + clazz + "Service.java");
		System.out.println("create service interface finished");
	}

	private static void createServiceImpl(String modulepath, String module, String clazz) throws IOException {
		createJavaFile(module, clazz, CLASSPATH + "test/util/TemplateServiceImpl.java.txt", modulepath + module + "/service/impl/" + clazz + "ServiceImpl.java");
		System.out.println("create service implement finished");
	}

	private static void createJavaFile(String module, String clazz, String templatefile, String javafile) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(templatefile));
		StringBuilder sb = new StringBuilder();
		int len = -1;
		char[] cbuf = new char[1024];
		while ((len = reader.read(cbuf)) != -1) {
			sb.append(cbuf, 0, len);
		}
		reader.close();

		String template = sb.toString();
		template = template.replace("#module#", module);
		template = template.replace("#clazz#", clazz);

		BufferedWriter writer = new BufferedWriter(new FileWriter(javafile));
		writer.write(template, 0, template.length());
		writer.flush();
		writer.close();

	}

}
