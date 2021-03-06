package genlab.examples.gui.wizards;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import genlab.core.commons.ProgramException;
import genlab.core.usermachineinteraction.GLLogger;
import genlab.examples.gui.creation.ExamplesCreation;
import genlab.gui.Utils;
import genlab.gui.examples.contributors.IGenlabExample;
import genlab.gui.genlab2eclipse.GenLab2eclipseUtils;
import genlab.gui.wizards.SelectProjectWizardPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;

/**
 * Wizard to propose the user to create a new workflow
 * 
 * @author Samuel Thiriot
 *
 */
public class CreateExampleWizard extends Wizard implements IWorkbenchWizard, INewWizard {

	protected CreateExampleWizardPageProject page1 = null;
	protected CreateExampleWizardPageExamples page2 = null;
	
	
	protected IProject newProject = null;
	

	protected Collection<IGenlabExample> listOfSelectedExamples = Collections.EMPTY_LIST;
	
	public CreateExampleWizard() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void addPages() {

		page1 = new CreateExampleWizardPageProject(
				"projet",
				null,
				"examples"
				);
		page1.setTitle("Select project");
		page1.setDescription("Select the project which will host the examples");
		
		
		page2 = new CreateExampleWizardPageExamples(listOfSelectedExamples);
		addPage(page1);
		addPage(page2);
	}
	
	

	@Override
	public boolean performFinish() {

		
		// retrieve or create project
		IProject targetProject = null;
		if (page1.shouldCreateProject()) {
			targetProject = Utils.createEclipseAndGenlabProject(
					page1.getNameOfProjectToCreate(), 
					getContainer(), 
					null,//ResourcesPlugin.getWorkspace().getRoot().getLocationURI(), 
					getShell().getDisplay()
					);
			// TODO manage errors
		} else {
			targetProject = page1.getSelectedProject();
		}
		
		// now we have the project to use
		// lets create the examples to create
		File hostDirectory = new File(targetProject.getLocationURI());
		hostDirectory.mkdirs();
		
		for (IGenlabExample ex: page2.getExamplesToCreate()) {
			try {
				ExamplesCreation.createWorkflow(ex, hostDirectory);
			} catch (RuntimeException e) {
				GLLogger.errorUser("unable to create the example workflow: "+e, getClass());
			}
		}
		
		return true;
		/*
		try {
			GLLogger.debugTech("Will create examples...", getClass());
	
			IProject eclipseProject = Utils.findEclipseProjectInSelection(selection);
			IGenlabProject glProject = GenLab2eclipseUtils.getGenlabProjectForEclipseProject(eclipseProject);
			if (glProject == null)
				GLLogger.warnTech("unable to find glproject, trouble ahead...", getClass());
			
			IGenlabWorkflowInstance workflow = GenlabFactory.createWorkflow(
					glProject, 
					page1.getWorkflowName(), 
					page1.getWorkflowDesc(), 
					Utils.getPathRelativeToProject(eclipseProject, page2.getRelativePath().toString())
					);
			
	
			
			return true;
			
		} catch (RuntimeException e) {
			page2.setErrorMessage(e.getMessage());
			return false;
		}
		*/
	}

	public void init(Collection<IGenlabExample> selectedExamples) {
		this.listOfSelectedExamples = selectedExamples;
	}


	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	

}
