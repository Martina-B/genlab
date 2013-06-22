package genlab.gui.graphiti.features;

import genlab.core.model.instance.IAlgoInstance;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.graphiti.GraphitiImageProvider;
import genlab.gui.views.ParametersView;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * This feature opens the parameters view for algo instances which need it.
 * 
 * @author Samuel Thiriot
 *
 */
public class OpenParametersFeature extends AbstractCustomFeature {

	
	public OpenParametersFeature(IFeatureProvider fp) {
		super(fp);
		System.err.println("open parameters created");

	}

	
	@Override
	public boolean canExecute(ICustomContext context) {
		
		System.err.println("open parameters can execute ?");
		
		if (context.getInnerPictogramElement() == null)
			return false;
		
		final Object value = getBusinessObjectForPictogramElement(context.getInnerPictogramElement());
		if (!(value instanceof IAlgoInstance))
			return false;
		
		IAlgoInstance algoInstance = (IAlgoInstance)value;
		
		return !algoInstance.getAlgo().getParameters().isEmpty();
		
	}


	@Override
	public void execute(ICustomContext context) {
		
		GLLogger.debugTech("opening preferences...", getClass());
		
		final IAlgoInstance algoInstance = (IAlgoInstance)getBusinessObjectForPictogramElement(context.getInnerPictogramElement());
		
		try {
			IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
					// id of the view (provided by the genlab.gui package)
					"genlab.gui.views.ParametersView",
					// if of the content (so several instances can be opened)
					algoInstance.getId(),
					IWorkbenchPage.VIEW_ACTIVATE
					);
			// transmit info to enable the view to load what is required
			WorkbenchPart v = (WorkbenchPart)view;
			v.setPartProperty(
					ParametersView.PROPERTY_PROJECT_ID, 
					algoInstance.getWorkflow().getProject().getId()
					);
			v.setPartProperty(
					ParametersView.PROPERTY_WORKFLOW_ID, 
					algoInstance.getWorkflow().getId()
					);
			v.setPartProperty(
					ParametersView.PROPERTY_ALGOINSTANCE_ID, 
					algoInstance.getId()
					);
			
		} catch (PartInitException e) {
			GLLogger.errorTech("error while attempting to open preferences: "+e.getLocalizedMessage(), getClass(), e);
		}

	}

	public String getImageId() {
		
		return GraphitiImageProvider.PARAMETERS_ID;
	}
}
