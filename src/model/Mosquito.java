package model;
/**
 * This class models the generic mosquito and must be extended
 * @author aditi
 *
 */
public abstract class Mosquito {

	int age;
	int lifeSpan;
	enum type{W, R};	//wolbachian and regular
	boolean alive;	//is the mosquito alive?
	type T;	//store type of mosquito
	/**
	 * Constructor
	 * t is 0 if wolbachian, 1 if not
	 */
	Mosquito(int t)	{
		age=0;
		alive=true;
		genLifespan();
		setType(t);
	}
	
	/**
	 * sets whether Wolbachian or not. 0 is Wolbachian
	 * 1 is regular.
	 * @param t
	 */
	void setType(int t)	{
		switch(t)	{
		case 0:
			T=type.W;
			break;
		case 1:
			T=type.R;
		}
	}
	
	/**
	 * Generates lifespan based on
	 * mosquito gender
	 * and assigns it to variable lifespan
	 */
	void genLifespan() {};
}
