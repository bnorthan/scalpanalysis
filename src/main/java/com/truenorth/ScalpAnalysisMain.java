package com.truenorth;

import java.io.File;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.type.numeric.RealType;

public class ScalpAnalysisMain {
	public static <T extends RealType<T>> void main(final String... args) throws Exception {
		// create the ImageJ application context with all available services
		final ImageJ ij = net.imagej.Main.launch(args);

		String imName = "/home/bnorthan/images/ij_list/hair transplant/aR5VYcropped.tif";

		final Dataset data = ij.dataset().open(new File(imName).getAbsolutePath());

		ij.command().run(ScalpAnalysisCommand.class, true, "data", data);

	}
}
