package genlab.algog.algos.meta;

import genlab.algog.algos.instance.GeneInstance;
import genlab.algog.types.Genome;
import genlab.algog.types.GenomeFlowType;
import genlab.core.commons.NotImplementedException;
import genlab.core.exec.IExecution;
import genlab.core.model.exec.IAlgoExecution;
import genlab.core.model.instance.AlgoInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.model.meta.IAlgoContainer;
import genlab.core.model.meta.InputOutput;
import genlab.core.parameters.DoubleParameter;

/**
 * TODO nice visual in graphiti, and a plugable visual interface
 * 
 * @author Samuel Thiriot
 * 
 *
 */
public abstract class AbstractGeneAlgo extends AbstractGeneticAlgo {

	public static final InputOutput<Genome> INPUT_GENOME = new InputOutput<Genome>(
			GenomeFlowType.SINGLETON, 
			"in_genome", 
			"genome",
			"the genome which manage this gene",
			false
			);
	
	public static final DoubleParameter PARAM_PROBA_MUTATION = new DoubleParameter(
			"param_proba_mutation", 
			"mutation probability", 
			"the probability for each gene to have a mutation", 
			new Double(0.01)
			);
	
	static {
		PARAM_PROBA_MUTATION.setMinValue(0.0);
		PARAM_PROBA_MUTATION.setMaxValue(1.0);
		PARAM_PROBA_MUTATION.setStep(0.001);
	}
	
	public AbstractGeneAlgo(String name, String desc) {
		super(
				name, 
				desc
				);
		
		inputs.add(INPUT_GENOME);
		
		registerParameter(PARAM_PROBA_MUTATION);
		
	}

	@Override
	public IAlgoExecution createExec(IExecution execution,
			AlgoInstance algoInstance) {
		//return null;
		throw new NotImplementedException("gene algorithms are not supposed to be executed");
	}

	@Override
	public boolean canBeContainedInto(IAlgoContainer algoContainer) {
		// genes can only be contained into genetic exploration algos
		return (algoContainer instanceof AbstractGeneticExplorationAlgo);
	}

	@Override
	public IAlgoInstance createInstance(IGenlabWorkflowInstance workflow) {
		return new GeneInstance(this, workflow);
	}

	@Override
	public IAlgoInstance createInstance(String id,
			IGenlabWorkflowInstance workflow) {
		return new GeneInstance(this, workflow, id);
	}
	
	/**
	 * Retusn the output for the value
	 * @return
	 */
	public abstract InputOutput<?> getMainOutput();

	
}
