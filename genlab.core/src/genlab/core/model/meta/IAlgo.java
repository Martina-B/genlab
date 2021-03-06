package genlab.core.model.meta;

import genlab.core.exec.IExecution;
import genlab.core.model.IGenlabResource;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.parameters.Parameter;

import java.util.Collection;
import java.util.Set;

import org.junit.Ignore;
import org.osgi.framework.Bundle;

/**
 * An algo is a basic processing entity that works this way:
 * can be plugged to inputs, can process these inputs to provide outputs.
 * This class refers to an algo available, not an algo instanciated into 
 * a workflow.
 * 
 * TODO add group to sort things.
 * 
 * @author Samuel Thiriot
 */
public interface IAlgo extends IGenlabResource {
	
	public String getName();
	
	public String getDescription();
		
	/**
	 * Returns the set of inputs that is declared for this algo.
	 * Note that instances might add some inputs !
	 * @return
	 */
	public Set<IInputOutput> getInputs();
	
	/**
	 * Returns the set of outputs declared for this algo.
	 * Node that instances of this algo might add outputs !
	 * @return
	 */
	public Set<IInputOutput> getOuputs();
	

	public boolean containsInput(IInputOutput input);
	public boolean containsOutput(IInputOutput output);
	

	public IInputOutput<?> getInputInstanceForId(String inputId);

	public IInputOutput<?> getOutputInstanceForId(String outputId);
	
	/**
	 * Returns true if the algorithm is available in the current environment 
	 * (dependant software, libs, etc) or false if it is not the case.
	 * @return
	 */
	public boolean isAvailable();
	
	/**
	 * Creates an instance into the workflow instance passed as parameter. 
	 * Note that caller still have to add the instance to this workflow.
	 * @param workflow
	 * @return
	 */
	public IAlgoInstance createInstance(IGenlabWorkflowInstance workflow);
	public IAlgoInstance createInstance(String id, IGenlabWorkflowInstance workflow);


	/**
	 * computes the algo on these input types. 
	 * Note that not all the algos can run in this way ("static")
	 * without instance. So some algos could raise an NotImplementedException there.
	 * @param inputs
	 * @return
	 */
	public IAlgoExecution createExec(IExecution execution, AlgoInstance algoInstance);
 
	
	/**
	 * Returns the category of the algo, in the form "category1/subcategory1/subsub" (various number of levels, 2 recommanded).
	 * Would be better to use the constants defined here.
	 * Constants have to be localized.
	 * @return
	 */
	public String getCategoryId();
	
	/**
	 * Returns the list of parameters (not inputs: parameters)
	 * @return
	 */
	public Collection<Parameter<?>> getParameters();
	
	public Parameter<?> getParameter(String id);

	public boolean hasParameter(String id);

	/**
	 * Returns a path to retrieve an icon / image for this algo
	 * or null if none. The path should be absolute, 
	 * so any plugin can find the file.
	 * @return
	 */
	public String getImagePath16X16();
	public String getImagePath32X32();
	public String getImagePath64X64();
	public String getImagePathBig();
	
	/**
	 * Returns the OSGI bundle which provides this algo. Usefull
	 * to load related infos like images. 
	 * @return
	 */
	public Bundle getBundle();
	
	/**
	 * Returns true if an algo of this type may be contained into the instance passed as parameter.
	 * @param algoInstance
	 * @return
	 */
	public boolean canBeContainedInto(IAlgoInstance algoInstance);

	/**
	 * Returns true if an algo of this type may be contained into the type of algo container passed as parameter
	 * @param algoContainer
	 * @return
	 */
	public boolean canBeContainedInto(IAlgoContainer algoContainer);


	/**
	 * Returns the priority for a constant for intuitive creation, wathever
	 * the context (the container). SHould be a number between 0 and 99, the higher
	 * the more powerfull.
	 * @return
	 */
	public Integer getPriorityForIntuitiveCreation();

	
}
