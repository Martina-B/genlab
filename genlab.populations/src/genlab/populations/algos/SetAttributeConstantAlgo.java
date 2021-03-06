package genlab.populations.algos;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.AnythingFlowType;
import genlab.core.model.meta.basics.flowtypes.StringFlowType;
import genlab.core.parameters.DoubleParameter;
import genlab.populations.bo.IPopulation;
import genlab.populations.execs.SetAttributeConstantExec;
import genlab.populations.execs.SetAttributeRandomDoubleExec;
import genlab.populations.flowtypes.PopulationFlowType;

public class SetAttributeConstantAlgo extends BasicAlgo {

	// TODO add parameter for replace or erase !
	
	public static final InputOutput<IPopulation> INPUT_POPULATION = new InputOutput<IPopulation>(
			PopulationFlowType.SINGLETON, 
			"in_pop", 
			"population", 
			"the population to update"
			);

	public static final InputOutput<String> INPUT_TYPENAME = new InputOutput<String>(
			StringFlowType.SINGLETON, 
			"in_agenttype", 
			"agent type", 
			"type of agents to update"
			);

	public static final InputOutput<String> INPUT_ATTRIBUTENAME = new InputOutput<String>(
			StringFlowType.SINGLETON, 
			"in_attributename", 
			"attribute name", 
			"the name of the attribute to generate randomly"
			);
	

	public static final InputOutput<Object> INPUT_VALUE = new InputOutput<Object>(
			AnythingFlowType.SINGLETON, 
			"in_value", 
			"value", 
			"the value to use for this attribute"
			);
	
	public static final InputOutput<IPopulation> OUTPUT_POPULATION = new InputOutput<IPopulation>(
			PopulationFlowType.SINGLETON, 
			"out_pop", 
			"population", 
			"the population filled"
			);
	
	public static final DoubleParameter PARAM_MIN = new DoubleParameter(
			"param_min", 
			"min", 
			"minimum value (included)", 
			0.0
			);
	
	public static final DoubleParameter PARAM_MAX = new DoubleParameter(
			"param_max", 
			"max", 
			"maximum value (included)", 
			1.0
			);
	
	public SetAttributeConstantAlgo() {
		super(
				"constant attribute", 
				"define an attribute from a constant", 
				ExistingAlgoCategories.GENERATORS_POPULATIONS, 
				null, 
				null
				);
		
		inputs.add(INPUT_POPULATION);
		inputs.add(INPUT_TYPENAME);
		inputs.add(INPUT_ATTRIBUTENAME);
		inputs.add(INPUT_VALUE);
		
		outputs.add(OUTPUT_POPULATION);
		
		registerParameter(PARAM_MIN);
		registerParameter(PARAM_MAX);
		
	}


	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		return new SetAttributeConstantExec(execution, algoInstance);
	}

}
