package genlab.gui.graphiti.diagram;

import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.graphiti.genlab2graphiti.GenlabNotificationService;
import genlab.gui.graphiti.genlab2graphiti.WorkflowListener;
import genlab.gui.graphiti.palette.WorkflowToolBehaviorProvider;

import java.util.Arrays;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.notification.INotificationService;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class GraphitiDiagramTypeProvider extends AbstractDiagramTypeProvider {

	public static final String GRAPH_TYPE_ID = "genlab.graphiti.diagtypes.workflow";
	public static final String PROVIDER_ID = "genlab.gui.graphiti.diagram.GraphitiDiagramTypeProvider";
	
	/**
	 * file extension for graphiti workflow diagrams.
	 * Has to be lowercase.
	 */
	public static final String GRAPH_EXTENSION = "wfdiag";

	private IToolBehaviorProvider[] toolBehaviorProviders;

	public static GraphitiDiagramTypeProvider lastInstanceCreated = null;

	public GraphitiDiagramTypeProvider() {
		super();
		GLLogger.debugTech("Graphiti diagram provider instanciated for graphtype "+GRAPH_TYPE_ID, getClass());
		
		// add feature provider
		setFeatureProvider(GraphitiFeatureProvider.getOrCreateFor(this));
		
		// create our behavior providers to return them later
		toolBehaviorProviders = new IToolBehaviorProvider[] { new WorkflowToolBehaviorProvider(this) };
		
		lastInstanceCreated = this;
		
	}
	
	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		return toolBehaviorProviders;
	}

	@Override
	public INotificationService getNotificationService() {
		return new GenlabNotificationService(this);
	}

	
	@Override
	public void init(Diagram diagram, IDiagramEditor diagramEditor) {
		
		super.init(diagram, diagramEditor);
		
		GLLogger.debugTech("at an init step of a diagram for a diagram editor", getClass());
		
		IGenlabWorkflowInstance workflow = (IGenlabWorkflowInstance) getFeatureProvider().getBusinessObjectForPictogramElement(diagram);
		
		if (workflow==null) {
			GLLogger.warnTech("too bad, not ready yet", getClass());
			return;

		}
		
		GLLogger.debugTech("now listening for this workflow", getClass());
		workflow.addListener(WorkflowListener.lastInstance);
			
		/*
		if(diagramEditor instanceof GenlabDiagramEditor) {
			
			GLLogger.debugTech("init of the diagram, attempting to reuse the independance solver from genlab workflow", getClass());
			
			GenlabDiagramEditor nonEmfDiagramEditor = (GenlabDiagramEditor) diagramEditor;
			
			DiagramEditorInput editorInput = (DiagramEditorInput) nonEmfDiagramEditor.getEditorInput();
			
			GraphitiFeatureProvider dfp = (GraphitiFeatureProvider)getFeatureProvider();
			
			// retrieve our workflow
			// ... attempt to load it from the mapping
			IGenlabWorkflowInstance workflow = (IGenlabWorkflowInstance)MappingObjects.getGenlabResourceFor(diagram);
			if (workflow == null) {
				workflow = GenlabPersistence.getPersistence().getWorkflowForFilename(nonEmfDiagramEditor.getFilename());
			}
			if (workflow == null) {
				workflow =  (IGenlabWorkflowInstance) getFeatureProvider().getBusinessObjectForPictogramElement(diagram); 
			}
			//IGenlabWorkflow workflow = (IGenlabWorkflow)dfp.getBusinessObjectForPictogramElement(diagram);
			if (workflow == null) {
				GLLogger.warnTech("unable to load the independance data from the workflow: workflow not found", getClass());
			} else {
				
				// retrieve our mapping file
				GLLogger.debugTech("initializing xstream for persistence...", getClass());
				
				GenLabIndependenceSolver independanceSolver = (GenLabIndependenceSolver)PersistenceUtils.getPersistenceUtils().loadAsXml(
						workflow.getAbsolutePath()+Genlab2GraphitiUtils.EXTENSION_FILE_MAPPING
						);
				
				if (independanceSolver != null) {
					independanceSolver._resolveIdsFromWorkflow(workflow);
					dfp._setIndependanceSolver(independanceSolver);	
				} else {
					GLLogger.warnTech("unable to read the independance solver from file", getClass());
				}
				
			}
			
		}
		*/
	}
}
