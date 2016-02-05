package com.truenorth;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>Scalp Analysis>Log Filter Threshold")
public class OpsLogFilterThresholdCommand<T extends RealType<T>> implements Command {

	@Parameter
	private OpService ops;

	@Parameter
	private Img<T> img;

	@Parameter(type = ItemIO.OUTPUT)
	private Img<UnsignedByteType> thresholded;

	@Override
	@SuppressWarnings("unchecked")
	public void run() {

		// create a log kernel
		Img<T> logKernel = (Img<T>) ops.create().kernelLog(2, 4.0);

		// apply log filter
		Img<T> log = (Img<T>) ops.filter().convolve(img, logKernel);

		// apply reni entropy thresh
		Img<BitType> thresholdedBit = (Img<BitType>) ops.threshold().renyiEntropy(log);

		thresholded = ops.create().img(thresholdedBit, new UnsignedByteType());

		// it will be more convenient if the output is unsigned byte type

		// so create a scaler op
		Op scale = ops.op(Ops.Convert.Scale.class, UnsignedByteType.class, BitType.class);

		// and use a map to apply the scaling to each pixl
		ops.map(thresholded, thresholdedBit, scale);

	}

}
