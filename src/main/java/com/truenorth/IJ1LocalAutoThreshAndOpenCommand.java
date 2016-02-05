package com.truenorth;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import ij.IJ;
import ij.ImagePlus;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>Scalp Analysis>Threshold Open")
public class IJ1LocalAutoThreshAndOpenCommand implements Command {

	@Parameter
	private ImagePlus imp;

	@Parameter
	private int radius = 100;

	@Override
	public void run() {

		// threshold using the auto local threshold
		IJ.run(imp, "Options...", "iterations=1 count=1 black pad");

		IJ.run(imp, "Auto Local Threshold", "method=Bernsen radius=" + radius + " parameter_1=0 parameter_2=0 white");

		IJ.run(imp, "Open", "");

	}
}
