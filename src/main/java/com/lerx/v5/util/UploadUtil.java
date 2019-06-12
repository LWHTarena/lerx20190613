package com.lerx.v5.util;

import com.lerx.entities.UploadedFile;
import com.lerx.sys.obj.FileInf;

public class UploadUtil {
	
	public static UploadedFile coverFromFi(FileInf fi){
		UploadedFile uf = new UploadedFile();
		uf.setUrl(fi.getUrl());
		uf.setFullPath(fi.getFullPath());
		uf.setExt(fi.getExt());
		uf.setNameNoExt(fi.getNameNoExt());
		uf.setRealPath(fi.getRealPath());
		uf.setSize(fi.getSize());
		uf.setUploadDatetime(fi.getAddtime());
		uf.setMd5(fi.getMd5());
		return uf;
	}

}
