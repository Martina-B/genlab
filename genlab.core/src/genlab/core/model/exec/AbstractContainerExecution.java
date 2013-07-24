package genlab.core.model.exec;

import genlab.core.commons.ProgramException;
import genlab.core.exec.IContainerTask;
import genlab.core.exec.IExecution;
import genlab.core.exec.ITask;
import genlab.core.model.instance.IAlgoContainerInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IConnection;
import genlab.core.model.instance.IInputOutputInstance;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractContainerExecution extends AbstractAlgoExecution implements IContainerTask {

	private final IAlgoContainerInstance algoInst;
	
	/**
	 * The connections from me to an algo outside (I'm waiting for its results)
	 */
	private final Map<IConnection,ConnectionExec> connection2OutsideToMe = new HashMap<IConnection, ConnectionExec>();
	/**
	 * The connections from from inside to me (a child is waiting for directives)
	 */
	protected final Map<IConnection,ConnectionExec> connection2MeToInside = new HashMap<IConnection, ConnectionExec>();
	/**
	 * The connections from me to inside (I'm waiting the result of a child)
	 */
	private final Map<IConnection,ConnectionExec> connection2InsideToMe = new HashMap<IConnection, ConnectionExec>();
	/**
	 * The connections from outside to me (a non child is waiting for my result)
	 */
	private final Map<IConnection,ConnectionExec> connection2MeToOutside = new HashMap<IConnection, ConnectionExec>();
	
	private Collection<IAlgoExecution> subExecs = new LinkedList<IAlgoExecution>();
	
	private Collection<ITask> subTasks = new LinkedList<ITask>();
	
	protected Object semaphoreStopWaitingChildren = new Object();
	
	public AbstractContainerExecution(
			IExecution exec, 
			IAlgoContainerInstance algoInst) {
		
		super(exec, algoInst, new ComputationProgressWithSteps());

		this.algoInst = algoInst;
	}

	@Override
	public long getTimeout() {
		// TODO Auto-generated method stub
		return 10000;
	}
	
	@Override
	public void initInputs(Map<IAlgoInstance,IAlgoExecution> instance2exec) {
		
		for (IAlgoInstance child : algoInst.getChildren()) {
			IAlgoExecution e = instance2exec.get(child);
			if (e == null)
				throw new ProgramException("exec should not be null ! my children were not processed, including : "+child);
			subExecs.add(e);
		}
		
		
		// our links are not created here (see declare**)
		
	}
	
	protected boolean allDataFromInsideReceived() {
		
		// beware of this small problem:
		// - all outputs available, so this one resets the internal state of execs...
		// - but, all the listeners were still not 
		
		for (ConnectionExec ex: connection2InsideToMe.values()) {
			if (ex.getValue() == null)
				return false;
			
		}
		
		return true;
	}
	
	protected void checkAllInputsAvailable() {
		
		
	}

	@Override
	public void notifyInputAvailable(IInputOutputInstance to) {
		
		if (progress.getComputationState() == ComputationState.WAITING_DEPENDENCY) {
			// check if all dependencies are satisfied
			for (ConnectionExec cex: connection2OutsideToMe.values()) {
				if (cex.getValue() == null)
					return;
			}
			messages.debugTech("all inputs available, I'm now ready !", getClass());
			progress.setComputationState(ComputationState.READY);
			
		} else {
			// maybe we have all the outputs ?
			synchronized (semaphoreStopWaitingChildren) {
				semaphoreStopWaitingChildren.notifyAll();	
			}
		}
		
	}

	
	protected void waitForInternalFinished() {
		

		messages.traceTech("now waiting for outputs...", getClass());
		while (!allDataFromInsideReceived()) {
			//GLLogger.traceTech("still waiting for outputs...", getClass());
			synchronized (semaphoreStopWaitingChildren) {
				try {
					semaphoreStopWaitingChildren.wait();
				} catch (InterruptedException e) {
					
				}	
			}
			//GLLogger.traceTech("interrupted, let's check the state of internal tasks...", getClass());

		}
		messages.debugTech("all outputs received ! ", getClass());
		
	}
	
	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public void declareInputExpected(ConnectionExec connToMe, IInputOutputInstance input, IConnection c, Map<IAlgoInstance,IAlgoExecution> instance2exec) {
		
		// true if the person who connected to me is my child
		boolean isMyChildren = algoInst.getChildren().contains(c.getTo().getAlgoInstance());
		
		if (isMyChildren) {
			
			// create a connection from the actual input out of this container to me
			ConnectionExec connOutsideToMe = new ConnectionExec(
					c, 
					instance2exec.get(c.getFrom().getAlgoInstance()), 
					this, 
					false
					);
			connection2OutsideToMe.put(c, connOutsideToMe);
			getConnectionsForInput(input).add(connOutsideToMe);

			messages.traceTech("I'm listening for outside algo: "+c.getFrom().getAlgoInstance(), getClass());
				
			// and remember the connection inside so I can warn it
			connection2MeToInside.put(c, connToMe);
			messages.traceTech("My child is listening for me: "+c.getTo().getAlgoInstance(), getClass());

			// also indicate that I depend on this input (else I become a root in the exec tree)
			addPrerequire(connOutsideToMe.from);
			
		} else {
			
			// create a connection from the actual output of this child to me
			ConnectionExec connOutsideToMe = new ConnectionExec(
					c, 
					instance2exec.get(c.getFrom().getAlgoInstance()),
					this,
					false
					);
			connection2InsideToMe.put(c, connOutsideToMe);
			messages.traceTech("I'm listening for my child algo: "+c.getFrom().getAlgoInstance(), getClass());
			
			// and remember the connection inside so I can warn it
			connection2MeToOutside.put(c, connToMe);
			messages.traceTech("An external algo is listening for me: "+c.getTo().getAlgoInstance(), getClass());

		}
		
		// suppress the standard listening behaviour
		this.getProgress().removeListener(connToMe);
		
	}

	@Override
	public void reset() {
		for (ConnectionExec cex : connection2MeToOutside.values()) {
			cex.reset();
		}
		progress.setComputationState(ComputationState.WAITING_DEPENDENCY);
	}

	
	@Override
	public void addTask(ITask t) {
		subTasks.add(t);
	}

	@Override
	public Collection<ITask> getTasks() {
		return subTasks;
	}
	
	protected abstract void initFirstRun();
	
	protected abstract void startOfIteration();
	
	protected abstract boolean shouldContinueRun();
	
	protected abstract void endOfRun();
	
	@Override
	public void run() {

	
		messages.debugTech("should start !", getClass());
		
		initFirstRun();
		
		progress.setComputationState(ComputationState.STARTED);
		
		while (shouldContinueRun()) {
		
			startOfIteration();
			
			// reset
			messages.traceTech("reset of each subtask...", getClass());
			for (IAlgoExecution subEx : subExecs) {
				subEx.reset();
			}
			
			// start tasks
			messages.traceTech("starting tasks by sending them values...", getClass());
			for (IConnection c : connection2MeToInside.keySet()) {
			
				Object value = connection2OutsideToMe.get(c).getValue();
				
				messages.debugTech("transmitting value "+value+" to "+c.getTo().getAlgoInstance(), getClass());
				
				connection2MeToInside.get(c).forceValue(value);
				
			}
			// TODO also start tasks automatically when they are roots ! 
			
			// wait
			waitForInternalFinished();

			endOfRun();
			
			// suggest garbage collecting right now
			Runtime.getRuntime().gc();
		}
		
		messages.debugTech("done iterations; will now transmit results out of this container", getClass());
		for (IConnection c: connection2InsideToMe.keySet()) {
			
			Object value = connection2InsideToMe.get(c).getValue();
			messages.debugTech("transmitting value "+value+" to "+c.getTo().getAlgoInstance(), getClass());
			connection2MeToOutside.get(c).forceValue(value);
		}
		
		progress.setComputationState(ComputationState.FINISHED_OK);

		
	}
	

	

	@Override
	public int getThreadsUsed() {
		return 0;
	}
	


}
