package com.example.ezImage.actions;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

public class ImageGrayScaler implements ImageAction {

	@Override
	public BufferedImage apply(BufferedImage img) {
		return Scalr.apply(img, Scalr.OP_GRAYSCALE);
	}
}
