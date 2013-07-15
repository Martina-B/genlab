package genlab.gui.graphiti.features;

import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.usermachineinteraction.GLLogger;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

/**
 * Manages the resizing of standard algo boxes
 * @see for resizing with invisible rect http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.graphiti.doc%2Fresources%2Fdocu%2Fgfw%2Fselection-behavior.htm
 * 
 * @author Samuel Thiriot
 *
 */
public class LayoutIAlgoFeature extends AbstractLayoutFeature {

	public static final int INVISIBLE_RECT_MARGIN_HORIZ = 5;

	public static final int ANCHOR_WIDTH = 10;
	
	
	public static final int MIN_HEIGHT = 100;
	 
	public static final int MIN_WIDTH = 100;
	    
	public LayoutIAlgoFeature(IFeatureProvider fp) {
		super(fp);
		
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		PictogramElement pe = context.getPictogramElement();
	    if (!(pe instanceof ContainerShape))
	    	return false;
	       
	    Object genlabObj = getBusinessObjectForPictogramElement(pe);
	    return (genlabObj != null) && (genlabObj instanceof IAlgoInstance);
	}

	/**
	 * returns true if something changed
	 * @param shape
	 * @return
	 */
	protected boolean manageResizing(int containerWidth, Shape shape) {

		boolean anythingChanged = false;
		
        GraphicsAlgorithm graphicsAlgorithm = shape.getGraphicsAlgorithm();
        IGaService gaService = Graphiti.getGaService();
        IDimension size =  gaService.calculateSize(graphicsAlgorithm);
        if (containerWidth != size.getWidth()) {
            if (graphicsAlgorithm instanceof Polyline) {
                Polyline polyline = (Polyline) graphicsAlgorithm;
                Point secondPoint = polyline.getPoints().get(1);
                Point newSecondPoint = gaService.createPoint(containerWidth+INVISIBLE_RECT_MARGIN_HORIZ, secondPoint.getY()); 
                polyline.getPoints().set(1, newSecondPoint);
                anythingChanged = true;
            } else if (graphicsAlgorithm instanceof Text) {
            	Object bo = getBusinessObjectForPictogramElement(shape);
            	if (bo != null && bo instanceof AlgoInstance) {
            		// if this is the top text
                	// ... resize it !
            		gaService.setWidth(graphicsAlgorithm, containerWidth);
            		anythingChanged = true;
            	} else {
            		// this is probably a text for anchor
            		Text text = (Text)graphicsAlgorithm;
            		if (text.getX() > ANCHOR_WIDTH*2) {
            			// this is a text aligned right
            			gaService.setLocationAndSize(
        						text, 
        						containerWidth/2, 
        						text.getY(), 
        						containerWidth/2-ANCHOR_WIDTH, 
        						text.getHeight()
        						);
        				
            		}
            	}
                
            }
        }
        
        return anythingChanged;

	}
	
	
	protected boolean manageResizing(int containerWidth, FixPointAnchor anchor) {

		boolean anythingChanged = false;
		
        GraphicsAlgorithm graphicsAlgorithm = anchor.getGraphicsAlgorithm();
        
        // do not process the anchors on the left
        if (anchor.getLocation().getX() == 0)
        	return false;
        
        //final int theoreticalX = 0;
        
        final int theoreticalX =  (containerWidth - INVISIBLE_RECT_MARGIN_HORIZ*2);
        
        if (graphicsAlgorithm.getX() != theoreticalX) {
        	GLLogger.traceTech("moving anchor horizontally from "+graphicsAlgorithm.getX()+" to "+theoreticalX+" over "+containerWidth, getClass());
        	//graphicsAlgorithm.setX(theoreticalX);
    		IGaService gaService = Graphiti.getGaService();
    		
    		anchor.setLocation(gaService.createPoint(
    				theoreticalX,
    				anchor.getLocation().getY()
    				));
    		
    		/*
        	gaService.setLocationAndSize(
        			graphicsAlgorithm, 
        			theoreticalX,
        			graphicsAlgorithm.getY(),
        			ANCHOR_WIDTH,
        			ANCHOR_WIDTH,
        			false
        			);
        			*/
        	anythingChanged = true;
//        	layoutPictogramElement(anchor);
        }
        
       
        
        return anythingChanged;

	}
	
	@Override
	public boolean layout(ILayoutContext context) {

		GLLogger.traceTech("layout of picto element: "+context, getClass());
		
        boolean anythingChanged = false;

        
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
     
        System.err.println("container size: "+containerShape.getGraphicsAlgorithm().getWidth());
        System.err.println("invisible rect: "+containerShape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().get(0).getWidth());
        
        
        // ensure min height and width are ok for invisible rectangle
        
        // height
        if (containerGa.getHeight() < MIN_HEIGHT) {
            containerGa.setHeight(MIN_HEIGHT);
            anythingChanged = true;
        }
 
        // width
        if (containerGa.getWidth() < MIN_WIDTH) {
            containerGa.setWidth(MIN_WIDTH);
            anythingChanged = true;
        }        
        
        int containerWidth = containerGa.getWidth() - INVISIBLE_RECT_MARGIN_HORIZ*2;

        // resize visible rectangle
        {
            GraphicsAlgorithm rectangle = containerGa.getGraphicsAlgorithmChildren().get(0);
            if (rectangle.getHeight() != containerGa.getHeight()) {
            	rectangle.setHeight(containerGa.getHeight());
            	anythingChanged = true;
            }
            if (rectangle.getWidth() != containerWidth) {
            	rectangle.setWidth(containerWidth);
            	anythingChanged = true;
            }        
            containerWidth = rectangle.getWidth();

        }
        
        System.err.println("container size: "+containerShape.getGraphicsAlgorithm().getWidth());
        System.err.println("invisible rect: "+containerShape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().get(0).getWidth());
        
        
        // rezize text, lines and this kind of stuff
        for (Shape shape : containerShape.getChildren()){
        	
        	anythingChanged = manageResizing(containerWidth, shape) || anythingChanged;
        	
        }
        
        
        // move anchors
        
        for (Anchor anchor: containerShape.getAnchors()) {
        	
        	anythingChanged = manageResizing(containerShape.getGraphicsAlgorithm().getWidth(), (FixPointAnchor)anchor) || anythingChanged;
            	
        }
        
        
        return anythingChanged;
        
	}

}
