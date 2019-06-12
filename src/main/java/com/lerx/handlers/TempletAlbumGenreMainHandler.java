package com.lerx.handlers;

import java.beans.IntrospectionException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.ITempletAlbumGenreMainDao;
import com.lerx.entities.TempletComment;
import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.entities.TempletSubElement;
import com.lerx.hql.entities.Rs;
import com.lerx.sys.obj.LayerUploadReturnData;
import com.lerx.sys.obj.LayerUploadedReturn;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.FileZip;
import com.lerx.sys.util.HttpPostUtil;
import com.lerx.sys.util.MavUtil;
import com.lerx.sys.util.ObjUtil;
import com.lerx.sys.util.SecurityUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.v5.util.TempletAlbumGenreUtil;

@RequestMapping("/action_albgenre_templet")
@Controller
public class TempletAlbumGenreMainHandler {

	private static final String ADMINFORBID = "_admin.forbid_";

	@Autowired
	private ITempletAlbumGenreMainDao templetAlbumGenreMainDaoImpl;

	@Autowired
	private ResourceBundleMessageSource messageSource;

	private TempletAlbumGenreMain chkuuid(TempletAlbumGenreMain templet) {
		if (templet!=null && (templet.getUuid()==null || templet.getUuid().trim().equals(""))) {
			templet.setUuid(StringUtil.uuidStr());
			templetAlbumGenreMainDaoImpl.modify(templet);
		}
		return templet;
	}
	
	
	/*
	 * 本私有方法用于检测数据库的对象结构是否完整，如果不完整则进行修复
	 */
	private TempletAlbumGenreMain findByID(Long id) {
		boolean chg = false;
		TempletAlbumGenreMain touchShowTempletMain = templetAlbumGenreMainDaoImpl.findByID(id);
		touchShowTempletMain=chkuuid(touchShowTempletMain);
		if (touchShowTempletMain.getElArt() == null) {
			touchShowTempletMain.setElArt(new TempletSubElement());
			chg = true;
		}
		if (touchShowTempletMain.getElExtend() == null) {
			touchShowTempletMain.setElExtend(new TempletSubElement());
			chg = true;
		}
		if (touchShowTempletMain.getElIndex() == null) {
			touchShowTempletMain.setElIndex(new TempletSubElement());
			chg = true;
		}
		if (touchShowTempletMain.getElNav() == null) {
			touchShowTempletMain.setElNav(new TempletSubElement());
			chg = true;
		}
		if (touchShowTempletMain.getElPublic() == null) {
			touchShowTempletMain.setElPublic(new TempletSubElement());
			chg = true;
		}

		if (touchShowTempletMain.getElComment() == null) {
			touchShowTempletMain.setElComment(new TempletComment());
			chg = true;
		}
		if (chg) {
			templetAlbumGenreMainDaoImpl.modify(touchShowTempletMain);
		}
		return touchShowTempletMain;

	}

	@ModelAttribute
	public void getTempletAlbumGenreMain(@RequestParam(value = "id", required = false) Long id, Map<String, Object> map) {
		if (id != null && id > 0) {
			map.put("templetAlbumGenreMain", findByID(id));
			// System.out.println("从数据库获取对象");
		}
	}

