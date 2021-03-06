package genlab.neo4j.algos.writers;

import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.SimpleGraphFlowType;
import genlab.core.model.meta.basics.graphs.IGenlabGraph;
import genlab.neo4j.Activator;

import org.osgi.framework.Bundle;

public class Neo4jGraphWriter extends BasicAlgo {

	public static final InputOutput<IGenlabGraph> PARAM_GRAPH = new InputOutput<IGenlabGraph>(
			SimpleGraphFlowType.SINGLETON, 
			"in_graph", 
			"graph", 
			"the graph to save"
	);
	
	
	public Neo4jGraphWriter() {
		super(
				"neo4j database", 
				"adds or append a graph into a neo4j database",
				ExistingAlgoCategories.WRITER_GRAPH,
				"/icons/neo4j"+IMAGE_PATH_PLACEHOLDER_SIZE+".gif",
				"/icons/neo4jBig.png"
				);
		
		inputs.add(PARAM_GRAPH);

	}
	
	@Override
	public Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}
	
	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		return new Neo4jGraphWriterExecution(execution, algoInstance);
	}

}
