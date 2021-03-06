package genlab.core.model.exec;

import genlab.core.commons.ProgramException;
import genlab.core.exec.ExecutionTask;
import genlab.core.exec.IExecution;
import genlab.core.exec.ITask;
import genlab.core.model.instance.IAlgoContainerInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IConnection;
import genlab.core.model.instance.IInputOutputInstance;
import genlab.core.model.meta.ExistingAlgos;
import genlab.core.model.meta.IAlgo;
import genlab.core.model.meta.IInputOutput;
import genlab.core.usermachineinteraction.ListOfMessages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Minimal algo execution: able to retrieve the algo, computation progress, publish a result.
 * 
 * 
 * 
 * @author Samuel Thiriot
 *
 * @param 
 */
public abstract class AbstractAlgoExecution extends ExecutionTask implements IAlgoExecution, Externalizable {

	protected IAlgoInstance algoInst;
	protected IComputationProgress progress;
	private IComputationResult result = null;
	
	protected IExecution exec;

	protected ListOfMessages messages;
	

	/**
	 * For each input, associates it with the incoming connections for this input.
	 */
	protected Map<IInputOutputInstance,Collection<IConnectionExecution>> input2connection = null; 

	
	/**
	 * During init, creates the input executable connections
	 * @param algoInst
	 * @param progress
	 */
	public AbstractAlgoExecution(IExecution exec, IAlgoInstance algoInst, IComputationProgress progress) {
		this.exec = exec;
		this.algoInst = algoInst;
		this.progress = progress;
		progress._setAlgoExecution(this);
		
		messages = exec.getListOfMessages();
		
		progress.setComputationState(ComputationState.CREATED);
	}	
	
	public void initInputs(Map<IAlgoInstance,IAlgoExecution> instance2exec) {
		
		this.input2connection = new HashMap<IInputOutputInstance, Collection<IConnectionExecution>>();
		
		for (IInputOutputInstance input: algoInst.getInputInstances()) {
			createInputExecutableConnection(input, instance2exec);
		}
		
		
	}
	
