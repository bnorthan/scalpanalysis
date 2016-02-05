package com.truenorth;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandService;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import ij.ImagePlus;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>Scalp Analysis>Main Routine")
public class ScalpAnalysisCommand<T extends RealType<T>> implements Command {

	@Parameter
	CommandService cmd;
	
	@Parameter
	UIService ui;

	@Parameter
	OpService ops;

	@Parameter
	Dataset data;

	@Parameter(type = ItemIO.OUTPUT)
	ImagePlus overlay1;

	@Parameter(type = ItemIO.OUTPUT)
	ImagePlus overlay2;

	@Override
	@SuppressWarnings("unchecked")
	public void run() {

		try {

			// get size of image
			long xLen = data.dimension(data.dimensionIndex(Axes.X));
			long yLen = data.dimension(data.dimensionIndex(Axes.Y));

			// get the image
			ImgPlus<T> img = (ImgPlus<T>) data.getImgPlus();

			// crop blue channel
			ImgPlus<T> blue = ops.image().crop(img, Intervals.createMinMax(0, 0, 2, xLen - 1, yLen - 1, 2), true);

			// get a couple of copies of the blue channel as ImgPlus to use for the overlays 
			overlay1 = ImageJFunctions.wrapUnsignedByte(blue.getImg(), "method 1");
			overlay2 = ImageJFunctions.wrapUnsignedByte(blue.getImg(), "method 2");
 
			// get another copy of the blue channel to use for processing
			ImagePlus blueImp2 = ImageJFunctions.wrapUnsignedByte(blue.getImg(), "blue");
			
			// module object is used to get the outputs from commands
			Module module = null;
			
			////// Method I ///////////////////////////////////////////////////////////////////////////////////////

			// perform the log filter threshold command
			module = cmd.run(OpsLogFilterThresholdCommand.class, true, "img", blue).get();

			// get the output
			Img<BitType> thresholded = (Img<BitType>) module.getOutput("thresholded");
			// and convert it to IJ1 ImagePlus
			ImagePlus thresholdedImp = ImageJFunctions.wrapUnsignedByte(thresholded, "thresholded");

			// run count particles/draw overlay command on the thresholded image calculated above 
			module = cmd.run(CountParticlesDrawOverlay.class, true, "thresholdedImp", thresholdedImp, "minSize", 0,
					"maxSize", 200, "minCircularity", 0, "maxCircularity", 100, "overlayImp", overlay1).get();
		
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			ui.show(overlay1);
			
			///// Method II /////////////////////////////////////////////////////////////////////////////////////////
			
			// try another method, this method uses IJ1 routines and changes the image in place
			module = cmd.run(IJ1LocalAutoThreshAndOpenCommand.class, true, "imp", blueImp2, "radius", 200).get();

			// run count particles/draw overlay command on the thresholded image calculated above
			module = cmd.run(CountParticlesDrawOverlay.class, true, "thresholdedImp", blueImp2, "minSize", 0, "maxSize",
					4000, "minCircularity", 0, "maxCircularity", 100, "overlayImp", overlay2).get();
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
		} catch (Exception ex) {
			// TODO proper exception handling
		}
	}
}
