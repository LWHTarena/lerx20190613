package com.lerx.handlers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.IAlbumGenreDao;
import com.lerx.dao.iface.ITempletAlbumGenreMainDao;
import com.lerx.entities.AlbumGenre;
import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.hql.entities.Rs;
import com.lerx.portal.obj.TempletSimple;
import com.lerx.sys.util.MavUtil;
import com.lerx.v5.util.TempletAlbumGenreUtil;

@RequestMapping("/action_albgenre")
@Controller
public class AlbgenreHandler {

	private static final String ADMINFORBID = "_admin.forbid_";
	private static final String SUCCESS = "jsp/result/success";
	private static final String FAILED = "jsp/result/failed";

	@Autowired
	private IAlbumGenreDao albumGenreDaoImpl;
	
	@Autowired
	private ITempletAlbumGenreMainDao templetAlbumGenreMainDaoImpl;

	@ModelAttribute
	public void getAlbumGenre(@RequestParam(value = "id", required = false) Long id, Map<String, Object> map) {
		if (id != null && id > 0) {
			map.put("albumGenre", albumGenreDaoImpl.findByID(id));
		}
	}
	
	@RequestMapping("/beforeAdd")
	public ModelAndView beforeAdd(HttpServletRequest request, Map<String, Object> map) {
		List<TempletSimple> list=TempletAlbumGenreUtil.queryAllSimple(templetAlbumGenreMainDaoImpl);
		map.put("templets", list);
		AlbumGenre albumGenre = new AlbumGenre();
		map.put("albumGenre", albumGenre);
		return MavUtil.mav("jsp/album/genre/add", "");
	}

	@RequestMapping("/add")
	@Token(ajax = false, log = true, mark = "album_genre--<add>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String add(@Valid AlbumGenre albumGenre, Errors result, Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		if (albumGenre.getId() == 0) {
			albumGenre.setComm(true);
			albumGenre.setStatus(true);
			albumGenre.setPoll(true);
			albumGenre.setCreateTime(System.currentTimeMillis());
			albumGenre.setOpen(true);
			albumGenre.setFree(true);
			
			TempletAlbumGenreMain templet=templetAlbumGenreMainDaoImpl.findDef();
			albumGenre.setTemplet(templet);
			if (albumGenreDaoImpl.add(albumGenre) == null) {
				return FAILED;
			} else {
				return SUCCESS;
			}
		} else {
			albumGenreDaoImpl.modify(albumGenre);
			return SUCCESS;
		}
	}

	// 进入修改页面
	@RequestMapping("/edit")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.nologin")
	public ModelAndView edit(Map<String, Object> map, HttpSession session, Long id) {

		AlbumGenre albumGenre = albumGenreDaoImpl.findByID(id);
		map.put("albumGenre", albumGenre);
		
		List<TempletSimple> list=TempletAlbumGenreUtil.queryAllSimple(templetAlbumGenreMainDaoImpl);
		map.put("templets", list);
		return MavUtil.mav("jsp/album/genre/add", "");

	}

	@ResponseBody
	@RequestMapping("/del")
	@Token(ajax = false, log = true, mark = "album_genre--<delete>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int del(Long id, HttpSession session) {
		boolean result = false;
		result = albumGenreDaoImpl.delByID(id);
		if (result) {
			return 0;
		} else {
			return -9;
		}

	}

	@RequestMapping("/list")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpSession session,
			Map<String, Object> map) {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Rs rs = albumGenreDaoImpl.query(page, pageSize, 0, 0);
		map.put("pageUrl", "/action_album_genre/list");
		map.put("rs", rs);

		return MavUtil.mav("jsp/album/genre/list", "");

	}
	
	@ResponseBody
	@RequestMapping(value = "/agree/{gid}")
	public String agree(@PathVariable(value = "gid", required = true) Long gid, HttpSession session) {
		AlbumGenre ag = albumGenreDaoImpl.findByID(gid);
		if (ag==null) {
			return "Error:-13";
		}
		String agreeTxt=ag.getAgreement();
		if (agreeTxt==null) {
			return "Null:-12";
		} else if (agreeTxt.trim().equals("")) {
			return "Null:-19";
		}else {
			return agreeTxt.trim();
		}

	}
	

}
