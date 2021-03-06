package genlab.igraph.algos.generation;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.basics.flowtypes.IntegerInOut;
import genlab.core.model.meta.basics.flowtypes.ProbabilityInOut;
import genlab.core.parameters.BooleanParameter;
import genlab.core.parameters.IntParameter;

public class WattsStrogatzGeneratorAlgo extends AbstractIGraphGenerator {

	public static final IntegerInOut INPUT_N = new IntegerInOut(
			"in_N", 
			"N", 
			"number of vertices to create",
			200,
			0
			);
	
	public static final IntegerInOut INPUT_NEI = new IntegerInOut(
			"in_NEI", 
			"nei", 
			"size of the neighborhood for each vertex",
			4,
			1
			);
	
	public static final ProbabilityInOut INPUT_P = new ProbabilityInOut(
			"in_p", 
			"p", 
			"rewiring probability",
			0.05
			);
	
	public static final BooleanParameter PARAM_ALLOW_LOOPS = new BooleanParameter(
			"param_allow_loops", 
			"allow loops", 
			"generate loop edges", 
			false
			);
	
	public static final BooleanParameter PARAM_ALLOW_MULTIPLE = new BooleanParameter(
			"param_allow_mulitple", 
			"allow multiple", 
			"accepts multiple edges", 
			false
			);
	
	public static final IntParameter PARAM_DIM = new IntParameter(
			"param_dim", 
			"dimensions", 
			"The size of the lattice along each dimension.", 
			new Integer(1),
			1
			);
	
	
	public WattsStrogatzGeneratorAlgo() {
		super(
				"Watts-Strogatz beta generator (igraph)", 
				"This function generates a graph according to the Watts-Strogatz model of small-world networks. The graph is obtained by creating a circular undirected lattice and then rewire the edges randomly with a constant probability.",
				true
				);
		
		inputs.add(INPUT_N);
		inputs.add(INPUT_P);
		inputs.add(INPUT_NEI);
		
		registerParameter(PARAM_ALLOW_LOOPS);
		registerParameter(PARAM_DIM);
		registerParameter(PARAM_ALLOW_MULTIPLE);
		
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		
		return new WattsStrogatzGeneratorExec(execution, algoInstance);
	}

}
