package model;

/**
 * This class is a model of the male mosquito
 * @author aditi
 *
 */
public class Male extends Mosquito{
	
	/**
	 * constructor
	 * t= 0 for Wolbachian, 1 for regular
	 * @param t
	 */
	Male(int t)	{
		super(t);
	}
	
	@Override
	void genLifespan()	{
		lifeSpan=10;	//10 days
	}
}
