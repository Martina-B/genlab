package genlab.algog.internal;

import java.util.Arrays;


public class AnIndividual implements Comparable<AnIndividual> {

	/** Definition of the genome */
	public final AGenome genome;
	/** Values for each gene defined */
	public final Object[] genes;
	/** Fitness for each objective defined */
	public Double[] fitness;
	/** Target values to reach for each objective defined */
	public Object[] targets;
	/** Actual value for each objective defined */
	public Object[] values;
	/** Pareto front ranking */
	public int rank;
	/** Crowded distance */
	public Double crowdDistance;
	
	public Double averageRank;
	public int birthday;
	public Double centerDistance;
	
	public static int lastId = 1;
	public static final Double INF = StrictMath.pow(10, 14);
	public static final Double EPS = StrictMath.pow(10, -14);
	
	public final int id;
	
	public AnIndividual(AGenome genome, Object[] genes) {
		super();
		
		this.id = lastId++;
		
		this.genome = genome;
		this.genes = genes;
		this.fitness = null;
		this.targets = null;
		this.values = null;
		this.rank = Integer.MAX_VALUE;
		this.crowdDistance = -INF;

		this.birthday = 0;
		this.averageRank = 0.5;
		this.centerDistance = 0d;
	}
	
	public AnIndividual(AnIndividual ind) {
		this(ind.genome, ind.genes.clone());
	}

	@Override
	public int compareTo(AnIndividual arg0) {
		
		return crowdDistance.compareTo(arg0.crowdDistance);
	}
	
	/**
	 * if one value isn't computed then this individual is not feasible
	 * @return true if feasible, false else
	 */
	public Boolean isFeasible() {
		
		for( Object value : this.values ) {
			if( value==null )
				return false;
		}
		
		return true;
	}

	/*
	 * 
	 * 
	 * toString
	 * 
	 * 
	 */
	
	public String toMiniString() {
		return averageRank +" "+birthday+" "+genes[0]+" "+genes[1];
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Arrays.toString(genes) + Arrays.toString(values);
//		return "AnIndividual [genome=" + genome + ", averageRank="
//				+ averageRank + ", genes="
//				+ Arrays.toString(genes) + ", fitness="
//				+ Arrays.toString(fitness) + ", targets="
//				+ Arrays.toString(targets) + ", values="
//				+ Arrays.toString(values) + ", rank=" + rank
//				+ ", crowdDistance=" + crowdDistance + ", birthday=" + birthday + ", centerDistance="
//				+ centerDistance + ", id=" + id + "]";
	}

	public String genesToString() {
		return genome.readableValues(this.genes);
	}
	
	public String fitnessToString() {
		return Arrays.toString(this.fitness);
	}
	
	public String targetsToString() {
		return Arrays.toString(this.targets );
	}
	
	public String valuesToString() {
		return Arrays.toString(this.values);
	}
}
