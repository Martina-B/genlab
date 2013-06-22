package genlab.core.model.meta.basics.algos;

import genlab.core.model.meta.InputOutput;
import genlab.core.model.meta.basics.flowtypes.DoubleFlowType;
import genlab.core.parameters.DoubleParameter;
import genlab.core.parameters.IntParameter;

public class ConstantValueDouble extends ConstantValue<Double> {

	public static final InputOutput<Double> OUTPUT = new InputOutput<Double>(
			new DoubleFlowType(), 
			"constantvalue.double.out", 
			"constant output", 
			"an output which is constant"
			);
	
	public ConstantValueDouble() {
		super(new DoubleFlowType(), OUTPUT, "constant double", "a constant double value");
		
		registerParameter(new DoubleParameter(paramId, "value", "the value of this constant", 0.0));

	}


}
