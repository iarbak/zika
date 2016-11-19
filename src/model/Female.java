package model;

/**
 * This class models the female mosquito
 */
public class Female extends Mosquito{

	/**
	 * constructor
	 * t= 0 for Wolbachian, 1 for regular
	 * @param t
	 */
	Female(int t)	{
		super(t);
	}
	
	@Override
	void genLifespan()	{
		lifeSpan=40;	//40 days
	}
}
