package genlab.populations.algos;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.DoubleFlowType;
import genlab.core.model.meta.basics.flowtypes.IntegerFlowType;
import genlab.core.model.meta.basics.flowtypes.StringFlowType;
import genlab.core.parameters.DoubleParameter;
import genlab.populations.bo.IPopulation;
import genlab.populations.execs.SetAttributeRandomDoubleExec;
import genlab.populations.flowtypes.PopulationFlowType;

public class SetAttributeRandomDoubleAlgo extends BasicAlgo {

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
	

	public static final InputOutput<Double> INPUT_MIN = new InputOutput<Double>(
			DoubleFlowType.SINGLETON, 
			"in_min", 
			"min", 
			"minimum value (inclusive)",
			0.0
			);
	public static final InputOutput<Double> INPUT_MAX = new InputOutput<Double>(
			DoubleFlowType.SINGLETON, 
			"in_max", 
			"max", 
			"maximum value (inclusive)",
			1.0
			);
	
	public static final InputOutput<IPopulation> OUTPUT_POPULATION = new InputOutput<IPopulation>(
			PopulationFlowType.SINGLETON, 
			"out_pop", 
			"population", 
			"the population filled"
			);
	
	public SetAttributeRandomDoubleAlgo() {
		super(
				"rand double attribute", 
				"define an attribute of an agent randomly", 
				ExistingAlgoCategories.GENERATORS_POPULATIONS, 
				null, 
				null
				);
		
		inputs.add(INPUT_POPULATION);
		inputs.add(INPUT_TYPENAME);
		inputs.add(INPUT_ATTRIBUTENAME);
		inputs.add(INPUT_MIN);
		inputs.add(INPUT_MAX);
		
		outputs.add(OUTPUT_POPULATION);
		
	}


	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		return new SetAttributeRandomDoubleExec(execution, algoInstance);
	}

}
