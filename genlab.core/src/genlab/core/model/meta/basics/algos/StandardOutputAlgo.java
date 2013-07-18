package genlab.core.model.meta.basics.algos;

import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.AbstractAlgoExecution;
import genlab.core.model.exec.ComputationProgressWithSteps;
import genlab.core.model.exec.ComputationState;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.instance.IConnection;
import genlab.core.model.instance.IInputOutputInstance;
import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.IDisplayAlgo;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.AnythingFlowType;
import genlab.core.model.meta.basics.flowtypes.DoubleFlowType;

/**
 * This algo writes any input in the standard output.
 * 
 * @author Samuel Thiriot
 */
public class StandardOutputAlgo extends BasicAlgo implements IDisplayAlgo {

	public static final InputOutput<Object> INPUT = new InputOutput<Object>(
			AnythingFlowType.SINGLETON, 
			"anything", 
			"any data to display", 
			"any data to be displayed into the console",
			true
			);
	
	public StandardOutputAlgo() {
		this(
				"standard console", 
				"displays all the inputs in the console"
				);
	}

	public StandardOutputAlgo(String name, String desc) {
		super(
				name, 
				desc, 
				null, 
				ExistingAlgoCategories.DISPLAY.getTotalId(),
				null
				);

		inputs.add(INPUT);
	}
	
	protected void initOutput() {
		System.out.flush();
	}

	protected void writeResult(IInputOutputInstance input, Object value) {
		System.out.print("result for ");
		System.out.print(input.getAlgoInstance().getName());
		System.out.print(" / ");
		System.out.print(input.getMeta().getName());
		System.out.print(": ");
		System.out.println(value);
	}
	
	protected void endOutput() {
		System.out.flush();
	}
	
	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		
		return new AbstractAlgoExecution(
				execution, 
				algoInstance, 
				new ComputationProgressWithSteps()
				) {
			
			@Override
			public void run() {
			
				getProgress().setComputationState(ComputationState.STARTED);
				
				// just read the values and display them
				Map<IConnection, Object> values = getInputValuesForInput(INPUT);
				
				getProgress().setProgressTotal(values.size());
				
				initOutput();
				for (Entry<IConnection,Object> entry : values.entrySet()) {
					writeResult(entry.getKey().getFrom(), entry.getValue());
					getProgress().incProgressMade();
				}
				endOutput();
				
				// in fact, we have nothing to do here
				// just set result to finished
				setResult(null);
				getProgress().setComputationState(ComputationState.FINISHED_OK);
				
			}
			
			@Override
			public void cancel() {
				// TODO Auto-generated method stub
				
			}
			
		
			@Override
			public void kill() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	@Override
	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

}
