package genlab.gui.graphiti.features;

import genlab.core.commons.ProgramException;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.usermachineinteraction.GLLogger;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

public class DeleteIAlgoInstanceFeature extends DefaultDeleteFeature {

	public DeleteIAlgoInstanceFeature(IFeatureProvider fp) {
		super(fp);

	}

	@Override
	public void delete(IDeleteContext context) {
		
		IAlgoInstance algoInstance = (IAlgoInstance) getBusinessObjectForPictogramElement(context.getPictogramElement());

		// delete graphiti objects
		super.delete(context);
		
		GLLogger.debugTech("IAlgoInstance removed from the diagram, removing it from genlab as well", getClass());
		
		// delete the corresponding IAlgoInstance
		algoInstance.delete();
		
		/*
		IGenlabWorkflow workflow = (IGenlabWorkflow) MappingObjects.removeGenlabResourceFor(context.getPictogramElement());
		MappingObjects.removeGenlabResourceFor(workflow.getAbsolutePath());
		MappingObjects.removeGenlabResourceFor(workflow.getAbsolutePath()+"."+GraphitiDiagramTypeProvider.GRAPH_EXTENSION);
		*/
	}

	@Override
	public boolean canDelete(IDeleteContext context) {
		return true;
	}

	
	
	
}
