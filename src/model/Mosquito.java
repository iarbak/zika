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
	boolean adult;	//is it capable of reproduction?
	type T;	//store type of mosquito
	/**
	 * Constructor
	 * t is 0 if wolbachian, 1 if not
	 */
	Mosquito(int t)	{
		age=0;
		alive=true;
		adult=false;
		genLifespan();
		setType(t);
	}
	
	/**
	 * constructor that intiialises for a specific age
	 * @param t=mosquito type: 0 for wolbachian, 1 for regular
	 * @param age =age in days
	 */
	Mosquito(int t, int age)	{
		this(t);
		this.age=age;
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
	
	/**
	 * changes variables with progress of a day
	 */
	void update()	{
		age++;
		if(age>=15)
			adult=true;
		if(age>lifeSpan)	{
			alive=false;
			adult=false;
		}
	}
}
