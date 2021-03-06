package genlab.gui.examples;

import genlab.core.IGenlabPlugin;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.examples.gui.creation.ExamplesCreation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IGenlabPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "genlab.gui.examples"; //$NON-NLS-1$

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
		GLLogger.infoUser("starting the examples plugin which proposes the creation of examples of workflows", getClass());
	
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


	public static final String getName() {
		return "GenLab / examples";
	}

	public static final String getDescription() {
		return "References the examples provided by plugins and publish them with graphical user interfaces";
	}
	
}
