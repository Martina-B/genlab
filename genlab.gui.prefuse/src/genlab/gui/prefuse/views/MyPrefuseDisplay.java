/*******************************************************************************
 * Copyright (c) 2009-2012 Samuel Thiriot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Samuel Thiriot - initial API and implementation
 ******************************************************************************/
package genlab.gui.prefuse.views;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.data.expression.Predicate;
import prefuse.util.display.DebugStatsPainter;
import prefuse.util.display.PaintListener;

/**
 * Just inherits from the prefuse Display class, in order to publish some protected methods.
 * 
 * @author Samuel Thiriot
 *
 */
public final class MyPrefuseDisplay extends Display {

	public MyPrefuseDisplay() {
		
	}

	public MyPrefuseDisplay(Visualization visualization) {
		super(visualization);
		
	}

	public MyPrefuseDisplay(Visualization visualization, String predicate) {
		super(visualization, predicate);
	}

	public MyPrefuseDisplay(Visualization visualization, Predicate predicate) {
		super(visualization, predicate);
	}
	
	 /**
     * Saves a copy of this display as an image to the specified output stream.
     * @param output the output stream to write to.
     * @param format the image format (e.g., "JPG", "PNG"). The number and kind
     * of available formats varies by platform. See
     * {@link javax.imageio.ImageIO} and related classes for more.
     * @param scale how much to scale the image by. For example, a value of 2.0
     * will result in an image with twice the pixel width and height of this
     * Display.
     * @return true if image was successfully saved, false if an error occurred.
     */
    public BufferedImage getBufferedImage(double scale)
    {
        try {
            // get an image to draw into
            Dimension d = new Dimension((int)(scale*getWidth()),
                                        (int)(scale*getHeight()));
            BufferedImage img = getNewOffscreenBuffer(d.width, d.height);
            Graphics2D g = (Graphics2D)img.getGraphics();
            
            // set up the display, render, then revert to normal settings
            Point2D p = new Point2D.Double(0,0);
            zoom(p, scale); // also takes care of damage report
            boolean q = isHighQuality();
            setHighQuality(true);
            paintDisplay(g, d);
            setHighQuality(q);
            zoom(p, 1/scale); // also takes care of damage report
            
            // save the image and return
            return img;
            
            //ImageIO.write(img, format, output);
            //return true;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
    
    private PaintListener m_debug = null;

    @Override
    protected void registerDefaultCommands() {
    	// erase the automatic registration of listeners that are not usefull here.
    	if (m_debug == null) {
            m_debug = new DebugStatsPainter();
            addPaintListener(m_debug);
        } 
    }

}
