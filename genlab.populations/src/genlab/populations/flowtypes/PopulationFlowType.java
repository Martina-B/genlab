package genlab.populations.flowtypes;

import genlab.core.model.meta.basics.flowtypes.AbstractFlowType;
import genlab.populations.bo.IPopulation;

/**
 * TODO define flow type for population
 * 
 * @author Samuel Thiriot
 *
 */
public class PopulationFlowType extends AbstractFlowType<IPopulation> {

	public static PopulationFlowType SINGLETON = new PopulationFlowType();

	private PopulationFlowType() {
		super(
				"genlab.population.yang.flowtypes.population", 
				"population", 
				"a population"
				);
	}

	@Override
	public IPopulation decodeFrom(Object value) {
		return (IPopulation)value;
	}

}
