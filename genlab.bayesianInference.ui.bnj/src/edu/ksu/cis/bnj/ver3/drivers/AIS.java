/*
 * This file is part of Bayesian Network for Java (BNJ).
 * Version 3.3+
 *
 * BNJ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * BNJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BNJ in LICENSE.txt file; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * BNJ Version History
 * ---------------------------------------------
 * BN tools Jan 2000-May 2002
 *
 *  prealpha- January 200 - June 2001
 *	Benjamin Perry, Haipeng Guo, Laura Haverkamp
 *  Version 1- June 2001 - May 2002
 * 	Haipeng Guo, Benjamin Perry, Julie A. Thornton BNJ
 *
 * Bayesian Network for Java (BNJ).
 *  Version 1 - May 2002 - July 2003
 *  	release: v1.03a 29 July 2003
 * 	Infrastructure - Roby Joehanes, Haipeng Guo, Benjamin Perry, Julie A. Thornton
 *	Modules - Sonal S. Junnarkar
 *  Version 2 - August 2003 - July 2004
 *  	release: v2.03a 08 July 2004
 * 	Infrastructure - Roby Joehanes, Julie A. Thornton
 *	Modules - Siddharth Chandak, Prashanth Boddhireddy, Chris H. Meyer, Charlie L. Thornton, Bart Peinter
 *  Version 3 - August 2004 - Present
 *     	Infrastructure - Jeffrey M. Barber
 *	Modules - William H. Hsu, Andrew L. King, Chris H. Meyer, Julie A. Thornton
 * ---------------------------------------------
 *//*
 * Created on Jul 29, 2004
 *
 * 
 */
package edu.ksu.cis.bnj.ver3.drivers;

import edu.ksu.cis.bnj.ver3.core.BeliefNetwork;
import edu.ksu.cis.util.driver.Options;

/**
 * @author Andrew King
 *
 * This is the driver for the AIS code
 * TODO: need to get an interval and samples option in Options
 * then run AIS according to those parameters
 */
public class AIS {
	
	public static void onbn(Options opt, BeliefNetwork bn)
	{
		edu.ksu.cis.bnj.ver3.inference.approximate.sampling.AIS ais = new edu.ksu.cis.bnj.ver3.inference.approximate.sampling.AIS();
		int trials = opt.getInteger("trials", 1);
		int samples = opt.getInteger("samples", 2000);
		int interval = opt.getInteger("interval", 100);
		ais.setNumSamples(samples);
		ais.setInterval(interval);
		System.out.println("Samples: " + samples);
		System.out.println("Interval: " + interval);
		if (trials == 1)
		{
			trials = opt.getInteger("t", 1);
		}
		Options.outputln("running " + trials + " trials");
		for (int i = 0; i < trials; i++)
		{
			ais.run(bn);
		}
		Options.outputln(opt.renderInferenceResult(bn, ais));
	}
	public static void exec(Options opt)
	{
		Options.outputln("AIS");
		opt.begin();
		String f;
		while ((f = opt.file()) != null)
		{
			BeliefNetwork bn = Options.load(f);
			if (bn != null)
			{
				opt.BeginPerfMeasure();
				onbn(opt, bn);
				opt.EndPerfMeasure();
				opt.outputPerformanceReport(bn.getName());
			}
		}
	}

}
