package model;

/**
 * This class models the female mosquito
 */
public class Female extends Mosquito{

	boolean fertile=false;
	//depending on mate, can female produce offspring
	type child;
	//if so, what kind of offspring (W or regular)?
	
	/**
	 * constructor
	 * t= 0 for Wolbachian, 1 for regular
	 * @param t
	 */
	public Female(int t)	{
		super(t);
	}
	
	public Female(int t, int age)	{
		super(t, age);
	}
	
	/**
	 * Ought this female mate?
	 */
	public boolean canMate()	{
		return (age==15);
	}
	
	@Override
	void genLifespan()	{
		lifeSpan=55;	//40 days adult
	}
	
	/**
	 * returns child type
	 * @return
	 */
	public int childType()	{
		return child.ordinal();
	}
	
	/**
	 * determines offspring type depending on mate m
	 * @param m
	 */
	public void mate(Male m){
		if(this.T==type.R)	{
			if (m.T==type.R){
				fertile=true;
				child=type.R;
			}
			else	{
				fertile=false;
				child=null;
			}
		}
		else	{
			fertile=true;
			child=type.W;
		}
	}
	
	/**
	 * returns value of fertile
	 */
	public boolean isFertile(){
		return fertile;
	}
	
	/**
	 * Will the mosquito produce a child today?
	 * 0 if no.
	 * 1 if male
	 * 2 if female
	 * @return
	 */
	public int produceChild(){
		if(fertile){
			if (age==35){
				return 2;
			}
			else if (age==15 || age==25 || age==45 || age==55)
				return 1;
		}
		return 0;
	}
}
