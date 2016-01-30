package com.example.ezImage.models;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ProcessedImage {
	
	private Date downloadDate;
	private String filePath;
	private String url;
	private String urlMD5;
	// Transient
	BufferedImage img;
	
	public ProcessedImage(Date downloadDate, String filePath, String url, String urlMD5) {
		super();
		this.downloadDate = downloadDate;
		this.filePath = filePath;
		this.url = url;
		this.urlMD5 = urlMD5;
	}

	public ProcessedImage(String url) {
		super();
		this.url = url;
		this.urlMD5 = md5(url);
	}
	
	public Date getDownloadDate() {
		return downloadDate;
	}
	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUrl() {
		return url;
	}
	public void setSrcUrl(String url) {
		this.url = url;
	}
	public String getUrlMD5() {
		return urlMD5;
	}
	public void setUrlMD5(String urlMD5) {
		this.urlMD5 = urlMD5;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	private String md5(String s) {
	    try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        m.update(s.getBytes(), 0, s.length());
	        BigInteger i = new BigInteger(1,m.digest());
	        return String.format("%1$032x", i);         
	    } catch (NoSuchAlgorithmException e) {
	    	return null;
	    }
	}

	@Override
	public String toString() {
		return "ProcessedImage [downloadDate=" + downloadDate + ", filePath=" + filePath + ", url=" + url + ", urlMD5=" + urlMD5 + "]";
	}
	
	
}
