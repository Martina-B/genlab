package genlab.gephi.algos.measure;

import genlab.core.model.meta.BasicAlgo;
import genlab.core.model.meta.ExistingAlgoCategories;
import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.SimpleGraphFlowType;
import genlab.core.model.meta.basics.graphs.IGenlabGraph;
import genlab.gephi.Activator;

import org.osgi.framework.Bundle;

public abstract class GephiAbstractAlgo extends BasicAlgo {

	public static final InputOutput<IGenlabGraph> INPUT_GRAPH = new InputOutput<IGenlabGraph>(
			SimpleGraphFlowType.SINGLETON, 
			"in_graph", 
			"graph", 
			"the graph to analyze"
	);
	
	public GephiAbstractAlgo(String name, String description, String longHtmlDescription) {
		super(
				name, 
				description, 
				longHtmlDescription,
				ExistingAlgoCategories.ANALYSIS_GRAPH.getTotalId(),
				"/icons/gephi.gif" 
				);
		
		inputs.add(INPUT_GRAPH);
	}

	@Override
	public Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}
	
}
