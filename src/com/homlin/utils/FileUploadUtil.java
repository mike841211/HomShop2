package com.homlin.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.homlin.app.exception.MessageException;


/**
 * 上传工具
 * 
 * @author Administrator
 * @version 1.0.140304
 */
public class FileUploadUtil {

	// -- 重命名类型 todo 使用枚举
	public static final String NAMEFORMAT_DATETIME = "DATETIME";
	public static final String NAMEFORMAT_ORIGINAL = "ORIGINAL";
	public static final String NAMEFORMAT_UUID = "UUID";
	public static final String NAMEFORMAT_DATETIMERAND = "DATETIMERAND";
	public static final String NAMEFORMAT_TIMESTAMP = "TIMESTAMP";

	// -- 文件类型 todo 使用枚举
	public static final String FILETYPE_IMAGE = "IMAGE";
	public static final String FILETYPE_FILE = "FILE";
	public static final String FILETYPE_FLASH = "FLASH";
	public static final String FILETYPE_MEDIA = "MEDIA";

	private HttpServletRequest request;

	private boolean useContextPath = true;

	// 合计最大限制
	// private Long totalMaxSize;

	// 单文件最大限制
	// private Long singleMaxSize;

	// 上传物理根目录
	private String rootPath;

	// 上传根目录
	private String saveFolder;

	// 保存文件夹
	private String subFolder = "";

	// 文件类型
	private String fileType;

	// 重命名格式
	private String nameFormat;

	// 批量上传计数
	private int uploadCount;

	// 定义允许上传的文件扩展名
	private HashMap<String, String> extMap = new HashMap<String, String>();

	public FileUploadUtil setRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setUseContextPath(boolean useContextPath) {
		this.useContextPath = useContextPath;
	}

	public boolean isUseContextPath() {
		return useContextPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setSaveFolder(String saveFolder) {
		this.saveFolder = saveFolder;
	}

	public String getSaveFolder() {
		return saveFolder;
	}

	public void setSubFolder(String subFolder) {
		if (StringUtils.isBlank(subFolder)) {
			subFolder = "";
		} else {
			subFolder = subFolder.replace("\\", "/");
			if (subFolder.substring(0, 1).equals("/")) {
				subFolder = subFolder.substring(1);
			}
		}
		this.subFolder = subFolder;
	}

	// 其他字符单引号：'file'/yyyy/MM/dd
	public void setDateFormatSubFolder(String dateFormat) {
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		String subFolder = sf.format(Calendar.getInstance().getTimeInMillis());
		setSubFolder(subFolder);
	}

	public String getSubFolder() {
		return subFolder;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat.toLowerCase();
	}

	public String getNameFormat() {
		return nameFormat;
	}

	public void setExtMap(HashMap<String, String> extMap) {
		this.extMap = extMap;
	}

	public HashMap<String, String> getExtMap() {
		return extMap;
	}

	public void setImageExt(String ext) {
		getExtMap().put(FILETYPE_IMAGE, ext.toLowerCase());
	}

	public void setFlashExt(String ext) {
		getExtMap().put(FILETYPE_FLASH, ext.toLowerCase());
	}

	public void setMediaExt(String ext) {
		getExtMap().put(FILETYPE_MEDIA, ext.toLowerCase());
	}

	public void setFileExt(String ext) {
		getExtMap().put(FILETYPE_FILE, ext.toLowerCase());
	}

	private void init() {
		ServletContext servletContext = getRequest().getServletContext();

		// ----
		String UPLOAD_ROOTPATH = servletContext.getInitParameter("UPLOAD_ROOTPATH");
		if (UPLOAD_ROOTPATH == null) {
			UPLOAD_ROOTPATH = servletContext.getRealPath("/");
		}
		setRootPath(UPLOAD_ROOTPATH);

		// 文件保存目录路径
		String UPLOAD_SAVEFOLDER = servletContext.getInitParameter("UPLOAD_SAVEFOLDER");
		if (UPLOAD_SAVEFOLDER == null) {
			UPLOAD_SAVEFOLDER = "upload";
		}
		String UPLOAD_NAMEFORMAT = servletContext.getInitParameter("UPLOAD_NAMEFORMAT");
		if (UPLOAD_NAMEFORMAT == null) {
			UPLOAD_NAMEFORMAT = "datetimernd";
		}
		setSaveFolder(UPLOAD_SAVEFOLDER);
		setDateFormatSubFolder("yyyy/MM/dd");
		setNameFormat(UPLOAD_NAMEFORMAT);
		// setFileType("image");

		uploadCount = 101;

		// 拓展名
		String ext;
		ext = servletContext.getInitParameter("UPLOAD_EXT_IMAGE");
		if (ext == null) {
			ext = "gif,jpg,jpeg,png,bmp";
		}
		setImageExt(ext);
		ext = servletContext.getInitParameter("UPLOAD_EXT_FLASH");
		if (ext == null) {
			ext = "swf,flv";
		}
		setFlashExt(ext);
		ext = servletContext.getInitParameter("UPLOAD_EXT_MEDIA");
		if (ext == null) {
			ext = "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb";
		}
		setMediaExt(ext);
		ext = servletContext.getInitParameter("UPLOAD_EXT_FILE");
		if (ext == null) {
			ext = "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2";
		}
		setFileExt(ext);

	}

	public FileUploadUtil(HttpServletRequest request) {
		this.request = request;
		init();
	}

	public FileUploadUtil() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		this.request = request;
		init();
	}

