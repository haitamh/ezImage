package com.example.ezImage.actions;

import java.awt.image.BufferedImage;

public interface ImageAction {
	
	BufferedImage apply(BufferedImage img);
	
}
