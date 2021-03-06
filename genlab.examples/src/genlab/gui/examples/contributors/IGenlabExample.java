package genlab.gui.examples.contributors;

import java.io.File;
import java.util.Collection;

import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.model.meta.AlgoCategory;
import genlab.core.model.meta.IFlowType;
import genlab.core.projects.IGenlabProject;

public interface IGenlabExample {

	/**
	 * generate an instance of a genlab workflow. In this method, please ensure you always call workflow.addAlgoInstance only
	 * when you defined completly the algo instance in question - notably setting up parent and children. 
	 * @return
	 */
	public void fillInstance(IGenlabWorkflowInstance workflow);
	
	public String getFileName();
	
	public String getName();
	
	public String getDescription();
	
	/**
	 * Creates the files required for this example.
	 * @param resourcesDirectory
	 */
	public void createFiles(File resourcesDirectory);
	
	public GenlabExampleDifficulty getDifficulty();
	
	/**
	 * Returns the list of central flowtypes involved in this example
	 * @return
	 */
	public Collection<IFlowType<?>> getIllustratedFlowTypes();
	
	public Collection<AlgoCategory> getIllustratedAlgoCategories();
	
}
