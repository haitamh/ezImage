package com.example.ezImage.processors;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.core.io.ClassPathResource;

import com.example.ezImage.models.ProcessedImage;

public class ImageReader extends FlatFileItemReader<ProcessedImage> {
	
	public ImageReader(String inputFile) {
		setResource(new ClassPathResource(inputFile));
		setLineMapper(new LineMapper<ProcessedImage>() {
			
			@Override
			public ProcessedImage mapLine(String url, int arg1) throws Exception {
				ProcessedImage processedImage = new ProcessedImage(url);
				
				return processedImage;
			}
		});
		
	}
}
