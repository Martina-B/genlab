package genlab.gui.genlab2eclipse;

import java.io.File;

import genlab.core.commons.FileUtils;
import genlab.core.model.meta.ExistingAlgos;
import genlab.core.persistence.GenlabPersistence;
import genlab.core.projects.GenlabProject;
import genlab.core.projects.IGenlabProject;
import genlab.core.usermachineinteraction.GLLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

/**
 * Listens for eclipse events related to resources, like opening projects.
 * 
 * TODO change to: when a project is opened, read the project file if available
 * 
 * @see http://www.eclipse.org/articles/Article-Resource-deltas/resource-deltas.html
 * 
 * @author Samuel Thiriot
 *
 */
public class EclipseResourceListener implements IResourceChangeListener {

	public EclipseResourceListener() {
	}

	protected void openProjectFromDirectory(IProject eclipseProject, String directoryRelativePath) {
		
		GLLogger.traceTech("attempting to open a project from directory : "+directoryRelativePath, getClass());
		
		// before loading a project, we need to detect all the possible algo instances provided by plugins
        ExistingAlgos.getExistingAlgos();
        // (if already loaded, it will not load them again, so no useless cost there)
        
		// read the genlab project 
        IGenlabProject genlabProject = GenlabPersistence.getPersistence().readProject(
        		directoryRelativePath
        		);
        
        // ... and associate it with this eclipse project
        GenLab2eclipseUtils.registerEclipseProjectForGenlabProject(
        		eclipseProject, 
        		genlabProject
        		);
        		
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		
		if (
				 event == null 
				 || event.getDelta() == null
				 )
		        return;
		 
		 
		 try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				
				/*
				   public boolean visit(IResourceDelta delta) throws CoreException {
			
					System.err.println("visiting delta: "+delta+" ("+delta.getKind()+")");

			    	final IResource resource = delta.getResource();
			        
			    	// stay there if the filename project is added (that means: project creation)
			    	if (
			    			(
			    					// when created
			    					(delta.getKind() == IResourceDelta.ADDED) 
			    					|| 
			    					// when project loaded during workspace resume
			    					(delta.getKind() == IResourceDelta.CHANGED))
			    			&&
			    			(resource.getType() == IResource.FILE)
			    			&&
			    			(resource.getName().equals(GenlabPersistence.FILENAME_PROJECT))
			    			) {
			    		
			    		GLLogger.debugTech("a project file was added; attempting to load the corresponding project...", getClass());
						   
			    		openProjectFromDirectory(
			    				resource.getProject(),
			    				FileUtils.extractPath(resource.getLocation().toOSString())
			    				);
			    		
			    		return false;
			    	}
			    	
			    	
			    	
			        return true;
			    }
			 });
				 */
			    public boolean visit(IResourceDelta delta) throws CoreException {
			
					System.err.println("visiting delta: "+delta+" ("+delta.getKind()+")");

			    	final IResource resource = delta.getResource();
			        
			    	// TODO detect renaming ?!
			    	// detect removals
			    	if (delta.getKind() == IResourceDelta.REMOVED) {
			    		// if this resource is a specific file, we have to update 
			    		// the genlab files accordingly
			    		if (delta.getResource().getFileExtension().equals(GenlabPersistence.EXTENSION_PROJECT)) {
			    			GLLogger.errorTech("an eclipse project was removed; should delete the corresponding genlab project", getClass()); 
			    		}
			    		else if (delta.getResource().getFileExtension().equals(GenlabPersistence.EXTENSION_WORKFLOW)) {
			    			GLLogger.errorTech("an eclipse workflow was removed; should delete the corresponding genlab workflow", getClass());
			    		}
			    		
			    	}
			    	
			    	// stay there if the filename project is added (that means: project creation)
			    	if (
			    			(
			    					// when created
			    					(delta.getKind() == IResourceDelta.ADDED) 
			    					|| 
			    					// when project loaded during workspace resume
			    					(delta.getKind() == IResourceDelta.CHANGED))
			    			&&
			    			(resource.getType() == IResource.FILE)
			    			&&
			    			(resource.getName().equals(GenlabPersistence.FILENAME_PROJECT))
			    			) {
			    		
			    		GLLogger.debugTech("a project file was added; attempting to load the corresponding project...", getClass());
						   
			    		openProjectFromDirectory(
			    				resource.getProject(),
			    				FileUtils.extractPath(resource.getLocation().toOSString())
			    				);
			    		
			    		return false;
			    	}
			    	
			    	// also stay there if the root changes (typically, restoration of an eclipse project)
			    	if (
			    			(delta.getKind() == IResourceDelta.CHANGED)
			    			&&
			    			(resource.getType() == IResource.ROOT)
			    			) {
			    		GLLogger.debugTech("a workspace root was added; attempting to load the corresponding project...", getClass());

			    		IWorkspace eclipseWorkspace = resource.getWorkspace();
			    		
			            IWorkspaceRoot eclipseRoot = eclipseWorkspace.getRoot(); 
			            IProject[] eclipseProjects = eclipseRoot.getProjects();
			            
			            // for each project...
			            for (IProject currentEclipseProject: eclipseProjects) {
			            
			            	// we only focus on projects having the right nature
			            	if (!currentEclipseProject.hasNature(GenLabWorkflowProjectNature.NATURE_ID)) 
			            		continue;
			            	
			            	
			            	// okay; please load this one
			            	openProjectFromDirectory(
				    				currentEclipseProject,
				    				currentEclipseProject.getLocation().toOSString()
				    				);
				    		
			            	
			            }
			            
			            return false;
			    	}
			    	
			        return true;
			    }
			 });
		} catch (CoreException e) {
			GLLogger.warnTech("catched an exception during an eclipse resource event: "+e.getMessage(), getClass(), e);
		}
		 
	}

}
