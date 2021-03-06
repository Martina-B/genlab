package genlab.netlogo.algos;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.IntegerInOut;
import genlab.core.model.meta.basics.flowtypes.ProbabilityInOut;
import genlab.core.model.meta.basics.flowtypes.SimpleGraphFlowType;
import genlab.core.model.meta.basics.graphs.IGenlabGraph;
import genlab.core.parameters.BooleanParameter;
import genlab.core.parameters.IntParameter;
import genlab.netlogo.exec.SIRModelExec;

public class SIRModelAlgo extends BasicAlgo {

	public static final InputOutput<IGenlabGraph> INPUT_GRAPH = new InputOutput<IGenlabGraph>(
			SimpleGraphFlowType.SINGLETON, 
			"in_graph", 
			"graph", 
			"the graph to simulate over"
	);
	

	public static final IntegerInOut INPUT_OUTBREAK = new IntegerInOut(
			"in_outbreak", 
			"initial outbreak", 
			"number of infectious nodes",
			2,
			0
			);
	
	public static final ProbabilityInOut INPUT_SPREAD_CHANCE = new ProbabilityInOut(
			"in_spread", 
			"spread probability", 
			"spreading probability",
			1.0
			);
	
	public static final ProbabilityInOut INPUT_RECOVER_CHANCE = new ProbabilityInOut(
			"in_recover", 
			"recover probability", 
			"recovering probability",
			0.5
			);
	
	public static final ProbabilityInOut INPUT_RESISTANCE_CHANCE = new ProbabilityInOut(
			"in_resistance", 
			"resistance probability", 
			"probability of gaining resistance",
			1.0
			);
	
	public static final IntegerInOut OUTPUT_SUSCEPTIBLE = new IntegerInOut(
			"out_susceptible", 
			"susceptible", 
			"number of susceptible at the end"
			);

	public static final IntegerInOut OUTPUT_INFECTED = new IntegerInOut(
			"out_infected", 
			"infected", 
			"number of infected at the end"
			);
	

	public static final IntegerInOut OUTPUT_RESISTANT = new IntegerInOut(
			"out_resistant", 
			"resistant", 
			"number of resistant at the end"
			);
	
	public static final IntegerInOut OUTPUT_DURATION = new IntegerInOut(
			"out_duration", 
			"duration", 
			"duration of epidemics in steps"
			);
	
	public static final IntParameter PARAM_MAX_STEPS = new IntParameter(
			"param_maxiteration", 
			"max steps", 
			"maximum duration of the simulation in steps", 
			1000,
			1
			);
	
	public static final BooleanParameter PARAM_GUI = new BooleanParameter(
			"param_gui", 
			"see GUI", 
			"see the graphical user interface", 
			Boolean.FALSE
			);
	
	public SIRModelAlgo() {
		super(
				"SIR over network (Netlogo)", 
				"netlogo simulation of SIR over a network", 
				ExistingAlgoCategories.MODELS, 
				null, 
				null
				);
		
		inputs.add(INPUT_GRAPH);
		inputs.add(INPUT_OUTBREAK);
		inputs.add(INPUT_SPREAD_CHANCE);
		inputs.add(INPUT_RECOVER_CHANCE);
		inputs.add(INPUT_RESISTANCE_CHANCE);
		
		outputs.add(OUTPUT_SUSCEPTIBLE);
		outputs.add(OUTPUT_INFECTED);
		outputs.add(OUTPUT_RESISTANT);
		outputs.add(OUTPUT_DURATION);
		
		registerParameter(PARAM_MAX_STEPS);
		registerParameter(PARAM_GUI);
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		return new SIRModelExec(execution, algoInstance);
	}

}
