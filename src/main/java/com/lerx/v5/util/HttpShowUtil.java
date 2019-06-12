package com.lerx.v5.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.analyze.vo.FindedDataAnalyzeResult;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IGroupDao;
import com.lerx.entities.AlbumGenre;
import com.lerx.entities.Article;
import com.lerx.entities.ArticleGroup;
import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.entities.TempletPortalMain;
import com.lerx.entities.TempletSubElement;
import com.lerx.portal.obj.EnvirSet;
import com.lerx.portal.obj.FdarDoReturn;
import com.lerx.portal.obj.GlobalTagsAnalyzeReturn;
import com.lerx.sys.obj.EnvParms;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.StringUtil;

public class HttpShowUtil {

	// 初始化html
	public static String htmlInit(TempletPortalMain template, TempletSubElement elTemplate,
			ResourceBundleMessageSource messageSource) {
		String html;
		TempletSubElement el = TempletPortalUtil.elInit(template.getElPublic(), elTemplate);
		html = el.getBorderCode();

		if (html == null || html.trim().equals("")) {
			html = FileUtil.readRes(messageSource, "template_main_outnormal");
		}
		// 当前子模块代码标签处理
		html = TempletPortalUtil.elCodeReplace(html, el);
		// 公共模块代码标签处理
		html = TempletPortalUtil.mainCodeReplace(html, template);

		return html;
	}

	// 初始化html
	public static String htmlInit(TempletAlbumGenreMain template, TempletSubElement elTemplate,
			ResourceBundleMessageSource messageSource) {
		String html;
		TempletSubElement el = TempletPortalUtil.elInit(template.getElPublic(), elTemplate);
		html = el.getBorderCode();

		if (html == null || html.trim().equals("")) {
			html = FileUtil.readRes(messageSource, "template_main_outnormal");
		}
		// 当前子模块代码标签处理
		html = TempletAlbumGenreUtil.elCodeReplace(html, el);
		// 公共模块代码标签处理
		html = TempletAlbumGenreUtil.mainCodeReplace(html, template);

		return html;
	}

	public static String nullTemplate(ResourceBundleMessageSource ms) {
		String error = ms.getMessage("err.template.notspecified", null,
				"The portal template for the specified output is not found at present!", null);
		String html = FileUtil.readRes(ms, "template_main_outnormal");
		html = AnalyzeUtil.replace(html, "code", "main", error);
		html = AnalyzeUtil.replace(html, "tag", "title", error);
		return html;
	}
	
	public static String argErrTemplate(ResourceBundleMessageSource ms) {
		String error = ms.getMessage("error.args", null,
				"Args error!", null);
		String html = FileUtil.readRes(ms, "template_main_outnormal");
		html = AnalyzeUtil.replace(html, "code", "main", error);
		html = AnalyzeUtil.replace(html, "tag", "title", error);
		return html;
	}

