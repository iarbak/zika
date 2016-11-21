package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Female;
import model.Human;
import model.Male;

/**
 * runs the actual simulation
 * 
 * @author aditi 
 * some assumptions: 1. female mosquito only mates once (based on actual mosquito behaviour
 * 2. female mosquito produces one female mosquito at 20th day of adult phase and
 *         4 males at 0, 10, 30 and 40th day. This has been assumed to keep
 *         population stable and gender ratio well distributed as 1:1
 */
public class Main {
	static Scanner sc = new Scanner(System.in);

	static HashMap<Female , Object> f = new HashMap();
	static HashMap<Male, Object> m = new HashMap();
	static HashMap<Human, Object> h = new HashMap();
	static int day = 0; // day number in simulation
	// population of mosquitos:
	static int fr, mr, fw, mw;
	static int totMonths;
	static int month = 0;
	static int pop; // population: number of humans
	static int totpreg=0;	//total pregnant women

	static Random r = new Random();
	
	public static void main(String[] args) {
		init();
		// females that ought to mate
		HashMap<Female, Object> mateablef = new HashMap();
		// males that ought to mate
		HashMap<Male, Object> mateablem = new HashMap();
		for (Female F : f.keySet()) {
			if (F.age == 15)
				mateablef.put(F, null);
		}
		for (Male M : m.keySet()) {
			if (M.age == 15 && M.alive)
				mateablem.put(M, null);
		}
		mate(mateablef, mateablem);
		mateablef.clear();
		mateablem.clear();
		
		while (month < totMonths) {
			if (month != 0)
				wulbach(month);
			// 30 days to the month
			
			for (int day = 0; day < 30; day++) {
				// first all the mosquito spawning
				HashMap<Female, Object> fClone = new HashMap(f);
				for (Female F : fClone.keySet()) {
					switch (F.produceChild()) {
					case 0:
						break;
					case 1:
						m.put(new Male(F.childType(), 0), null);
						break;
					case 2:
						f.put(new Female(F.childType(), 0), null);
					}
				}
				// mating again
				for (Female F : f.keySet()) {
					if (F.age == 15)
						mateablef.put(F, null);
				}
				for (Male M : m.keySet()) {
					if (M.age >= 15)
						mateablem.put(M, null);
				}
				mate(mateablef, mateablem);
				mateablef.clear();
				mateablem.clear();
				// now all our mosquitoes age, we weed out the dead
				for(Female F : f.keySet()) {
					if(!fClone.containsKey(F))
						f.put(F, null);
				}
				
				Iterator<Female> iter = f.keySet().iterator();
				while (iter.hasNext()) {
					Female F = iter.next();
					F.update();
					if (!F.alive)
						iter.remove();
				    }

				Iterator<Male> iter1 = m.keySet().iterator();
				while (iter1.hasNext()) {
					Male M = iter1.next();
					M.update();
					if (!M.alive)
						iter1.remove();
				    }
			
				
				// humans progressing on the path to recovery
				for (Human H : h.keySet()) {
					H.update();
				}
				// mosquitoes bite
				bite(f, h);

				// end of day

				if (day % 5 == 0) {
					print(f, m);
				}
			}
			month++;
		}
		System.out.println("Total number of pregnant women afftected: "+totpreg);
	}

	/**
	 * Simulates mating of input female list with current adult males randomly
	 * we assume in this simulation that a female only mates once
	 */
	static void mate(HashMap<Female, Object> mateableF , HashMap<Male, Object> mateablem) {
		for (Female F : mateableF.keySet()) {
			// generate random index of male to mate with
			
			int index = (mateablem.size() == 0)? 0 : r.nextInt(mateablem.size());
			List<Male> keys = new ArrayList<Male>(mateablem.keySet());
			F.mate(keys.get(index));
		}
	}

	/**
	 * Accepts initial conditions from users, initialises mosquito population
	 * with random ages
	 */
	static void init() {
		// initialising human population
		System.out.println("Enter number of humans:");
		pop = sc.nextInt();
		int infec = (int) ((double) pop * 0.07);
		for (int i = 0; i < pop; i++) {
			boolean infected = i % infec == 0;
			if (i % 30 == 0)	//0.033 is taken as % of humans pregnant
				h.put(new Human(infected, true), null);
			else
				h.put(new Human(infected), null);
		}

		// initialising previously present mosquito population
		System.out.println("Enter orignal regular female population:");
		fr = sc.nextInt();
		System.out.println("Enter original regular male population:");
		mr = sc.nextInt();
		wulbach(0);
		System.out.println("Enter number of months the simulation should run:");
		totMonths = sc.nextInt();
		for (int i = 0; i < fr; i++) {
			int age = r.nextInt(41) + 15;
			if (i % 50 == 0 && i % 100 != 0) // 1% population infected
				f.put(new Female(1, age, true) , null);
			else
				f.put(new Female(1, age), null);
		}
		for (int i = 0; i < mr; i++) {
			int age = r.nextInt(41) + 15;
			m.put(new Male(1, age), null);
		}
	}

	/**
	 * Handles biting event
	 * 
	 * @param h
	 *            human who is bitten
	 * @param f
	 *            mosquito that bites the human
	 */
	static void handleBite(Human h, Female f) {
		f.bite(h);
		if(!h.infected)	{
			h.bitten(f);
			if(h.infected && h.pregnant)
				totpreg++;
		}
	}

	/**
	 * Mosquitoes in f bite humans in h
	 * 
	 * @param f
	 * @param m
	 */
	static void bite(HashMap<Female, Object> f, HashMap<Human, Object> h) {
		if (h.size() > 0) {
			for (Female F : f.keySet()) {
				 // bite rate is 0.15
				if (r.nextDouble() < 0.15) {
					// generate random index of human to bite

					int index = (h.size() == 0)? 0 : r.nextInt(h.size());
					List<Human> keys = new ArrayList<Human>(h.keySet());
				
					handleBite(keys.get(index), F);
				}
			}
		}
	}

	/**
	 * input wolbachians you are releasing i is month number assume all adults
	 * are newly hatched
	 * 
	 * @param i
	 */
	static void wulbach(int j) {
		System.out.println("Enter wolbachian female population released in month " + j + ":");
		fw = sc.nextInt();
		System.out.println("Enter wolbachian male population released in month " + j + ":");
		mw = sc.nextInt();
		for (int i = 0; i < fw; i++) {
			f.put(new Female(0, 15), null);
		}
		for (int i = 0; i < mw; i++)
			m.put(new Male(0, 15), null);
	}

	/**
	 * prints population statistics
	 * 
	 * @param f
	 *            array of females
	 * @param m
	 *            array of males
	 */
	static void print(HashMap<Female, Object> f, HashMap<Male, Object> m) {
		int count = 0;
		for (Female F : f.keySet()) {
			if (F.getType() == 0)
				count++;
		}
		System.out.println("Number of female Wulbachians: " + count);
		System.out.println("Number of female Regular: " + (f.size() - count));
		count = 0;
		for (Male M : m.keySet()) {
			if (M.getType() == 0)
				count++;
		}
		System.out.println("Number of male Wulbachians: " + count);
		System.out.println("Number of male Regular: " + (m.size() - count));

		count = 0;
		int p = 0;
		for (Human H : h.keySet()) {
			if (H.infected) {
				count++;
				if (H.pregnant)
					p++;
			}
		}
		System.out.println("Number of humans infected: " + count);
		System.out.println("Number of them that are pregnant: " + p);
		System.out.println();
	}
}
