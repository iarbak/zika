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
	public Male(int t)	{
		super(t);
	}
	public Male(int t, int age)	{
		super(t, age);
	}
	
	public boolean canMate()
	{
		return isAdult();
	}
	@Override
	void genLifespan()	{
		lifeSpan=25;	//10 days as adult, 15 as egg/larva
	}
}
