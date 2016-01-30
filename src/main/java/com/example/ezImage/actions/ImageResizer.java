package com.example.ezImage.actions;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

public class ImageResizer implements ImageAction {

	@Override
	public BufferedImage apply(BufferedImage img) {
		return Scalr.resize(img, Scalr.Mode.FIT_EXACT, 200, 200);
	}
}