	/*
	 * 列表
	 */
	@RequestMapping("/list")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String list(Map<String, Object> map, HttpSession session,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		if (page == null) {
			page = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		String securityStr = SecurityUtil.readWords(messageSource);
		map.put("security", securityStr);
		Rs rs = templetAlbumGenreMainDaoImpl.query(page, pageSize);

		map.put("rs", rs);
		String marketSrv = messageSource.getMessage("template.market.srv.url", null, "http://www.lerx.com/", null);
		marketSrv = marketSrv.trim();
		if (marketSrv.endsWith("/")) {
			marketSrv = marketSrv.substring(0, marketSrv.length() - 1);
		}
		map.put("marketUrl", marketSrv);

		map.put("pageUrl", "/action_albgenre_templet/list");

		return "jsp/templet/albgenre/list";
	}

	/*
	 * 列表
	 */
	@RequestMapping("/marketlist")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String marketlist(Map<String, Object> map, HttpSession session,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (page == null) {
			page = 0;
		}
		if (limit == null) {
			limit = 10;
		}

		String marketSrv = messageSource.getMessage("template.market.srv.url", null, "http://www.lerx.com/", null);
		marketSrv = marketSrv.trim();
		if (marketSrv.endsWith("/")) {
			marketSrv = marketSrv.substring(0, marketSrv.length() - 1);
		}
		map.put("marketUrl", marketSrv);
		map.put("page", page);
		map.put("pageSize", limit);
		map.put("marketUrl", marketSrv);

		return "jsp/templet/albgenre/market";
	}

	/*
	 * 增加
	 */
	@RequestMapping("/add")
	@ResponseBody
	@Token(ajax = true, log = true, mark = "template-albumgenre--<add>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int add(String name, HttpServletRequest request, Map<String, Object> map) {

		if (name == null || name.trim().equals("")) {
			return -12;
		}
		List<TempletAlbumGenreMain> findedResult = templetAlbumGenreMainDaoImpl.findByTitle(name);
		if (findedResult.size() > 0) {
			return -10;
		}
		TempletAlbumGenreMain templetAlbumGenreMain = new TempletAlbumGenreMain();
		templetAlbumGenreMain.setName(name);
		templetAlbumGenreMain.setUuid(StringUtil.uuidStr());
		templetAlbumGenreMain.setState(true);
		templetAlbumGenreMain.setDef(false);
		templetAlbumGenreMain.setFreeOnClouds(false);
		templetAlbumGenreMain.setDesignTime(System.currentTimeMillis());
		templetAlbumGenreMain.setElPublic(new TempletSubElement());
		templetAlbumGenreMain.setElIndex(new TempletSubElement());
		templetAlbumGenreMain.setElNav(new TempletSubElement());
		templetAlbumGenreMain.setElArt(new TempletSubElement());
		templetAlbumGenreMain.setElExtend(new TempletSubElement());
		templetAlbumGenreMain.setElComment(new TempletComment());
		long tid = templetAlbumGenreMainDaoImpl.add(templetAlbumGenreMain);
		if (tid > 0L) {
			return 0;
		} else {
			return -1;
		}

	}

	/*
	 * 查询并进入修改页面
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String edit(@PathVariable("id") Long id, long mid, Map<String, Object> map) {
		TempletAlbumGenreMain touchShowTempletMain = findByID(id);
		map.put("touchShowTempletMain", touchShowTempletMain);
		map.put("mid", mid);
		return "tctemplet/add";
	}

	/*
	 * 修改状态
	 */
	@ResponseBody
	@RequestMapping(value = "/chgstatus/{id}", method = RequestMethod.POST)
	@Token(ajax = true, log = true, mark = "template-albumgenre--<chgstatus>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int status(@PathVariable(value = "id", required = true) Long id, HttpSession session) {
		TempletAlbumGenreMain templet = templetAlbumGenreMainDaoImpl.findByID(id);
		templet=chkuuid(templet);
		if (templet.isDef()) {
			return -4;
		} else {
			if (templet.isState()) {
				templet.setState(false);
			} else {
				templet.setState(true);
			}
			templetAlbumGenreMainDaoImpl.modify(templet);
			return 0;
		}

	}

	/*
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
	@Token(ajax = true, log = true, mark = "template-albumgenre--<del>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int del(@PathVariable(value = "id", required = true) Long id, HttpSession session) {
		TempletAlbumGenreMain templet = templetAlbumGenreMainDaoImpl.findByID(id);
		templet=chkuuid(templet);
		if (templet.isDef()) {
			return -4;
		} else {
			String resFolder = templet.getResFolder();
			if (resFolder!=null && ! resFolder.trim().equals("")) {
				String path = FileUtil.appPath();

				String TempletAlbumGenreDir = messageSource.getMessage("templet.portal.dir", null, "templates/portal", null);
				String resPath = path + File.separator + TempletAlbumGenreDir + File.separator + resFolder;
				
				File res=new File(resPath);
				if (res.exists()) {
					try {
						FileUtils.deleteDirectory(res);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			templetAlbumGenreMainDaoImpl.delByID(id);
			return 0;
		}

	}

	/*
	 * 设默认
	 */
	@ResponseBody
	@RequestMapping(value = "/def/{id}")
	@Token(ajax = true, log = true, mark = "template-albumgenre--<def>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int modifyDef(@PathVariable(value = "id", required = true) Integer id, HttpSession session) {
		templetAlbumGenreMainDaoImpl.setDef(id);
		if (templetAlbumGenreMainDaoImpl.findDef().getId() == id) {
			return 0;
		} else {
			return -1;
		}

	}

	/*
	 * 复制
	 */
	@ResponseBody
	@RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
	@Token(ajax = false, log = true, mark = "template-albumgenre--<copy>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int copy(@PathVariable(value = "id", required = true) Long id, String newTitle, HttpSession session) {
		long r;
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		TempletAlbumGenreMain templateNew = TempletAlbumGenreUtil.copy(templetAlbumGenreMain, newTitle);
		templateNew.setUuid(StringUtil.uuidStr());
		templateNew.setFreeOnClouds(false);
		templateNew.setDef(false);
		r = templetAlbumGenreMainDaoImpl.add(templateNew);

		if (r > 0L) {
			return 0;
		} else {
			return -1;
		}

	}

	/*
	 * 下载
	 */
	@ResponseBody
	@RequestMapping("/downRemote")
	@Token(ajax = true, log = true, mark = "template-albumgenre--<downRemote>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int downRemote(String url, String folder) {
		// System.out.println("url:"+url);
		String path = FileUtil.appPath();
		path += File.separator + "tmp";
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}

		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File f = new File(filePath + File.separator + "aaa.zip");
		try {
//			System.out.println("url:"+url);
			urlfile = new URL(url);
			httpUrl = (HttpURLConnection) urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}

			bos.flush();
			bos.close();
			bis.close();
			httpUrl.disconnect();

			// File f = new File("D:\\tmp\\biao.zip");
			List<String> urlList = new ArrayList<>();

			path = FileUtil.appPath();

			String TempletAlbumGenreDir = messageSource.getMessage("templet.portal.dir", null, "templates/portal", null);
			if (folder == null) {
				folder = "default";
			}
			// String resPath = path + File.separator + TempletAlbumGenreDir + File.separator
			// + folder+ File.separator;
			String resPath = path + File.separator + TempletAlbumGenreDir + File.separator;

			FileZip.unZip(f, resPath, urlList);

			for (String s : urlList) {
				if (s.endsWith(".portal")) {
					String templateFile = s;
					TempletAlbumGenreMain temp, tfind;
					temp = (TempletAlbumGenreMain) ObjUtil.objectXmlDecoder(templateFile).get(0);
					// temp.setName(temp.getUuid());
					tfind = templetAlbumGenreMainDaoImpl.findByUuid(temp.getUuid());
					if (tfind != null) {
						TempletAlbumGenreUtil.copy(temp, tfind);
						tfind.setModifyTime(System.currentTimeMillis());
						templetAlbumGenreMainDaoImpl.modify(tfind);

					} else {
						TempletAlbumGenreMain templateNew = TempletAlbumGenreUtil.copy(temp, temp.getName());
						templateNew.setDesignTime(System.currentTimeMillis());
						templateNew.setModifyTime(System.currentTimeMillis());
						templateNew.setUuid(temp.getUuid());
						templateNew.setFreeOnClouds(false);
						templetAlbumGenreMainDaoImpl.add(templateNew);
					}

					FileUtil.delete(s);

				}

				if (s.endsWith("md5") || s.endsWith("security")) {
					FileUtil.delete(s);
				}

			}
			// 下面导入

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/*
	 * 下载
	 */
	@ResponseBody
	@RequestMapping(value = "/download/{id}")
	@Token(ajax = true, log = true, mark = "template-albumgenre--<down>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int download(@PathVariable(value = "id", required = true) Long id, HttpServletResponse response)
			throws FileNotFoundException, IOException, Exception {
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		templetAlbumGenreMain.setUuid(StringUtil.uuidStr());
		/*
		 * Locale locale = Locale.getDefault();
		 * System.out.println(locale.getLanguage());
		 * System.out.println(locale.getCountry());
		 */
		String path = FileUtil.appPath();
		path += File.separator + "tmp";
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}
		java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateStr = fmt.format(System.currentTimeMillis());
		String fileName = "portalTemplate_" + dateStr + ".portal";
		ObjUtil.objectXmlEncoder(templetAlbumGenreMain, path + File.separator + fileName);
		FileUtil.download(response, path, fileName);
		/*
		 * System.out.println(path+" "+fileName); Thread.sleep(20000);
		 */

		FileUtil.delete(path, fileName);
		return 0;
	}

	/*
	 * 模板完整性检查
	 */
	@ResponseBody
	@RequestMapping(value = "/integrityCheck/{id}", method = RequestMethod.POST)
	@Token(ajax = true, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String integrityCheck(@PathVariable(value = "id", required = true) Long id) {
		String msg = "";
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		// 检查模板中的标签有没有值
		if (templetAlbumGenreMain.getResFolder() == null || templetAlbumGenreMain.getResFolder().trim().equals("")) {
			msg += messageSource.getMessage("warn.template.res.folder.notdefinition", null, "", null);
		} else {
			String path = FileUtil.appPath();

			String TempletAlbumGenreDir = messageSource.getMessage("templet.portal.dir", null, "templates/portal", null);

			String resPath = path + File.separator + TempletAlbumGenreDir + File.separator
					+ templetAlbumGenreMain.getResFolder();

			File filePath = new File(resPath);
			if (!filePath.exists()) {
				msg += messageSource.getMessage("warn.template.res.folder.notfound", null, "", null) + "(" + filePath
						+ ")!";
			}
			String demoJpg = resPath + File.separator + "images" + File.separator + "demo.jpg";
			filePath = new File(demoJpg);
			if (!filePath.exists()) {
				msg += "<br />" + messageSource.getMessage("warn.template.res.demo.notfound", null, "", null) + "("
						+ filePath + ")!";
			}
		}

		return msg;
	}

	// 云端模板
	@ResponseBody
	@RequestMapping(value = "/remote/{id}")
	@Token(ajax = true, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView remote(@PathVariable(value = "id", required = true) Long id, Map<String, Object> map) {
		map.put("remotID", id);
		String marketSrv = messageSource.getMessage("template.market.srv.url", null, "http://www.lerx.com/", null);
		marketSrv = marketSrv.trim();
		if (marketSrv.endsWith("/")) {
			marketSrv = marketSrv.substring(0, marketSrv.length() - 1);
		}
		map.put("marketUrl", marketSrv);
		return MavUtil.mav("jsp/templet/albgenre/remote", "");
	}

	@ResponseBody
	@RequestMapping(value = "/report/{id}")
	@Token(ajax = true, log = true, mark = "template--<report>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int report(@PathVariable(value = "id", required = true) Long id, HttpServletResponse response)
			throws FileNotFoundException, IOException, Exception {
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		templetAlbumGenreMain.setState(true);
		String uuid = templetAlbumGenreMain.getUuid();
		String securityStr = SecurityUtil.readWords(messageSource);
		// System.out.println("securityStr:"+securityStr);
		templetAlbumGenreMain.setOwnerToken(StringUtil.md5(uuid + securityStr));
		String path = FileUtil.appPath();
		String TempletAlbumGenreDir = messageSource.getMessage("templet.portal.dir", null, "templates/portal", null);
		String resPath = path + File.separator + TempletAlbumGenreDir + File.separator + templetAlbumGenreMain.getResFolder();
		String reportPath = path + File.separator + "report";
		String tmpPath = path + File.separator + "tmp";
		resPath = StringUtil.strReplace(resPath, "\\", File.separator);

		File filePath = new File(resPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}
		filePath = new File(reportPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}

		filePath = new File(tmpPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}

		java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
		String dateStr = fmt.format(System.currentTimeMillis());
		String fileName = "portalTemplate_" + dateStr + ".portal";
		String templetFile = tmpPath + File.separator + fileName;
		ObjUtil.objectXmlEncoder(templetAlbumGenreMain, templetFile);
		FileInputStream fis = new FileInputStream(templetFile);
		String md5 = DigestUtils.md5DigestAsHex(IOUtils.toByteArray(fis));
		fis.close();
		fis = null;

		String md5File = tmpPath + File.separator + "md5";
		String securityFile = tmpPath + File.separator + "security";
		FileUtil.writeStringToFile(md5File, md5, false, messageSource.getMessage("charset", null, "UTF-8", null));
		FileUtil.writeStringToFile(securityFile, securityStr, false,
				messageSource.getMessage("charset", null, "UTF-8", null));
		FileZip zc = new FileZip(reportPath + File.separator + uuid + ".zip");
		templetFile = FileUtil.repairFilePath(templetFile);
		md5File = FileUtil.repairFilePath(md5File);
		securityFile = FileUtil.repairFilePath(securityFile);
		reportPath = FileUtil.repairFilePath(reportPath);
		resPath = FileUtil.repairFilePath(resPath);
		zc.compress(templetFile, resPath, md5File, securityFile);
		File f = new File(reportPath + File.separator + uuid + ".zip");

		String marketSrv = messageSource.getMessage("template.market.srv.url", null, "http://www.lerx.com/", null);
		marketSrv = marketSrv.trim();
		if (marketSrv.endsWith("/")) {
			marketSrv = marketSrv.substring(0, marketSrv.length() - 1);
		}
		// 此处修改为自己上传文件的地址
		String url = marketSrv + "/action_templet_market/receive/" + templetAlbumGenreMain.getUuid() + "/" + md5;
		String rstr = HttpPostUtil.uploadFile(f, url);
		if (StringUtil.isNumber(rstr)) {
			return Integer.valueOf(rstr);
		} else {
			return -1;
		}

	}

	/*
	 * 导入
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	@Token(ajax = true, log = true, mark = "template-albumgenre--<import>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public LayerUploadedReturn importTemplet(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws Exception {
		long r;

		String path = FileUtil.appPath();
		path += File.separator + "tmp";

		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
			FileUtil.erable(filePath);
		}

		String fileName = path + File.separator + "albgenreTemplate_import_tmp.xml";
		fileName = FileUtil.repairFilePath(fileName);
		// System.out.println("file:"+file);
		// System.out.println("fileName:"+fileName);
		FileUtil.upload(file, "tmp/portalTemplate_import_tmp.xml");
		File f = new File(fileName);
		TempletAlbumGenreMain temp = (TempletAlbumGenreMain) ObjUtil.objectXmlDecoder(f.toString()).get(0);
		java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateStr = fmt.format(System.currentTimeMillis());
		String title = messageSource.getMessage("template.import.name", null, "newTemplate", null);

		TempletAlbumGenreMain templateNew = TempletAlbumGenreUtil.copy(temp, title + "_" + dateStr);
		templateNew.setUuid(StringUtil.uuidStr());
		templateNew.setFreeOnClouds(false);
		templateNew.setDef(false);
		r = templetAlbumGenreMainDaoImpl.add(templateNew);

		LayerUploadReturnData rd = new LayerUploadReturnData();
		LayerUploadedReturn lur = new LayerUploadedReturn();
		rd.setSrc("");
		lur.setData(rd);
		if (r > 0L) {
			lur.setCode(0);
			FileUtil.delete(fileName);
			lur.setMsg(messageSource.getMessage("success.upload", null, "Upload success!", null));
		} else {
			lur.setCode(-1);
			lur.setMsg(messageSource.getMessage("fail.upload", null, "Upload failure!", null));
		}

		return lur;

	}

	/*
	 * 获取类的属性，用于修改模板
	 */

	@RequestMapping(value = "/fields/{id}/{area}")
	@Token(admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String fields(@PathVariable(value = "id", required = true) Long id,
			@PathVariable(value = "area", required = true) Integer area, Map<String, Object> map) {
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		List<String> fieldsList = new ArrayList<String>();
		List<String> elsList = new ArrayList<String>();
		String type;
		Field[] field = templetAlbumGenreMain.getClass().getDeclaredFields();
		for (int j = 0; j < field.length; j++) {
			type = field[j].getGenericType().toString();
			if (type.endsWith("TempletSubElement")) {
				field[j].setAccessible(true);
				String name = field[j].getName(); // 获取属性的名字
				if (name.endsWith("Index")) {
					name = " (" + messageSource.getMessage("el.area.index", null, null) + ")" + name;
				} else if (name.endsWith("Nav")) {
					name = " (" + messageSource.getMessage("el.area.nav", null, null) + ")" + name;
				} else if (name.endsWith("Art")) {
					name = " (" + messageSource.getMessage("el.area.art", null, null) + ")" + name;
				} else if (name.endsWith("Corpus")) {
					name = " (" + messageSource.getMessage("el.area.corpus", null, null) + ")" + name;
				} else if (name.endsWith("Public")) {
					name = " (" + messageSource.getMessage("el.area.public", null, null) + ")" + name;
				} else if (name.endsWith("Edit")) {
					name = " (" + messageSource.getMessage("el.area.edit", null, null) + ")" + name;
				} else if (name.endsWith("Extend")) {
					name = " (" + messageSource.getMessage("el.area.extend", null, null) + ")" + name;
				}
				elsList.add(name);
			} else if (type.endsWith("TempletComment")) {
				field[j].setAccessible(true);
				String name = field[j].getName(); // 获取属性的名字
				name = " (" + messageSource.getMessage("el.area.comment", null, null) + ")" + name;
				elsList.add(name);
			}
		}
		if (area > 0) {
			switch (area) {
			case 8:
				TempletComment elc = new TempletComment();
				field = elc.getClass().getDeclaredFields();
				break;
			default:
				TempletSubElement el = new TempletSubElement();
				field = el.getClass().getDeclaredFields();
			}

		}

		for (int j = 0; j < field.length; j++) {
			type = field[j].getGenericType().toString();
			if (type.endsWith("java.lang.String")) {
				String name = field[j].getName(); // 获取属性的名字
				fieldsList.add(name);
			}

			/*
			 * String name2= field[j].getName();
			 * 
			 * System.out.println("attribute name:"+name2); name2 =
			 * name2.substring(0,1).toUpperCase()+name2.substring(1);
			 * //将属性的首字符大写，方便构造get，set方法 String type2 =
			 * field[j].getGenericType().toString(); //获取属性的类型
			 * System.out.println("type:"+type2);
			 */
		}

		map.put("templetID", templetAlbumGenreMain.getId());
		map.put("els", elsList);
		map.put("fields", fieldsList);
		map.put("templetTitle", templetAlbumGenreMain.getName());
		map.put("area", area);
		return "jsp/templet/albgenre/details";
	}

	/*
	 * 获取类的属性，用于修改模板
	 */

	@RequestMapping(value = "/viewcode/{id}/{area}/{var}")
	@Token(admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String code(@PathVariable(value = "id", required = true) Long id,
			@PathVariable(value = "area", required = true) Integer area,
			@PathVariable(value = "var", required = true) String var, Map<String, Object> map, HttpSession session)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException {
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);
		String value = "";
		String areaTitle = "";

		switch (area) {
		case 0: // 全局
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain, var);
			areaTitle = messageSource.getMessage("el.area.main", null, null);
			break;
		case 1: // public模块
			TempletSubElement el = templetAlbumGenreMain.getElPublic();
			areaTitle = messageSource.getMessage("el.area.public", null, null);
			value = (String) ObjUtil.getProperty(el, var);
			break;
		case 2: // index
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain.getElIndex(), var);
			areaTitle = messageSource.getMessage("el.area.index", null, null);
			break;
		case 3: // nav
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain.getElNav(), var);
			areaTitle = messageSource.getMessage("el.area.nav", null, null);
			break;
		case 4: // art
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain.getElArt(), var);
			areaTitle = messageSource.getMessage("el.area.art", null, null);
			break;
		case 5: // extend
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain.getElExtend(), var);
			areaTitle = messageSource.getMessage("el.area.extend", null, null);
			break;
		case 6: // extend
			value = (String) ObjUtil.getProperty(templetAlbumGenreMain.getElComment(), var);
			areaTitle = messageSource.getMessage("el.area.comment", null, null);
			break;
		}
		/*
		 * if (area==0){
		 * 
		 * value=(String) ObjUtil.getProperty(touchShowTempletMain, var); Method m =
		 * touchShowTempletMain.getClass().getMethod("get"+StringUtil.captureName(var,1)
		 * ); value = (String) m.invoke(touchShowTempletMain); if(value != null){
		 * System.out.println("attribute value:"+value); } }
		 */

		map.put("code", value);
		map.put("areaTitle", areaTitle);
		map.put("area", area);

		map.put("templetID", templetAlbumGenreMain.getId());
		map.put("var", var);
		return "jsp/templet/albgenre/viewcode";
	}

