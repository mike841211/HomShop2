package com.homlin.module.commom.kindeditor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.homlin.app.controller.BaseController;
import com.homlin.app.exception.MessageException;
import com.homlin.utils.FileUploadUtil;
import com.homlin.utils.JacksonUtil;

@Controller
@RequestMapping("/kindeditor")
public class KindEditorController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "/upload_json", method = RequestMethod.POST)
	public String uploadJson(@RequestParam("imgFile") CommonsMultipartFile[] files,
			@RequestParam(value = "dir", defaultValue = "") String dir, HttpServletRequest request) throws Exception {

		Map<String, Object> obj = new HashMap<String, Object>();

		FileUploadUtil fileUploadUtil = new FileUploadUtil();
		// fileUploadUtil.setSubFolder(dir);

		List<String> list = new ArrayList<String>();
		try {
			list = fileUploadUtil.upload(files);
			obj.put("error", 0);
			obj.put("url", list.get(0));
		} catch (MessageException e) {
			obj.put("error", 1);
			obj.put("message", e.getMessage());
		} catch (Exception e) {
			obj.put("error", 1);
			obj.put("message", e.getMessage());
			trace(e.getMessage());
			// e.printStackTrace();
		}
		return JacksonUtil.toJsonString(obj);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/file_manager_json", method = RequestMethod.GET)
	public String fileManagerJson(
			@RequestParam(value = "dir", defaultValue = "") String dir, @RequestParam(value = "path", defaultValue = "") String path,
			@RequestParam(value = "order", defaultValue = "") String order, HttpServletRequest request) throws Exception {

		@SuppressWarnings("rawtypes")
		class NameComparator implements Comparator {
			public int compare(Object a, Object b) {
				Hashtable hashA = (Hashtable) a;
				Hashtable hashB = (Hashtable) b;
				if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
					return -1;
				} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
					return 1;
				} else {
					return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
				}
			}
		}
		@SuppressWarnings("rawtypes")
		class SizeComparator implements Comparator {
			public int compare(Object a, Object b) {
				Hashtable hashA = (Hashtable) a;
				Hashtable hashB = (Hashtable) b;
				if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
					return -1;
				} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
					return 1;
				} else {
					if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
						return 1;
					} else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}
		@SuppressWarnings("rawtypes")
		class TypeComparator implements Comparator {
			public int compare(Object a, Object b) {
				Hashtable hashA = (Hashtable) a;
				Hashtable hashB = (Hashtable) b;
				if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
					return -1;
				} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
					return 1;
				} else {
					return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
				}
			}
		}


		// ServletContext sc = getRequest().getSession().getServletContext();
		//
		// // 文件保存目录路径
		// String UPLOAD_SAVEFOLDER = sc.getInitParameter("UPLOAD_SAVEFOLDER");
		// if (UPLOAD_SAVEFOLDER == null) {
		// UPLOAD_SAVEFOLDER = "upload";
		// }

		// 根目录路径，可以指定绝对路径，比如 /var/www/upload/
		String rootPath = getRequest().getSession().getServletContext().getRealPath("/") + "upload/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/upload/
		String rootUrl = getRequest().getContextPath() + "/upload/";

		// if (!"".equals(dir)) {
		// if (!Arrays.<String> asList(new String[] { "image", "flash", "media", "file" }).contains(dir)) {
		// throw new MessageException("Invalid Directory name.");
		// }
		// rootPath += dir + "/";
		// rootUrl += dir + "/";
		// File saveDirFile = new File(rootPath);
		// if (!saveDirFile.exists()) {
		// // saveDirFile.mkdirs();
		// throw new MessageException("Invalid Directory name.");
		// }
		// }

		// 根据path参数，设置各路径和URL
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			throw new MessageException("Access is not allowed.");
		}
		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			throw new MessageException("Parameter \"path\" is not valid.");
		}
		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			throw new MessageException("Directory does not exist.");
		}

		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
		// 遍历目录取的文件信息
		List<Hashtable<String, Object>> fileList = new ArrayList<Hashtable<String, Object>>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		// 排序形式，name or size or type
		if ("size".equalsIgnoreCase(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equalsIgnoreCase(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);

		return JacksonUtil.toJsonString(result);
	}

}
