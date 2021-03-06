package genlab.core.model.instance;

/**
 * Describes listeners interested in the lifecycle of workflows
 * 
 * @author Samuel Thiriot
 *
 */
public interface IWorkflowListener {

	public void workflowCreation(IGenlabWorkflowInstance workflow);
	
	public void workflowChanged(IGenlabWorkflowInstance workflow);

	public void workflowOpened(IGenlabWorkflowInstance workflow);
	
	public void workflowSaving(IGenlabWorkflowInstance workflow);

	public void workflowSaved(IGenlabWorkflowInstance workflow);

	/**
	 * Sent when a workflow was automatically created (thus, by a program, like for examples).
	 * So listeners could for instance layout the diagram or any other relevant task.
	 * @param instance
	 */
	public void workflowAutomaticallyCreatedAndFinished(IGenlabWorkflowInstance instance);

	/**
	 * send when a workflow was loaded from persistence
	 * @param wf
	 */
	public void workflowLoaded(IGenlabWorkflowInstance instance);
}