	public static GlobalTagsAnalyzeReturn portalGlobalTagsAnalyze(EnvirSet es, String station) {
		GlobalTagsAnalyzeReturn gtar = new GlobalTagsAnalyzeReturn();
		gtar.setEs(es);
		ResourceBundleMessageSource messageSource = es.getMessageSource();
		TempletPortalMain template = TempletPortalUtil.currTemplet(es);
		
		if (template == null) {
			gtar.setHtml(HttpShowUtil.nullTemplate(messageSource));

			return gtar;
		}
		
		TempletSubElement elTemplate = TempletPortalUtil.elInitByTagStr(template, station);
		String html = HttpShowUtil.htmlInit(template, elTemplate, messageSource);
		html = StringUtil.clear65279(html);
		String imgHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_img");
		// 优先处理文章
		Article artCurr;
		EnvParms ep = HttpUtil.epInit(es.getRequest(), es.getResponse(), null, messageSource);
		if (es.getAid() > 0L) {
			artCurr = es.getArticleDaoImpl().findByID(es.getAid());
			// 这个地方要完整替换标签
			html = ArticleUtil.fmt(ep, html, artCurr, imgHtmlTemplet, 0, 1, true);
		}
		ArticleGroup agCurr = null;
		if (es.getGid() > 0L) {
			html = AnalyzeUtil.replace(html, "tag", "gid", "" + es.getGid());
			agCurr = es.getGroupDaoImpl().findByID(es.getGid());
			if (agCurr != null) {
				html = GroupUtil.fmt(html, agCurr);
			}
		} else {
			html = AnalyzeUtil.replace(html, "tag", "gid", "0");
		}
		String delimiter = TempletPortalUtil.sundriesTag(messageSource, template, "delimiter");
		Set<FindedDataAnalyzeResult> fdarSet = AnalyzeUtil.find(html);
		List<FindedDataAnalyzeResult> fdarList = new ArrayList<FindedDataAnalyzeResult>();
		for (FindedDataAnalyzeResult fdar : fdarSet) {
			fdarList.add(fdar);
		}
		/*
		 * 通过排序，将有spare1的排到最后
		 */
		Comparator<FindedDataAnalyzeResult> comparator = new Comparator<FindedDataAnalyzeResult>() {
			public int compare(FindedDataAnalyzeResult a1, FindedDataAnalyzeResult a2) {
				// 先排通过数
				if (a1.getDsp().getSpare1() != a2.getDsp().getSpare1()) {
					return (int) (a1.getDsp().getSpare1() - a2.getDsp().getSpare1());
				} else {
					return (int) (a2.getDsp().getSpare1() - a1.getDsp().getSpare1());
				}
			}
		};
		Collections.sort(fdarList, comparator);
		
		String fiterGids="";
		if (fdarList != null && fdarList.size() > 0) {
			for (FindedDataAnalyzeResult fdar : fdarList) {
				/*System.out.println();
				System.out.println();
				System.out.println(step+"step  WholeTag:"+fdar.getWholeTag());*/
				if (fdar != null) {
					fdar = TempletPortalUtil.fmt(fdar, template, elTemplate); // 此处的fdar中的dsp中的loopStr中必定有格式化字符串

					if (fdar == null || fdar.getDsp() == null || fdar.getDsp().getLoopFormatStr() == null) {
						continue;
					}
					es.setTmpStr(fiterGids);   //本行将已处理过的gid通过tmpStr传送，如果有，则不处理
					FdarDoReturn fdr=TempletPortalUtil.tagToData(html, es, fdar);
					html = fdr.getHtml();
					//如果处理的栏目的文章数据，将返回gid，生成一个判别字符串加入fiterGids，fiterGids将在最后处理栏目聚集的文章数据时进行判别
					if (fdr.getGid()>0) {
						fiterGids += " f<"+fdr.getGid()+">";
					}

					// 栏目处理
					if (es.getGid() > 0L) {
						if (fdar.getDsp().getGid() == 0) {
							fdar.getDsp().setGid(es.getGid());
						}

						if (agCurr != null) {
							if (agCurr.getHtmlOwn() != null && !agCurr.getHtmlOwn().trim().equals("")) {
								html = AnalyzeUtil.replace(html, "code", "htmlOwn", agCurr.getHtmlOwn());
							} else {
								html = AnalyzeUtil.replace(html, "code", "htmlOwn", "");
							}

						}

						html = TempletPortalUtil.tagToNavs(html, es, fdar);
					}

				}

			}
		}

		if (es.getGid() > 0L) {
			
			String href = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_href_simplest");
			if (href==null || href.trim().equals("")) {
				href="<a href=\"{$tag:href$}\" >{$tag:name$}</a>";	
			}
			
			if (es.getAid() == 0) {
				html = AnalyzeUtil.replace(html, "tag", "station",
						GroupUtil.locationStr(es, href, delimiter, false));
			} else {
				html = AnalyzeUtil.replace(html, "tag", "station",
						GroupUtil.locationStr(es, href, delimiter, true));
			}

		}

		html = AnalyzeUtil.replace(html, "tag", "pageCurr", TempletPortalUtil.sundriesTag(template, station));
		html = AnalyzeUtil.replace(html, "tag", "delimiter", delimiter);
		html = AnalyzeUtil.replace(html, "tag", "hrefTarget", StringUtil.nullFilter2(elTemplate.getTargetStr()));
		gtar.setHtml(html);
		gtar.setEs(es);
		return gtar;
	}

	/*
	 * 专辑类首页
	 */
	public static GlobalTagsAnalyzeReturn albgenreGlobalTagsAnalyze(EnvirSet es, String station) {
		GlobalTagsAnalyzeReturn gtar = new GlobalTagsAnalyzeReturn();
		gtar.setEs(es);
		ResourceBundleMessageSource messageSource = es.getMessageSource();
		AlbumGenre ag = es.getAlbumGenreDaoImpl().findByID(es.getGid());
		if (ag==null) {
			gtar.setHtml(argErrTemplate(messageSource));

			return gtar;
		}
		TempletAlbumGenreMain template = ag.getTemplet();
		if (template == null) {
			gtar.setHtml(nullTemplate(messageSource));

			return gtar;
		}

		TempletSubElement elTemplate = TempletAlbumGenreUtil.elInitByTagStr(template, station);
		String html = HttpShowUtil.htmlInit(template, elTemplate, messageSource);
		html = StringUtil.clear65279(html);
		html = AnalyzeUtil.replace(html, "tag", "gid", "" + es.getGid());
		html = AlbgenreUtil.fmt(html, ag);
		String delimiter = TempletAlbumGenreUtil.sundriesTag(messageSource, template, "delimiter");
		Set<FindedDataAnalyzeResult> fdarSet = AnalyzeUtil.find(html);
		if (fdarSet != null && fdarSet.size() > 0) {
			for (FindedDataAnalyzeResult fdar : fdarSet) {
				if (fdar != null) {
					if (fdar == null || fdar.getDsp() == null || fdar.getDsp().getLoopFormatStr() == null) {
						continue;
					}
					html = TempletAlbumGenreUtil.tagToData(html, es, fdar);
				}
			}
		}
		html = AnalyzeUtil.replace(html, "tag", "delimiter", delimiter);
		gtar.setHtml(html);
		gtar.setEs(es);
		return gtar;
	}

	// 更新栏目热度
	public static void updateAgHotn(IGroupDao groupDaoImpl, ArticleGroup group, boolean newip, int hotnOfVisit,
			int hotnOfIP) {
		int hotn;
		if (newip) {
			hotn = hotnOfVisit;
		} else {
			hotn = hotnOfIP;
		}
		if (hotn > 0) {
			group.setHotn(group.getHotn() + hotn);
			groupDaoImpl.modifySimple(group);
		}
	}

	// 更新文章热度
	public static void updateArtHotn(IArticleDao articleDaoImpl, Article art, boolean newip, int hotnOfVisit,
			int hotnOfIP) {
		int hotn;
		if (newip) {
			hotn = hotnOfVisit;
		} else {
			hotn = hotnOfIP;
		}
		if (hotn > 0) {
			art.setHotn(art.getHotn() + hotn);
			articleDaoImpl.modify(art);
		}
	}

}
