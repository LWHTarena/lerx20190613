package com.lerx.v5.util;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.entities.AlbumGenre;

public class AlbgenreUtil {
	
	
	public static String fmt(String lf,AlbumGenre ag){
		lf=AnalyzeUtil.replace(lf, "tag", "id", ""+ag.getId());
		lf=AnalyzeUtil.replace(lf, "tag", "name", ag.getName());
		lf=AnalyzeUtil.replace(lf, "tag", "navName", ag.getName());
		lf=AnalyzeUtil.replace(lf, "tag", "folder", ag.getFolder());
		lf=AnalyzeUtil.replace(lf, "tag", "staticFolder", ""); 		//静态目录，以后再写
		
		
		return lf;
	}

}
