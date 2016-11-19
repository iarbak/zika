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
	
	/**
	 * Constructor
	 */
	Mosquito()	{
		age=0;
		alive=true;
		genLifespan();
	}
	
	/**
	 * Generates lifespan based on
	 * mosquito type
	 * and assigns it to variable lifespan
	 */
	void genLifespan() {};
}