	public static FileUploadUtil getInstance() {
		return new FileUploadUtil();
	}

	/**
	 * 获取拓展名
	 * 
	 * @param filename
	 * @return
	 */
	public String getExt(String filename) {
		String ext = "";
		if (filename != null && filename.length() > 0) {
			int i = filename.lastIndexOf(".");
			if (i > -1 && i < filename.length() - 1) {
				ext = filename.substring(i + 1);
			}
		}
		return ext;
	}

	/**
	 * 获取源文件名
	 * 
	 * @param filename
	 * @return
	 */
	public String getName(String filename) {
		String name = "";
		if (filename != null && filename.length() > 0) {
			int i = filename.lastIndexOf(".");
			if (i > -1 && i < filename.length() - 1) {
				name = filename.substring(0, i);
			} else {
				name = filename;
			}
		}
		return name;
	}

	/**
	 * 获取新文件名
	 * 
	 * @param filename
	 * @return
	 */
	public String getSaveName(String filename) {
		String ext = getExt(filename);
		String newName = "";
		if (StringUtils.equalsIgnoreCase(getNameFormat(), NAMEFORMAT_DATETIMERAND)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			newName = sf.format(Calendar.getInstance().getTimeInMillis()) + RandomStringUtils.randomNumeric(5) + "." + ext;
			// newName = sf.format(Calendar.getInstance().getTimeInMillis()) + (int) (Math.random() * 89999 + 10000) + "." + ext;
		} else if (StringUtils.equalsIgnoreCase(getNameFormat(), NAMEFORMAT_TIMESTAMP)) {
			newName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + "." + ext;
		} else if (StringUtils.equalsIgnoreCase(getNameFormat(), NAMEFORMAT_ORIGINAL)) {
			String original = getName(filename);
			int i = 1;
			while (new File(getRootPath(), getSaveFolder() + "/" + getSubFolder() + "/" + filename).exists()) {
				filename = original + "(" + i++ + ")" + "." + ext;
			}
			newName = filename;
		} else if (StringUtils.equalsIgnoreCase(getNameFormat(), NAMEFORMAT_UUID)) {
			newName = UUID.randomUUID().toString() + "." + ext;
			// } else if (StringUtils.equalsIgnoreCase(getNameFormat(), NAMEFORMAT_DATETIME)) {
		} else { // 不适合KindEditor
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			newName = sf.format(Calendar.getInstance().getTimeInMillis()) + uploadCount++ + "." + ext;
			// while (new File(getRootPath(), getSaveFolder() + "/" + getSubFolder() + "/" + newName).exists()) {
			// newName = sf.format(Calendar.getInstance().getTimeInMillis()) + uploadCount++ + "." + ext;
			// }
		}
		return newName;
	}

	public List<String> upload(CommonsMultipartFile[] files) throws Exception {
		// 检查文件
		for (CommonsMultipartFile mFile : files) {
			if (!mFile.isEmpty()) {
				boolean alown = false;
				String filename = mFile.getOriginalFilename();
				String fileExt = getExt(filename);
				if (getFileType() != null) { // 指定文件类别
					alown = Arrays.<String> asList(getExtMap().get(getFileType()).split(",")).contains(fileExt);
					if (!alown) {
						throw new MessageException("您上传的文件格式不允许。\n只允许" + getExtMap().get(getFileType()) + "格式。");
					}
				} else {
					for (String key : getExtMap().keySet()) {
						alown = Arrays.<String> asList(getExtMap().get(key).split(",")).contains(fileExt.toLowerCase());
						if (alown) {
							break;
						}
					}
					if (!alown) {
						throw new MessageException("您上传的文件格式不允许。");
					}
				}
			}
		}

		// 应用程序根目录URL
		String rootUrl = getRequest().getContextPath();

		// 上传保存
		List<String> list = new ArrayList<String>();
		for (CommonsMultipartFile mFile : files) {
			if (!mFile.isEmpty()) {
				// 获取本地存储路径
				File folder = new File(getRootPath() + getSaveFolder() + "/" + getSubFolder());
				if (!folder.exists()) {
					folder.mkdirs();
				}

				// 文件路径，绝对路径
				String filepath = "/" + getSaveFolder() + "/" + getSubFolder() + "/" + getSaveName(mFile.getOriginalFilename());
				File savefile = new File(getRootPath() + filepath); // 新建一个文件
				try {
					mFile.getFileItem().write(savefile); // 将上传的文件写入新建的文件中
					if (isUseContextPath()) {
						filepath = rootUrl + filepath;
					}
					list.add(filepath);
					// log.info("=======文件上传成功====");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

}
