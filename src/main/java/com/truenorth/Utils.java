package com.truenorth;

import java.awt.Color;
import java.util.List;

import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.img.Img;

public class Utils {

	public static void drawParticleOnImage(ImagePlus imp, Roi roi, Color color) {
		imp.getProcessor().setColor(color);
		imp.getProcessor().draw(roi);
	}

	public static void drawParticlesOnImage(ImagePlus imp, Roi[] rois, Color color) {
		for (Roi roi : rois) {
			drawParticleOnImage(imp, roi, color);
		}
	}

	public static void drawOverlayOnImage(ImagePlus imp, Roi[] rois, Color color) {

		Overlay overlay = new Overlay();
		for (int i = 0; i < rois.length; i++)
			overlay.add(rois[i]);
		
		imp.setOverlay(overlay);

	}

	static public Interval RoiToInterval(Roi roi, Img<?> img) {

		int n = img.numDimensions();

		long start[] = new long[n];
		long end[] = new long[n];

		start[0] = (long) roi.getBounds().getX();
		start[1] = (long) roi.getBounds().getY();

		end[0] = start[0] + (long) roi.getBounds().getWidth() - 1;
		end[1] = start[1] + (long) roi.getBounds().getHeight() - 1;

		for (int d = 2; d < n; d++) {
			start[d] = 0;
			end[d] = img.dimension(d) - 1;
		}

		return new FinalInterval(start, end);

	}

}
