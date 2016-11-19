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
	
	@Override
	void genLifespan()	{
		lifeSpan=55;	//40 days adult
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
	
}
