package com.example.ezImage.processors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.example.ezImage.models.ProcessedImage;

public class ImageWriter extends JdbcBatchItemWriter<ProcessedImage> {
	
	private static final Logger log = LoggerFactory.getLogger(ImageWriter.class);
	
	private String dirPath;
	
	public ImageWriter(DataSource dataSource, String dirPath) {
		super();
		this.dirPath = dirPath;
		setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ProcessedImage>());
        setSql("INSERT INTO images (downloadDate, filepath, url, md5) VALUES (:downloadDate, :filePath, :url, :urlMD5)");
        setDataSource(dataSource);
	}

	@Override
	public void write(List<? extends ProcessedImage> images) throws Exception {
		List<ProcessedImage> savedImages = new ArrayList<>(images.size());
		// save images to disk
		for (ProcessedImage processedImage : images) {
			BufferedImage img = processedImage.getImg();
			// use original file name
			String fileName = getFileName(processedImage.getUrl());
			String filePath = dirPath + File.separator + fileName;
			processedImage.setFilePath(filePath);
			File imageFile = new File(filePath);
		    try {
				boolean write = ImageIO.write(img, getFileFormat(fileName), imageFile);
				if(false == write) {
					// ImageIO.write returns false if no appropriate writer is found
					log.warn("ImageIO.write returned false: no appropriate writer is found, fileName:" + fileName);
					continue;
				}
			} catch (IOException e) {
				// save successful records to db
				super.write(savedImages);
				throw e;
			}
		    
		    img.flush();
		    processedImage.setImg(null);
		    savedImages.add(processedImage);
		}
		
		// save records to db
		super.write(savedImages);
	}
	
	private String getFileName(String urlText) throws MalformedURLException {
		URL url = new URL(urlText);
		String[] splits = url.getPath().split("/");
		return splits[splits.length - 1];
	}
	
	private String getFileFormat(String fileName) {
		String[] splits = fileName.split("\\.");
		return splits[splits.length - 1];
	}
}
