package genlab.gui.algos;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.ComputationState;
import genlab.core.model.exec.ConnectionExecFromIterationToReduce;
import genlab.core.model.exec.ConnectionExecFromSupervisorToChild;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.exec.IConnectionExecution;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IConnection;
import genlab.core.model.instance.IInputOutputInstance;
import genlab.gui.views.AbstractViewOpenedByAlgo;
import genlab.gui.views.ConsoleView;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class GraphicalConsoleExec extends AbstractOpenViewAlgoExec {

	
	public GraphicalConsoleExec(
			IExecution exec, 
			IAlgoInstance algoInst) {
		
		super(exec, algoInst, ConsoleView.VIEW_ID);

		
	}
	
	@Override
	protected void displayResultsSync(AbstractViewOpenedByAlgo theView) {
		
		ConsoleView cv = (ConsoleView)theView;
		
		if (cv == null) {
			getResult().getMessages().warnUser("unable to find the info to display", getClass());
			getProgress().setComputationState(ComputationState.FINISHED_FAILURE);
			return;
		}
				
		cv.showBusy(true);

		// just read the values and display them
		Map<IConnection, Object> values = new HashMap<IConnection, Object>();
		for (IConnectionExecution c: getConnectionsForInput(algoInst.getInputInstanceForInput(GraphicalConsoleAlgo.INPUT))) {
		
			// ignore reduced connections that where displayed previously
			if (c instanceof ConnectionExecFromIterationToReduce || c instanceof ConnectionExecFromSupervisorToChild)
				continue;
		
			values.put(c.getConnection(), c.getValue());
			
		}
		
		getProgress().setProgressTotal(values.size()+1);
	
		StringBuffer sb = new StringBuffer();
		for (Entry<IConnection,Object> entry : values.entrySet()) {
			
			IInputOutputInstance input = entry.getKey().getFrom();
			sb.append("result for ");
			sb.append(input.getAlgoInstance().getName());
			sb.append(" / ");
			sb.append(input.getMeta().getName());
			sb.append(": ");
			sb.append(entry.getValue());
			sb.append("\n");
			
			getProgress().incProgressMade();
		}
		
		cv.write(sb.toString());
		
		cv.showBusy(false);

		getProgress().incProgressMade();

		// in fact, we have nothing to do here
		// just set result to finished
		setResult(null);
		getProgress().setComputationState(ComputationState.FINISHED_OK);
		
	}

	@Override
	public long getTimeout() {
		return 1000*5;
	}

	@Override
	protected void loadDataSuccessiveFromInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void displayResultsSyncReduced(AbstractViewOpenedByAlgo theView,
			IAlgoExecution executionRun, IConnectionExecution connectionExec,
			Object value) {
		
		ConsoleView cv = (ConsoleView)theView;
		
		if (cv == null) 
			return;

		cv.showBusy(true);

	
		StringBuffer sb = new StringBuffer();
			
		IInputOutputInstance input = connectionExec.getConnection().getFrom();
		sb.append("result for run ");
		sb.append(executionRun);
		sb.append(",  ");
		sb.append(input.getAlgoInstance().getName());
		sb.append(" / ");
		sb.append(input.getMeta().getName());
		sb.append(": ");
		sb.append(value);
		sb.append("\n");
		
		getProgress().incProgressMade();
	
		cv.write(sb.toString());
		
		cv.showBusy(false);

	}

	

}
