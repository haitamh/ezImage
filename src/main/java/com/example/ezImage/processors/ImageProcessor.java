package com.example.ezImage.processors;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.batch.item.ItemProcessor;

import com.example.ezImage.actions.ImageGrayScaler;
import com.example.ezImage.actions.ImageResizer;
import com.example.ezImage.actions.ImageAction;
import com.example.ezImage.models.ProcessedImage;

public class ImageProcessor implements ItemProcessor<ProcessedImage, ProcessedImage> {
	
	private List<ImageAction> actions = new LinkedList<>();
	
	public ImageProcessor() {
		super();
		
		actions.add(new ImageResizer());
		actions.add(new ImageGrayScaler());
		
	}



	@Override
	public ProcessedImage process(ProcessedImage processedImage) throws MalformedURLException, IOException {
		
		// download image from url
		// this process could be extracted to a separate step and configure the batch job to parallel the steps
		BufferedImage image = ImageIO.read(new URL(processedImage.getUrl()));
		if(null == image) {
			throw new IOException("Failed to download from:" + processedImage.getUrl());
		}
		
		processedImage.setImg(image);
		processedImage.setDownloadDate(new Date());
		
		// apply sequence of editing actions on the image
		for (ImageAction imageAction : actions) {
			BufferedImage source = processedImage.getImg();
			BufferedImage result = imageAction.apply(source);
			source.flush();
			processedImage.setImg(result);
		}
		
		return processedImage;
	}
}
