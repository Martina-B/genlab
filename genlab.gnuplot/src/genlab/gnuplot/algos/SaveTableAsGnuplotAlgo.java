package genlab.gnuplot.algos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import genlab.core.GenLab;
import genlab.core.commons.FileUtils;
import genlab.core.commons.ProgramException;
import genlab.core.exec.IExecution;
import genlab.core.model.exec.AbstractAlgoExecutionOneshot;
import genlab.core.model.exec.ComputationProgressWithSteps;
import genlab.core.model.exec.ComputationResult;
import genlab.core.model.exec.ComputationState;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.model.instance.WorkflowCheckResult;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.FileFlowType;
import genlab.core.model.meta.basics.flowtypes.IGenlabTable;
import genlab.core.model.meta.basics.flowtypes.TableFlowType;
import genlab.core.parameters.FileParameter;

/**
 * TODO manage NaN
 * 
 * @author Samuel Thiriot
 *
 */
public class SaveTableAsGnuplotAlgo extends GnuplotAbstractAlgo {

	public static final InputOutput<IGenlabTable> INPUT_TABLE = new InputOutput<IGenlabTable>(
			TableFlowType.SINGLETON, 
			"in_table", 
			"table", 
			"the table to write in a file"
			);
	
	public static final InputOutput<File> OUTPUT_FILE = new InputOutput<File>(
			FileFlowType.SINGLETON, 
			"out_file", 
			"file", 
			"the file in which the table is stored"
	);
	
	
	public static final FileParameter PARAMETER_FILE = new FileParameter(
			"param_file", 
			"file", 
			"the file into which write the result", 
			FileUtils.getHomeDirectoryFile()
			);
	
	public SaveTableAsGnuplotAlgo() {
		super(
				"write table as gnuplot", 
				"writes a table in the gnuplot data format", 
				ExistingAlgoCategories.WRITER_TABLE
				);
		
		inputs.add(INPUT_TABLE);
		outputs.add(OUTPUT_FILE);
		registerParameter(PARAMETER_FILE);
	}
	
	

	@Override
	public IAlgoInstance createInstance(String id, IGenlabWorkflowInstance workflow) {
		return new AlgoInstance(this, workflow, id) {

			@Override
			public void checkForRun(WorkflowCheckResult res) {
				// checks by parent: connected, etc.
				super.checkForRun(res);
				
				// local checks: conformity of parameters
				File file = (File)getValueForParameter(PARAMETER_FILE);
				if (file.isDirectory()) {
					res.messages.errorUser("invalid value for the parameter "+PARAMETER_FILE.getName()+": the path "+file.getPath()+" is a directory while a file is expected", getClass());
				} else if (file.exists()) {
					if (!file.canWrite()) {
						res.messages.errorUser("invalid value for the parameter "+PARAMETER_FILE.getName()+": the file "+file.getPath()+" is not writable", getClass());
					} else {
						res.messages.warnUser("the parameter "+PARAMETER_FILE.getName()+" will lead to the replacement of the file "+file.getPath()+"; its previous content will be lost", getClass());
					}
				}
			}
			
		};
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {

		final File file = (File)algoInstance.getValueForParameter(PARAMETER_FILE);
		
		return new AbstractAlgoExecutionOneshot(execution, algoInstance, new ComputationProgressWithSteps()) {
			
			@Override
			public void cancel() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void run() {
				
				progress.setComputationState(ComputationState.STARTED);
				
				IGenlabTable table = (IGenlabTable)getInputValueForInput(INPUT_TABLE);
				
				if (table == null)
					throw new ProgramException("no data available for computation");
				
				progress.setProgressTotal(table.getRowsCount());
				
				try {
					PrintStream ps = new PrintStream(file);
					
					// add titles
					ps.print("# file generated by Genlab ");
					ps.println(GenLab.getVersionString());
					
					ps.println("#");
					ps.print("# to plot data with gnuplot: plot \"");
					ps.print(file.getName());
					ps.println("\" using 1:2");
					
					ps.println("#");
					ps.print("# ");
					for (String id : table.getColumnsId()) {
						ps.print(id);
						ps.print("\t");
					}
					ps.println();
					
					Object[] row = null;
					for (int i=0; i<table.getRowsCount(); i++) {

						row = table.getRow(i);
						
						for (int j=0; j<row.length; j++) {
							if (j>0)
								ps.print("\t");
							ps.print(row[j]);	
						}
						ps.println();
						
						progress.incProgressMade();
					}
					
					ps.close();
					
					// export the file as a result
					ComputationResult res = new ComputationResult(algoInst, progress, messages);
					res.setResult(OUTPUT_FILE, file);
					setResult(res);
					
					progress.setComputationState(ComputationState.FINISHED_OK);

					
				} catch (RuntimeException e) {
					
					e.printStackTrace();
					messages.errorUser("an error occured during the writing of the table to a file: "+e.getMessage(), getClass(), e);
					progress.setComputationState(ComputationState.FINISHED_FAILURE);
					progress.setException(e);
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					messages.errorUser("unable to create a file named \""+file+"\": "+e.getMessage(), getClass(), e);
					progress.setComputationState(ComputationState.FINISHED_FAILURE);
					progress.setException(e);
					
				}
				
					
				
			}
			
			@Override
			public void kill() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public long getTimeout() {
				// TODO Auto-generated method stub
				return 5000;
			}
		};
	}

}
