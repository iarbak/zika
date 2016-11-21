package model;

public class Human {

	public boolean infected;
	public boolean pregnant;
	int daySince;	//day since the human was bitten

	/**
	 * default constructor
	 */
	public Human(boolean infect)	{
		infected=infect;
		pregnant=false;
		daySince=15;
	}

	/**
	 * parametrized constructor:
	 * p is whether this human is pregnant
	 */
	public Human(boolean infect, boolean p)	{
		this(infect);
		pregnant=p;
	}

	/**
	 * What happens to the human if it is bitten
	 * @param m is the mosquito that bites
	 */
	public void bitten(Mosquito m){
		if(m.infected && !infected)	{
			double random = (Math.random());	//transmission chance is half
			if (random < 0.5)	{
				infected=true;
				daySince=0;
			}
		}
	}

	public void update()	{
		if(daySince<Integer.MAX_VALUE)
			daySince++;
		else
			daySince=15;
		if(daySince>14)
			infected=false;
	}
}
