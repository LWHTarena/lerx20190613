package com.lerx.handlers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.dao.iface.IAlbumDao;
import com.lerx.dao.iface.IAlbumGenreDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.Album;
import com.lerx.entities.AlbumGenre;
import com.lerx.entities.Poll;
import com.lerx.entities.Role;
import com.lerx.entities.User;
import com.lerx.entities.VisitorsBook;
import com.lerx.sys.util.StringUtil;
import com.lerx.v5.util.ConfigUtil;

@RequestMapping("/action_album")
@Controller
public class AlbumHandler {

	@Autowired
	private IAlbumGenreDao albumGenreDaoImpl;

	@Autowired
	private IAlbumDao albumDaoImpl;

	@Autowired
	private IUserDao userDaoImpl;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	/*
	 * 申请
	 */
	@ResponseBody
	@RequestMapping("/apply")
	public int apply(String name,long gid, @CookieValue(value = "uid_lerx", required = false) String uid_lerx,
			HttpServletRequest request, HttpSession session) {
		if (name!=null) {
			name=name.trim();
			name = ConfigUtil.strFilter(messageSource, name);
			
			if (name.trim().equals("") ) {
				return -12;
			}
			if (name.indexOf("*") > -1) {
				return -18;
			}
		}else {
			return -12;
		}
		
		
		Album album=albumDaoImpl.findByName(name);
		User user = null;
		if (uid_lerx != null && !uid_lerx.trim().equals("") && StringUtil.isNumber(uid_lerx)) {
			long uid = Long.valueOf(uid_lerx);
			user = userDaoImpl.findByID(uid);

			Role role = null;
			if (user != null) {
				role = user.getRole();
			}else {
				return -3;
			}

			String mask = "";
			if (role != null) {
				mask = role.getMask();
			}
			/*if (mask.equals("0")) {
				return 0;
			}*/
			mask=","+mask+",";
			if (!mask.equals(",0,") && mask.indexOf(",alb_g0,")<0 && mask.indexOf(",alb_g"+gid+",")<0) {
				return -2;
			}
		}else {
			return -3;
		}
		if (album==null) {
			album = new Album();
			AlbumGenre ag=albumGenreDaoImpl.findByID(gid);
			if (ag==null) {
				return -11;
			}
			
			if (!ag.isStatus() || !ag.isFree()) {
				return -5;
			}
			
			/*
			 * 查查该用户是否已经有记录
			 */
			List<Album> la = albumDaoImpl.findByUid(user.getId(), gid, 0);
			if (la!=null && !la.isEmpty() && la.size()>0) {
				if (la.size() >= ag.getQuota()) {
					return -20;
				}
			}
			
			album.setGenre(ag);
			album.setName(name);
			album.setLeader(user);
			album.setStatus(true);
			album.setOpen(true);
			
			Poll poll=new Poll();
			
			album.setPoll(poll);
			
			VisitorsBook vbook = new VisitorsBook();
			vbook.setObjType(3);
			album.setVbook(vbook);
			album=albumDaoImpl.add(album);
			
			if (album==null) {
				return -1;
			}else {
				return 0;
			}
			
		}else {
			return -12;
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/findByName")
	public boolean agree(String name, HttpSession session) {
		Album album=albumDaoImpl.findByName(name);
		if (album==null) {
			return false;
		}else {
			return true;
		}
	}
	
	

}
