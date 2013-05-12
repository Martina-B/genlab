package genlab.basics.javaTypes.graphs;

import java.util.Collection;
import java.util.Map;

public interface IGenlabGraph {

	public String getGraphId();
	
	public long getVerticesCount();
	public long getEdgesCount();
	
	public void declareGraphAttribute(String attributeId, Class type);
	public Collection<String> getDeclaredGraphAttributes();
	public Map<String,Class> getDeclaredGraphAttributesAndTypes();
	public void setGraphAttribute(String attributeId, Object value);
	public Object getGraphAttribute(String attributeId);
	
	public void declareVertexAttribute(String attributeId, Class type);
	public void declareEdgeAttribute(String attributeId, Class type);
	
	public Collection<String> getDeclaredVertexAttributes();
	public Map<String,Class> getDeclaredVertexAttributesAndTypes();

	public Collection<String> getDeclaredEdgeAttributes();
	public Map<String,Class> getDeclaredEdgeAttributesAndTypes();

	public void addVertex(String id);
	
	public void setVertexAttribute(String vertexId, String attributeId, Object value);
	
	
	public Collection<String> getVertices();
	
	public void addEdge(String id, String vertexIdFrom, String vertexIdTo);
	
	public void addEdge(String vertexIdFrom, String vertexIdTo, boolean directed);

	public void addEdge(String id, String vertexIdFrom, String vertexIdTo, boolean directed);
	
	public void setEdgeAttribute(String vertexId, String attributeId, Object value);
	
	
	/**
	 * Returns true if one can create several edges between the 
	 * two same edges
	 * @return
	 */
	public boolean isMultiGraph();
	
	/**
	 * Get the directionnality of the graph (directed, undirected, mixed)
	 * @return
	 */
	public GraphDirectionality getDirectionality();
	
	/**
	 * Returns true if attributes are allowed / declared for nodes
	 * @return
	 */
	public boolean isVertexAttributed();
	
	/**
	 * Returns true if attributes are allowed / declared for nodes
	 * @return
	 */
	public boolean isEdgeAttributed();
	
	public boolean containsEdge(String vertexFrom, String vertexTo);
	
	public boolean containsVertex(String vertexId);
	
	public Collection<String> getNeighboors(String vertexId);
	
	
}
