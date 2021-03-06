package genlab.core.model.exec;

import genlab.core.model.instance.IInputOutputInstance;

/**
 * Tags the algo executions which run in one-shot mode: once initialized, they wait for all their inputs to
 * become available. Once these inputs are available, then the algo execution becomes ready. Once executed, 
 * it is finished.
 * 
 * @author Samuel Thiriot
 *
 */
public interface IAlgoExecutionOneshot extends IAlgoExecution {

	
	/**
	 * Called during execution when an input became available.
	 * In the successive execution scheme, when a oneshot algo
	 * received all the required inputs, it may run and end.
	 * @param to
	 */
	public void notifyInputAvailable(IInputOutputInstance to);
	
}
