package genlab.gui.graphiti;

import genlab.core.IGenlabPlugin;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.gui.graphiti.diagram.GraphitiDiagramTypeProvider;
import genlab.gui.graphiti.genlab2graphiti.EclipseResourceListener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.graphiti.dt.IDiagramType;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IGenlabPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "genlab.graphiti"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		GLLogger.infoTech("initializing the graphiti plugin for genlab...", getClass());
		
		// check the diagram types detected by graphiti 
		boolean foundOurs = false;
		for (IDiagramType t : GraphitiUi.getExtensionManager().getDiagramTypes()) {
			if (t.getId().equals(GraphitiDiagramTypeProvider.GRAPH_TYPE_ID))
				foundOurs = true;
			else {
				GLLogger.tipTech("another type of graphiti diagram was detected by graphiti ("+t.getId()+", "+t.getName()+", "+t.getDescription()+"); it may interfer with ours", getClass());
			}
		}
		if (!foundOurs) {
			GLLogger.warnTech("our type of graphiti diagram was not detected by graphiti, problems ahead :-(", getClass());
		} else {
			GLLogger.debugTech("our type of graphiti diagram was detected by graphiti.", getClass());
			
			String provider = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(GraphitiDiagramTypeProvider.GRAPH_TYPE_ID);
			if (provider.equals(GraphitiDiagramTypeProvider.PROVIDER_ID)) {
				GLLogger.debugTech("our type of graphiti diagram was associated with our provider.", getClass());
			} else {
				GLLogger.warnTech(
							"our type of graphiti diagram ("+
							GraphitiDiagramTypeProvider.GRAPH_TYPE_ID+
							") is NOT associated with our provider ("+
							GraphitiDiagramTypeProvider.PROVIDER_ID+
							"); problems ahead.", 
							getClass()
							);
			}
				
		}
		
		// listen for resource events, notably to save genlab resources when graphiti ones are
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new EclipseResourceListener(),
				IResourceChangeEvent.POST_CHANGE
				);
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	

	public static final String getName() {
		return "GenLab / GUI / Graphiti";
	}

	public static final String getDescription() {
		return "Adds to the graphical user interface a Workflow editor based on the Graphiti engine";
	}
}
