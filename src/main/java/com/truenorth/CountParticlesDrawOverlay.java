package com.truenorth;

import java.awt.Color;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>Scalp Analysis>CountParticlesDrawOverlay")
public class CountParticlesDrawOverlay implements Command {

	@Parameter
	private ImagePlus thresholdedImp;

	@Parameter
	int minSize;

	@Parameter
	int maxSize;

	@Parameter
	int minCircularity;

	@Parameter
	int maxCircularity;

	// optional image to draw overlay on
	@Parameter
	private ImagePlus overlayImp;

	@Parameter(type = ItemIO.OUTPUT)
	Roi[] roisArray;

	public void run() {

		ResultsTable rt = new ResultsTable();

		// create an roi manager
		RoiManager roim = new RoiManager(true);

		// Create the particle analyzer
		ParticleAnalyzer pa = new ParticleAnalyzer(
				ParticleAnalyzer.ADD_TO_MANAGER /*
												 * | ParticleAnalyzer.
												 * EXCLUDE_EDGE_PARTICLES
												 */,
				Measurements.AREA | Measurements.MEAN | Measurements.ELLIPSE | Measurements.CIRCULARITY, rt, minSize,
				maxSize, minCircularity, maxCircularity);

		pa.setRoiManager(roim);

		pa.setHideOutputImage(true);

		if (pa.analyze(thresholdedImp)) {
			System.out.println("All ok");
		} else {
			System.out.println("There was a problem in analyzing");
		}

		roisArray = roim.getRoisAsArray();

		if (overlayImp != null) {
			Utils.drawOverlayOnImage(overlayImp, roisArray, Color.BLUE);
			overlayImp.updateAndDraw();
		}

	}

}
