package model;

/**
 * This class models the female mosquito
 */
public class Female extends Mosquito {

	boolean fertile = false;
	// depending on mate, can female produce offspring
	type child;
	// if so, what kind of offspring (W or regular)?

	/**
	 * constructor t= 0 for Wolbachian, 1 for regular
	 * 
	 * @param t
	 */
	public Female(int t) {
		super(t);
	}

	public Female(int t, int age) {
		super(t, age);
	}

	/**
	 * method to construct possibly zika infected mosquito
	 * @param t
	 * @param infected
	 */
	public Female(int t, boolean infected)	{
		this(t);
		if(t==1)	{
			this.infected=infected;
		}
	}

	/**
	 * method to construct possibly infected female
	 * @param t	is the type of mosquito
	 * @param age is age of the mosquito
	 * @param infected is whether the mosquito received the virus
	 */
	public Female(int t, int age, boolean infected)	{
		this(t, age);
		if(t==1)	{
			this.infected=infected;
		}
	}
	
	/**
	 * How does biting a human affect the mosquito
	 * @param h
	 */
	public void bite(Human h)	{
		if(h.infected)
			if(T==type.W)
				infected=true;
	}

	@Override
	void genLifespan() {
		lifeSpan = 55; // 40 days adult
	}

	/**
	 * returns child type
	 * 
	 * @return
	 */
	public int childType() {
		return child.ordinal();
	}

	/**
	 * determines offspring type depending on mate m
	 * 
	 * @param m
	 */
	public void mate(Male m) {
		if (this.T == type.R) {
			if (m.T == type.R) {
				fertile = true;
				child = type.R;
			} else {
				fertile = false;
				child = null;
			}
		} else {
			fertile = true;
			child = type.W;
		}
	}

	/**
	 * returns value of fertile
	 */
	public boolean isFertile() {
		return fertile;
	}

	/**
	 * Will the mosquito produce a child today? 0 if no. 1 if male 2 if female
	 * 
	 * @return
	 */
	public int produceChild() {
		if (fertile) {
			if (age == 35) {
				return 2;
			} else if (age == 15 || age == 25 || age == 45 || age == 55)
				return 1;
		}
		return 0;
	}
}
