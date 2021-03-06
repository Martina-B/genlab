package genlab.graphstream.algos.generators;

import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.basics.flowtypes.IntegerInOut;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.LobsterGenerator;

public class LobsterAlgo extends GraphStreamGeneratorAlgo {

	public static final String ALGO_NAME = "Lobster generator (graphstream)";
			
	public static final IntegerInOut INPUT_N = new IntegerInOut(
			"in_N", 
			"N", 
			"number of vertices in the generated graph",
			200,
			0
	);
	
	public static final IntegerInOut INPUT_MAXDISTANCE = new IntegerInOut(
			"in_max_distance", 
			"max distance", 
			"maximum distance to the root path",
			20,
			0
	);
	
	public static final IntegerInOut INPUT_MAXDEGREE = new IntegerInOut(
			"in_max_degree", 
			"max degree", 
			"maximum degree in the graph",
			20,
			0
	);
	
	public LobsterAlgo() {
		super(
				ALGO_NAME,
				"Generate a Lobster tree. Lobster are trees where the distance between any node and a root path is less than 2. In this generator, the max distance can be customized."
				);
		
		inputs.add(INPUT_N);
		inputs.add(INPUT_MAXDISTANCE);
		inputs.add(INPUT_MAXDEGREE);
	}

	@Override
	public BaseGenerator getBaseGeneratorForExec(
			AbstractGraphStreamGeneratorExec exec,
			AlgoInstance algoInstance) {
		
		final Integer distance = (Integer)exec.getInputValueForInput(LobsterAlgo.INPUT_MAXDISTANCE);
		final Integer degree = (Integer)exec.getInputValueForInput(LobsterAlgo.INPUT_MAXDEGREE);
			
		LobsterGenerator gen = new LobsterGenerator(distance, degree);
		
		return gen;
	}

	@Override
	public int getIterationsForExec(AbstractGraphStreamGeneratorExec exec) {
		
		final Integer size = (Integer)exec.getInputValueForInput(LobsterAlgo.INPUT_N);
		
		return size;
	}
		
	


}
