package genlab.gui.graphiti.features;

import genlab.core.model.instance.IAlgoContainerInstance;
import genlab.core.model.instance.IAlgoInstance;
import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.model.instance.IInputOutputInstance;
import genlab.core.model.instance.InputInstance;
import genlab.core.model.meta.IAlgoContainer;
import genlab.core.model.meta.IConstantAlgo;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.graphiti.Utils;
import genlab.gui.graphiti.editors.IntuitiveObjectCreation;
import genlab.gui.graphiti.editors.IntuitiveObjectCreation.ProposalObjectCreation;
import genlab.gui.graphiti.genlab2graphiti.WorkflowListener;

import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.util.ILocationInfo;

public class CreateDomainObjectConnectionConnectionFeature extends AbstractCreateConnectionFeature
		implements ICreateConnectionFeature {

	public CreateDomainObjectConnectionConnectionFeature(IFeatureProvider fp) {
		super(fp, "connection", "create a connection");
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		
		// TODO: check for right domain object instance below
		// return getBusinessObjectForPictogramElement(context.getSourcePictogramElement()) instanceof <DomainObject>;

		//GLLogger.traceTech("event can start connection with source "+context.getSourceAnchor(), getClass());
		try {
			IInputOutputInstance from = (IInputOutputInstance)getBusinessObjectForPictogramElement(context.getSourceAnchor());
			
			// if there is not starting point, then don't propose to create
			if (from == null)
				return false;
			
			// if user is attempting to create a connection starting from an input...
			if (from instanceof InputInstance) {
				// accept the creation if the input has no link OR accepts multiple inputs
				return (from.getConnections().isEmpty() || from.getMeta().acceptsMultipleInputs());
			} else {
				// this is an output
				// always accept to create output connections
				return true;
			}
			
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
		
	}

	/**
	 * For a given context, returns the pictogram to take into account to search for a connection.
	 * The easiest solution is if the context directly points to some shape; but it might also 
	 * be a search for an host under the mouse location, or even a fallback to the parent workflow.
	 */
	private PictogramElement getPictogramElementToSearchForContext(ICreateConnectionContext context) {
		
		PictogramElement targetPictogramElement = context.getTargetPictogramElement(); 
		
		if (targetPictogramElement == null) {
			// if no container is provided, maybe we should search for it under the mouse location (as user might not try to connect something, by somewhere !)
			try {
				ILocationInfo info = Graphiti.getPeService().getLocationInfo(getDiagram(), context.getTargetLocation().getX(), context.getTargetLocation().getY());
				targetPictogramElement = info.getShape();
			} catch (RuntimeException e) {
				// ignore it 
				e.printStackTrace();
			}
		}
		
		return targetPictogramElement;
		
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		
		// two cases:
		// - source and dest were both filled: standard creation of a connection
		// - only source provided: source should be used as dest or origin (depending on its type.
		// - TODO only dest provided: propose to display into the console
		
		// quick exit
		// nota: we accept strange cases with source empty. 
		// The idea is to propose dynamically the creation of something
		
		// if no source, we can do nothing
		if (context.getSourceAnchor() == null)
			return false;
		
		
		try {
			/*
			GLLogger.traceTech(
							"event can create connection with source and target "+
							context.getSourceAnchor()+
							" and "+
							context.getTargetAnchor(), 
							getClass()
							);
			*/
	
			// there is always a source (but it may become a dest !)
			Anchor source = context.getSourceAnchor();
			IInputOutputInstance from = (IInputOutputInstance)getBusinessObjectForPictogramElement(source);
			
			Anchor dest = context.getTargetAnchor();
			IInputOutputInstance to = null;
			if (dest != null) {
				to = (IInputOutputInstance)getBusinessObjectForPictogramElement(dest);
			}
			
			boolean fromIsOutput = from.getAlgoInstance().getOutputInstances().contains(from);
			
			if (to != null && to instanceof IInputOutputInstance) {
				// standard case: ensure the user attempts to really connect 
				// outputs to inputs
				
				// correct wrong direction if necceary
				IInputOutputInstance correctedFrom = null;
				IInputOutputInstance correctedTo = null;
				boolean destIsInput = to.getAlgoInstance().getInputInstances().contains(to);

				
				if (fromIsOutput) {
					if (destIsInput) {
						// no correction to do :-)
						correctedFrom = from;
						correctedTo = to;	
					} else {
						// strange demand...
						return false;
					}
				} else {
					if (!destIsInput) {
						// correction !
						correctedFrom = to;
						correctedTo = from;
					} else {
						// strange demand
						return false;
					}
				}
				
				return correctedFrom.acceptsConnectionTo(correctedTo) || correctedTo.acceptsConnectionFrom(correctedFrom);
				
			} else {
				

				// no standard case: dest == null, probably the user wants us to help him.
				// engage in a suggestion process: given the destination, what can we provide as an input ?
				
				// first retrieve the target container
				PictogramElement targetPictogramElement = getPictogramElementToSearchForContext(context);
				

				if (targetPictogramElement == null) {
					// if no container is provided, it means the container is the diagram (graphiti obj) / workflow (in genlab world)
					targetPictogramElement = getFeatureProvider().getPictogramElementForBusinessObject(from.getAlgoInstance().getWorkflow());
				}
				
				/*
				IInputOutputInstance ioi = null;
				try { 
					ioi = (IInputOutputInstance) getBusinessObjectForPictogramElement(targetPictogramElement);
				} catch (ClassCastException e) {
					return false;
				}
				*/
				IAlgoContainerInstance aci = null;
				try {
					aci = (IAlgoContainerInstance) getBusinessObjectForPictogramElement(targetPictogramElement);
				} catch (ClassCastException e) {
					return false;
				}
				
				System.out.println("reco for "+aci+", "+aci.getAlgo());
				
				ProposalObjectCreation proposal = null;
				if (fromIsOutput) {
					proposal = IntuitiveObjectCreation.getAutoInputForOutput(
							(IAlgoContainer)aci.getAlgo(), 
							from.getMeta()
							);
						
				} else {
					proposal = IntuitiveObjectCreation.getAutoOutputForInput(
							(IAlgoContainer)aci.getAlgo(), 
							from.getMeta()
							);
					
				}
				
				// no proposal => can no create
				return (proposal != null); 
				
			}
	
			
			
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public Connection create(final ICreateConnectionContext context) {
		
		GLLogger.traceTech("event create connection with source and target "+context.getSourceAnchor()+" and "+context.getTargetAnchor(), getClass());

		Anchor source = context.getSourceAnchor();
		IInputOutputInstance from = (IInputOutputInstance)getBusinessObjectForPictogramElement(source);
		
		Anchor dest = context.getTargetAnchor();
		IInputOutputInstance to = null;
		if (dest != null)
			to = (IInputOutputInstance)getBusinessObjectForPictogramElement(dest);
		
		IInputOutputInstance correctedFrom = null;
		IInputOutputInstance correctedTo = null;

		boolean fromIsOutput = from.getAlgoInstance().getOutputInstances().contains(from);

		if (dest != null) {
			// standard case
		
			// correct wrong direction if necceary
			boolean destIsInput = to.getAlgoInstance().getInputInstances().contains(to);
			
			if (fromIsOutput) {
				if (destIsInput) {
					// no correction to do :-)
					correctedFrom = from;
					correctedTo = to;	
				} else {
					// strange demand...
					return null;
				}
			} else {
				if (!destIsInput) {
					// correction !
					correctedFrom = to;
					correctedTo = from;
				} else {
					// strange demand...
					return null;
				}
			}
		} else {
			
			GLLogger.debugTech("no dest provided; will search something to add as an input", getClass());
			
			correctedTo = from;
			correctedFrom = null;
			
			IAlgoContainerInstance aci = null;
			try {
				aci = (IAlgoContainerInstance) getBusinessObjectForPictogramElement(getPictogramElementToSearchForContext(context));
			} catch (ClassCastException e) {
				// TODO what ?
			}
			
			// engage in a suggestion process: given the destination, what can we provide as an input ?
			ProposalObjectCreation proposal = null;
			if (fromIsOutput) {
				proposal = IntuitiveObjectCreation.getAutoInputForOutput(
					(IAlgoContainer)aci.getAlgo(), 
					from.getMeta()
					);
			} else {
				proposal = IntuitiveObjectCreation.getAutoOutputForInput(
						(IAlgoContainer)aci.getAlgo(), 
						from.getMeta()
						);
			}
			
			// no proposal => can no create
			if (proposal == null) {
				GLLogger.warnTech("unable to create this connection, no source was automatically found", getClass());
				return null; 
			}
			
			// apply the proposal: create the corresponding algo instance
			{
				GLLogger.debugTech("applying the proposal", getClass());
					
				IGenlabWorkflowInstance workflow = from.getAlgoInstance().getWorkflow();
				
				IAlgoInstance addInstance = proposal.algoToCreate.createInstance(workflow);
				
				WorkflowListener.lastInstance.transmitLastUIParameters(
						addInstance, 
						new WorkflowListener.UIInfos() {{
							x = context.getTargetLocation().getX() - context.getTargetPictogramElement().getGraphicsAlgorithm().getX();
							y = context.getTargetLocation().getY() - context.getTargetPictogramElement().getGraphicsAlgorithm().getY();
							width = 30;
							height = 30;
							containerShape = Utils.getFirstContainer(context.getTargetPictogramElement());
						}}
						);
				
				// add this instance to the workflow, this will create the corresponding graphical representation
				workflow.addAlgoInstance(addInstance);
				
				aci.addChildren(addInstance);
				addInstance.setContainer(aci);
				
				// then create the connection
				if (fromIsOutput) {
					correctedFrom = from;
					correctedTo = addInstance.getInputInstanceForInput(proposal.ioToUse);
				} else {
					correctedFrom = addInstance.getOutputInstanceForOutput(proposal.ioToUse);
				}
				
				// this should create the corresponding graphical representation
				
				// if this is a constant, let's define the default value
				if (correctedTo.getMeta().getDefaultValue() != null && addInstance.getAlgo() instanceof IConstantAlgo) {
					IConstantAlgo c = (IConstantAlgo) addInstance.getAlgo();
					addInstance.setValueForParameter(
							c.getConstantParameter(), 
							correctedTo.getMeta().getDefaultValue()
							);
				}
				
				// TODO lets' name the instance ? 
			}
			
			
		}
		
		genlab.core.model.instance.IConnection genlabConnection = from.getAlgoInstance().getWorkflow().connect(
				correctedFrom, 
				correctedTo
				);
		

		// the corresponding graphiti object will be craeted in reaction to the workflow event !
				
		// TODO name the constant based on the input name ? 
		
		// TODO display a menu if several possibilities
		
		return null;

	}

	
}
