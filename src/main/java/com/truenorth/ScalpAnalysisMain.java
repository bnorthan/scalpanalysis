package com.truenorth;

import java.io.File;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.type.numeric.RealType;

public class ScalpAnalysisMain {
	public static <T extends RealType<T>> void main(final String... args) throws Exception {
		
		System.setProperty("plugins.dir", "/home/bnorthan/fiji/Fiji.app");
		
		// comment this in to show imagej gui
		//final ImageJ ij=net.imagej.Main.launch(args);
		// or this one for headless imagej
		final ImageJ ij=new ImageJ();
	
		String imName = "/home/bnorthan/images/ij_list/hair transplant/aR5VYcropped.tif";

		final Dataset data = ij.dataset().open(new File(imName).getAbsolutePath());

		ij.command().run(ScalpAnalysisCommand.class, true, "data", data);

	}
}
