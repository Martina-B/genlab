package genlab.core.projects;

import genlab.core.model.instance.IGenlabWorkflowInstance;
import genlab.core.persistence.GenlabPersistence;
import genlab.core.usermachineinteraction.GLLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO later: save
 * 
 * @author Samuel THiriot
 *
 */
public class GenlabProject implements IGenlabProject {

	private transient String baseDirectory;
	private Map<String,Object> key2object = new HashMap<String,Object>();
	
	private Collection<String> workflowPathes = new LinkedList<String>();
	private transient Map<String,IGenlabWorkflowInstance> id2workflow = new HashMap<String,IGenlabWorkflowInstance>();


	protected static transient Map<String,GenlabProject> openedProjects = new HashMap<String, GenlabProject>();
	
	public static void registerOpenedProject(GenlabProject project) {
		synchronized (openedProjects) {
			openedProjects.put(project.getId(), project);
		}
	}
	
	public static GenlabProject getProject(String id) {
		synchronized (openedProjects) {
			return openedProjects.get(id);
		}
	}
	
	
	public GenlabProject(String baseDirectory) {
		this.baseDirectory = baseDirectory;
		
		registerOpenedProject(this);
	}

	@Override
	public String getBaseDirectory() {
		
		return baseDirectory;
	}

	@Override
	public File getFolder() {
		return new File(baseDirectory);
	}

	@Override
	public Object getAttachedObject(String key) {
		return key2object.get(key);
	}
	
	@Override
	public void setAttachedObject(String key, Object o) {
		key2object.put(key, o);
	}

	@Override
	public Collection<IGenlabWorkflowInstance> getWorkflows() {
		LinkedList<IGenlabWorkflowInstance> l = new LinkedList<IGenlabWorkflowInstance>();
		for (String s: workflowPathes) {
			IGenlabWorkflowInstance i = GenlabPersistence.getPersistence().getWorkflowForFilename(this.getBaseDirectory()+s);
			if (i == null)
				GLLogger.warnTech("unable to find a workflow for path "+s, getClass());
			l.add(i);
		}
		
		return l;
	}

	@Override
	public void addWorkflow(IGenlabWorkflowInstance workflow) {
		if (!workflowPathes.contains(workflow.getRelativeFilename())) {
			workflowPathes.add(workflow.getRelativeFilename());
			id2workflow.put(workflow.getId(), workflow);
		}
	}
	
	public IGenlabWorkflowInstance getWorkflowForId(String id) {
		return id2workflow.get(id);
	}

	@Override
	public String getProjectSavingFilename() {
		return baseDirectory+File.separator+GenlabPersistence.FILENAME_PROJECT;
	}
	
	public Collection<String> getWorkflowPathes() {
		if (workflowPathes.isEmpty())
			// quick return
			return Collections.EMPTY_LIST;
		return new LinkedList<String>(workflowPathes);
	}

	public void _setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	private Object readResolve() {
		id2workflow = new HashMap<String,IGenlabWorkflowInstance>();
		return this;
	}

	@Override
	public Map<String, Object> getAttachedObjects() {
		return Collections.unmodifiableMap(key2object);
	}

	@Override
	public String getId() {
		return baseDirectory;
	}

}
