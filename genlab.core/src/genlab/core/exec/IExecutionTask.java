package genlab.core.exec;


import java.util.Collection;

/**
 * A task
 * 
 * @author Samuel Thiriot
 *
 */
public interface IExecutionTask extends ITask, Runnable  {

	/**
	 * returns the set of execution tasks that should be ran before this one.
	 * @return
	 * 
	 */
	public Collection<IExecutionTask> getPrerequires();
	
	public void addPrerequire(IExecutionTask task);

	/**
	 * Actually run this task
	 */
	public void run();
	
	/**
	 * Returns true if the execution task is so costless than it would be more costly to create a 
	 * thread to run it than running it directly. This case is rare.
	 * @return
	 */
	public boolean isCostless();
	
	/**
	 * Kills the task. Cancellation should be tried first.
	 */
	public void kill();
	
		
}