	protected void createInputExecutableConnection(IInputOutputInstance input, Map<IAlgoInstance,IAlgoExecution> instance2exec) {
		
		for (IConnection c : input.getConnections()) {
			createInputExecutableConnection(input, c, instance2exec);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<IConnectionExecution> getConnectionsForInput(IInputOutputInstance input) {
		Collection<IConnectionExecution> conns = input2connection.get(input);
		if (conns == null) 
			return Collections.EMPTY_LIST;
		else 
			return conns;
	}
	
	protected Collection<IConnectionExecution> getOrCreateConnectionsForInput(IInputOutputInstance input) {
		Collection<IConnectionExecution> conns = input2connection.get(input);
		if (conns == null) {
			conns = new LinkedList<IConnectionExecution>();
			input2connection.put(input, conns);
		}
		return conns;
	}
	
	protected IConnectionExecution getExecutableConnectionForConnection(IConnection c) {
		
		for (IConnectionExecution cEx: getConnectionsForInput(c.getTo())) {
			if (cEx.getConnection() == c)
				return cEx;
		}
		
		throw new ProgramException("unable to find an executable connection for connection : "+c);
		
	}

	
	protected IConnectionExecution createInputExecutableConnection(IInputOutputInstance input, IConnection c, Map<IAlgoInstance,IAlgoExecution> instance2exec) {
		
		try {
			final IAlgoExecution fromExec = instance2exec.get(c.getFrom().getAlgoInstance());
			final IAlgoExecution toExec = instance2exec.get(c.getTo().getAlgoInstance());
			
			IConnectionExecution cEx = ExecutableConnectionsFactory.createExecutableConnection(fromExec, toExec, c);
			
			getOrCreateConnectionsForInput(input).add(cEx);
			
			addPrerequire(fromExec);
	
			return cEx;
		
		} catch (RuntimeException e) {
			throw new ProgramException("error while attempting to connect "+input+" with "+c, e);
		}
	}

	@Override
	public final IComputationProgress getProgress() {
		return progress;
	}

	@Override
	public final IComputationResult getResult() {
		return result;
	}
	
	public void setResult(IComputationResult res) {
		this.result = res;
	}

	
	@Override
	public IAlgoInstance getAlgoInstance() {
		return algoInst;
	}

	protected Object getInputValueForInput(IInputOutputInstance input) {
		
		Collection<IConnectionExecution> cs = input2connection.get(input);
		if (cs == null)
			throw new ProgramException("unable to find the executable connection for this input: "+input);
		
		if (cs.isEmpty())
			throw new ProgramException("unable to find the executable connection for this input: "+input);
		
		if (cs.size() > 1)
			throw new ProgramException("there are several connections for this input: "+input);
		
		final IConnectionExecution c = cs.iterator().next(); 
		
		Object value = input.getMeta().decodeFromParameters(c.getValue());
		
		if (value == null)
			throw new ProgramException("unable to retrieve the value for input "+input);
		
		return value;
	}
	
	public Object getInputValueForInput(IInputOutput<?> input) {
		
		return getInputValueForInput(
				getAlgoInstance().getInputInstanceForInput(input)
				);
		
	}

	protected Map<IConnection,Object> getInputValuesForInput(IInputOutputInstance input) {
		
		Collection<IConnectionExecution> cs = input2connection.get(input);
		if (cs == null) {
			if (input.getMeta().isFacultative())
				return Collections.EMPTY_MAP;
			else
				throw new ProgramException("unable to find the executable connection for this input: "+input);
		}
		if (cs.isEmpty()) {
			if (input.getMeta().isFacultative())
				return Collections.EMPTY_MAP;
			else
				throw new ProgramException("unable to find the executable connection for this input: "+input);
		}
		
		Map<IConnection,Object> map = new HashMap<IConnection, Object>();
		for (IConnectionExecution ce : cs) {
			map.put(
					ce.getConnection(), 
					input.getMeta().decodeFromParameters(ce.getValue())
					);
		}
		
		
		return map;
	}
	
	protected Map<IConnection,Object> getInputValuesForInput(IInputOutput<?> input) {

		return getInputValuesForInput(
				getAlgoInstance().getInputInstanceForInput(input)
				);
	}
	
	public IExecution getExecution() {
		return exec;
	}

	
	/**
	 * Returns true if the output is used. Children should take care of not computing things that are not useful (for instance, 
	 * results which are not used !).
	 * @param output
	 * @return
	 */
	public boolean isUsed(IInputOutputInstance output) {
		return !output.getConnections().isEmpty();
	}

	public boolean isUsed(IInputOutput<?> output) {
		return !algoInst.getOutputInstanceForOutput(output).getConnections().isEmpty();
	}


	/**
	 * Returns true if no output is used, that is 
	 * no result is observed. Standard behavior in this case is to 
	 * not compute, because nobody cares about the results; still, 
	 * in this case, the user has to be warned.
	 * @return
	 */
	protected boolean noOutputIsUsed() {
		
		for (IInputOutputInstance output : algoInst.getOutputInstances()) {
			if (isUsed(output))
				return false;
		}
		return true;
	}
	
	@Override
	public String getName() {
		return algoInst.getName();
	}
	

	@Override
	public void reset() {

	}

	@Override
	public int getThreadsUsed() {
		return 1;
	}
	

	@Override
	public void collectEntities(
			Set<IAlgoExecution> execs,
			Set<IConnectionExecution> connections
			) {
		
		// collect this entity
		execs.add(this);
		
		// collect my connection execs
		for (Collection<IConnectionExecution> connexs : input2connection.values()) {
			connections.addAll(connexs);	
		}
		
		
	}

	@Override
	public void clean() {
				
		// check feasibility
		if (progress != null && progress.getComputationState() != null && !progress.getComputationState().isFinished())
			throw new ProgramException("attempting to clean a task which is not finished.");
		
		// super clean
		super.clean();
		
		// clean local data
		if (progress != null)
			progress.clean();
		
		if (result != null)
			result.clean();
		
		messages = null;
		
		// ask the incoming connections to clean themselves
		if (input2connection!=null) {
			for (Collection<IConnectionExecution> cExIns: input2connection.values()) {
				for (IConnectionExecution cExIn: cExIns) {
					cExIn.clean();
				}
			}
			// and forget them
			//input2connection.clear();
			input2connection = null;
		}
	}
	
	@Override
	public boolean containedInto(IAlgoExecution other) {
		

		// if the other is not a container, it can not contain us
		if (!(other.getAlgoInstance() instanceof IAlgoContainerInstance))
			return false;

		return algoInst.isContainedInto((IAlgoContainerInstance)other.getAlgoInstance());
				
	
	}

	public void propagateRank(Integer rank, Set<ITask> visited) {
		super.propagateRank(rank, visited);
		
		progress.propagateRank(this.rank, visited);
	}
	
	@Override
	public boolean isCleanable() {

		return (progress != null) 
				&& (progress.getComputationState() != null)
				&& (progress.getComputationState().isFinished()) 
				&& (System.currentTimeMillis() - progress.getTimestampEnd() > 5000) // TODO cleanest method !
				;
		
	}
	
	protected void dieExecutionWithMessage(String msg) {
		
		// display in the execution log
		messages.errorUser(msg, this.getClass());
		
		// kill execution (will cancel subsequent computations)
		progress.setComputationState(ComputationState.FINISHED_FAILURE);
		
	}
	

	protected void dieExecutionWithMessage(String msg, Throwable e) {
		
		// display in the execution log
		messages.errorUser(msg, this.getClass(), e);
		
		// kill execution (will cancel subsequent computations)
		progress.setComputationState(ComputationState.FINISHED_FAILURE);
		progress.setException(e);
		
	}
	
	/**
	 * For serialization only
	 */
	public AbstractAlgoExecution() {}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		// TODO
		
		// save elements to recreate algoInstance 
		out.writeUTF(algoInst.getAlgo().getId());
		out.writeUTF(algoInst.getId());
		out.writeUTF(algoInst.getName());
		out.writeObject(algoInst.getParametersAndValues());
		
		// others
		out.writeObject(exec);

		{
			Map<String,Collection<IConnectionExecution>> inputId2connection = new HashMap<String, Collection<IConnectionExecution>>(input2connection.size());
			for (IInputOutputInstance i: input2connection.keySet()) {
				inputId2connection.put(i.getMeta().getId(), input2connection.get(i));
			}
			out.writeObject(inputId2connection);
		}

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		
		// recreate algoinstance based on these elements
		{
			// first recreate an instance...
			final String algoId = in.readUTF();
			final IAlgo algo = ExistingAlgos.getExistingAlgos().getAlgoForId(algoId);
			if (algo == null)
				throw new ClassNotFoundException("Unable to find the GenLab algorithm for id "+algoId);
			algoInst = algo.createInstance(in.readUTF(), null);
			
			// then set name
			algoInst.setName(in.readUTF());
			// and parameters
			Map<String,Object> parameterId2value = (Map<String,Object>)in.readObject();
			for (String id: parameterId2value.keySet()) {
				algoInst.setValueForParameter(id, parameterId2value.get(id));
			}
		}
		
		// others
		exec = (IExecution) in.readObject();

		// inits from other elements
		messages = exec.getListOfMessages();
		
		progress = new ComputationProgressWithSteps();
		progress._setAlgoExecution(this);
		progress.setComputationState(ComputationState.CREATED);

		input2connection = new HashMap<IInputOutputInstance, Collection<IConnectionExecution>>();
		{
			Map<String,Collection<IConnectionExecution>> inputId2connection = (Map<String, Collection<IConnectionExecution>>) in.readObject();
			for (String id: inputId2connection.keySet()) {
				input2connection.put(
						getAlgoInstance().getInputInstanceForInput(id), 
						inputId2connection.get(id)
						);
			}
		}
	}
		
}
