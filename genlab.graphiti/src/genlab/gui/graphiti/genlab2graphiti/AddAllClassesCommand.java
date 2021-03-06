package genlab.gui.graphiti.genlab2graphiti;

import genlab.core.commons.ProgramException;
import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.gui.graphiti.diagram.GraphitiDiagramTypeProvider;
import genlab.gui.graphiti.diagram.GraphitiFeatureProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;

/**
 * Used to create an empty graphiti diagram. 
 * 
 * @author Samuel Thiriot
 */
public class AddAllClassesCommand extends RecordingCommand {

	private IProject project;
	private TransactionalEditingDomain editingDomain;
	private Resource createdResource;
	private IGenlabWorkflowInstance workflow;
	private Diagram diagram = null;

	public AddAllClassesCommand(IProject project, TransactionalEditingDomain editingDomain, IGenlabWorkflowInstance workflow) {
		super(editingDomain);
		
		if (project == null)
			throw new ProgramException("no project provided; cannot create the workflow");
		
		this.project = project;
		this.editingDomain = editingDomain;
		this.workflow = workflow;
	}

	@Override
	protected void doExecute() {
		
		diagram = Graphiti.getPeCreateService().createDiagram(
				GraphitiDiagramTypeProvider.GRAPH_TYPE_ID, 
				workflow.getName(), 
				true
				);
		
		IDiagramTypeProvider dtp = GraphitiUi.getExtensionManager().createDiagramTypeProvider(
				diagram,
				GraphitiDiagramTypeProvider.PROVIDER_ID
				); 
		
		
		//((GraphitiFeatureProvider)dtp.getFeatureProvider()).getIndependanceSolver().
		
        // project = genlab.gui.Utils.findEclipseProjectForWorkflow(workflow)
		
		//genlab.gui.Utils.getEclipseURIForWorkflowFile(workflow);
		
		/*IFolder diagramFolder = project.getFolder(project.getProjectRelativePath().append(workflow.getRelativePath())); //$NON-NLS-1$
		IFile diagramFile = diagramFolder.getFile(
				workflow.getFilename()+
				"."+ //$NON-NLS-1$
				GraphitiDiagramTypeProvider.GRAPH_EXTENSION
				); */
		URI uri = URI.createPlatformResourceURI(
				"/"+
				project.getName()+
				"/"+
				genlab.gui.Utils.getWorkflowPathRelativeToProject(workflow).toString()+
				"."+GraphitiDiagramTypeProvider.GRAPH_EXTENSION, 
				true
				);
		createdResource = editingDomain.getResourceSet().createResource(uri);
		createdResource.getContents().add(diagram);
	
		
		//  TODO remove ? MappingObjects.register(diagram, workflow);
		
		// add a link between the workflow and the diagram file
		workflow.addObjectForKey(
				Genlab2GraphitiUtils.KEY_WORKFLOW_TO_GRAPHITI_FILE, 
				workflow.getAbsolutePath()
				);
		GraphitiFeatureProvider gfp = (GraphitiFeatureProvider)dtp.getFeatureProvider();
		gfp.associateWorkflowWithThisProvider(workflow);
		
		Genlab2GraphitiUtils.linkInTransaction(
				gfp,
				diagram, 
				workflow
				);
		
	}
	
	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * @return the createdResource
	 */
	public Resource getCreatedResource() {
		return createdResource;
	}
}