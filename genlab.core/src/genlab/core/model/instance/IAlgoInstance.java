package genlab.core.model.instance;

import genlab.core.exec.IExecution;
import genlab.core.model.IGenlabResource;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.meta.IAlgo;
import genlab.core.model.meta.IInputOutput;
import genlab.core.model.meta.InputOutput;
import genlab.core.parameters.Parameter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IAlgoInstance extends IGenlabResource {

	public String getId();
	
	/**
	 * The name of an algo instance is provided by the user, 
	 * and helps him to understand its role in the workflow
	 */
	public String getName();

	public void setName(String novelName);
	
	public IAlgo getAlgo();
	
	
	/**
	 * Returns the set of inputs that is declared for this algo instance and this algo.
	 * Note that instances might add some inputs !
	 * @return
	 */
	public Set<IInputOutput> getInputs();
	
	/**
	 * Returns the set of outputs declared for this algo instance and this algo.
	 * Node that instances of this algo might add outputs !
	 * @return
	 */
	public Set<IInputOutput> getOuputs();
	
	public boolean containsInput(IInputOutput input);
	public boolean containsOutput(IInputOutput output);
	
	/**
	 * Returns the executable object.
	 * @return
	 */
	public IAlgoExecution execute(IExecution execution);

	/**
	 * Returns that workflow that contains this algo instance (returns null if no parent).
	 * @return
	 */
	public IGenlabWorkflowInstance getWorkflow();
	
	public void _setWorkflowInstance(IGenlabWorkflowInstance  workflow);
	
	/**
	 * Lists all the inputs of this instance
	 * @return
	 */
	public Collection<IInputOutputInstance> getInputInstances();
	

	public Collection<IConnection> getAllIncomingConnections();
	
	

	/**
	 * Lists all the outputs of this instance
	 * @return
	 */
	public Collection<IInputOutputInstance> getOutputInstances();

	public IInputOutputInstance getInputInstanceForInput(String inputId);

	public IInputOutputInstance getInputInstanceForInput(IInputOutput<?> meta);
	
	public IInputOutputInstance getOutputInstanceForOutput(IInputOutput<?> meta);
	public IInputOutputInstance getOutputInstanceForOutput(String inputId);
	
	/**
	 * removes this algo instance (removes for related elements as well, 
	 * including workflow.)
	 */
	public void delete();
	
	public Object getValueForParameter(String id);
	public Object getValueForParameter(Parameter<?> parameter);

	public void setValueForParameter(Parameter<?> parameter, Object value);


	public void setValueForParameter(String name, Object value);
	
	public void addParametersListener(IParametersListener list);
	public void removeParametersListener(IParametersListener list);

	/**
	 * Check this instance would be able to run. 
	 * The WorkflowCheckResult passed as parameter will receive the messages 
	 * from all the instances of a workflow; an error will mean no run.
	 * @param res
	 */
	public void checkForRun(WorkflowCheckResult res);

	public IAlgoContainerInstance getContainer();
	
	public void setContainer(IAlgoContainerInstance container);
	
	public IAlgoInstance cloneInContext(IAlgoContainerInstance container);
	
	public boolean canBeContainedInto(IAlgoContainerInstance container);

	/**
	 * Returns the parameters for this algo instance. Most of the time (but not always), 
	 * it will be the list from the meta algo
	 * @return
	 */
	public Collection<Parameter<?>> getParameters();
	
	public boolean hasParameter(String id);
	public Parameter<?> getParameter(String id);
	public Map<String,Object> getParametersAndValues();
	
	public boolean isContainedInto(IAlgoContainerInstance otherInstance);

	public void _initializeParamChangeName();

	void declareParameter(Parameter<?> p);
	
	public boolean isDisabled();

	public Collection<Object> getPrecomputedValuesForInput(IInputOutput<?> input);

	public  Object getPrecomputedValueForInput(IInputOutput<?> input);

	public Object getPrecomputedValueForOutput(IInputOutput<?> output);

	
}