	/*
	 * 修改模板
	 */

	@RequestMapping(value = "/modifyCode/{id}/{area}/{var}")
	@Token(ajax = false, log = true, mark = "template-albumgenre--<modify>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String modifyCode(HttpServletRequest request, @PathVariable(value = "id", required = true) Long id,
			@PathVariable(value = "area", required = true) Integer area,
			@PathVariable(value = "var", required = true) String var, String code, Map<String, Object> map,
			HttpSession session)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException {
		TempletAlbumGenreMain templetAlbumGenreMain = findByID(id);

		switch (area) {
		case 0: // 全局
			if (!var.trim().equalsIgnoreCase("uuid") && !var.trim().equalsIgnoreCase("ownerToken")) {
				ObjUtil.setProperty(templetAlbumGenreMain, var, code);
			}

			// templetAlbumGenreMainDaoImpl.modify(templetAlbumGenreMain);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain, var);
			break;
		case 1: // public模块
			ObjUtil.setProperty(templetAlbumGenreMain.getElPublic(), var, code);

			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElPublic(), var);
			break;
		case 2: // index
			ObjUtil.setProperty(templetAlbumGenreMain.getElIndex(), var, code);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElIndex(), var);
			break;
		case 3: // nav
			ObjUtil.setProperty(templetAlbumGenreMain.getElNav(), var, code);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElNav(), var);
			break;
		case 4: // art
			ObjUtil.setProperty(templetAlbumGenreMain.getElArt(), var, code);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElArt(), var);
			break;
		case 5: // search
			ObjUtil.setProperty(templetAlbumGenreMain.getElExtend(), var, code);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElSearch(), var);
			break;
		case 6: // search
			ObjUtil.setProperty(templetAlbumGenreMain.getElComment(), var, code);
			// value=(String) ObjUtil.getProperty(touchShowTempletMain.getElSearch(), var);
			break;
		}

		if (area == 0) {

			if (!var.trim().equalsIgnoreCase("uuid") && !var.trim().equalsIgnoreCase("ownerToken")) {
				templetAlbumGenreMainDaoImpl.modify(templetAlbumGenreMain);
			}
		} else {
			templetAlbumGenreMainDaoImpl.modify(templetAlbumGenreMain);
		}

		map.put("area", area);
		map.put("templetID", templetAlbumGenreMain.getId());
		map.put("var", var);

		String url = request.getContextPath() + "/action_albgenre_templet/fields/" + id + "/" + area;

		map.put("his", url);
		return "jsp/templet/success";
	}

}
