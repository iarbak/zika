package controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import model.Female;
import model.Human;
import model.Male;

/**
 * runs the actual simulation
 * 
 * @author aditi some assumptions: 1. female mosquito only mates once (based on
 *         actual mosquito behaviour 2. female mosquito produces one female
 *         mosquito at 20th day of adult phase and 4 males at 0, 10, 30 and 40th
 *         day. This has been assumed to keep population stable and gender ratio
 *         well distributed as 1:1
 */
public class Main {
	static Scanner sc = new Scanner(System.in);

	static ConcurrentHashMap<Female, Integer> f = new ConcurrentHashMap(10000000);

	static ConcurrentHashMap<Male, Integer> m = new ConcurrentHashMap(10000000);
	static Field table = null;
	
	static{
		try {
		 table = HashMap.class.getDeclaredField("table");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setAccessible(true);
	}

	static HashMap<Human, Integer> h = new HashMap(1000000);
	static int day = 0; // day number in simulation
	// population of mosquitos:
	static int fr, mr, fw, mw;
	static int totMonths;
	static int month = 0;
	static int pop; // population: number of humans
	static int totpreg = 0; // total pregnant women

	static Random r = new Random();

	public static void main(String[] args) {
		init();
		// females that ought to mate
		HashMap<Female, Integer> mateablef = new HashMap(1000);
		// males that ought to mate
		HashMap<Male, Integer> mateablem = new HashMap(1000);
		for (Female F : f.keySet()) {
			if (F.age == 15)
				mateablef.put(F, 0);
		}
		for (Male M : m.keySet()) {
			if (M.age == 15 && M.alive)
				mateablem.put(M, 0);
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

				for (Female F : f.keySet()) {
					switch (F.produceChild()) {
					case 0:
						break;
					case 1:
						m.put(new Male(F.childType(), 0), 0);
						break;
					case 2:
						f.put(new Female(F.childType(), 0), 0);
					}
				}
				// mating again
				for (Female F : f.keySet()) {
					if (F.age == 15)
						mateablef.put(F, 0);
				}
				for (Male M : m.keySet()) {
					if (M.age >= 15)
						mateablem.put(M, 0);
				}
				mate(mateablef, mateablem);
				mateablef.clear();
				mateablem.clear();
				// now all our mosquitoes age, we weed out the dead

				for (Female F : f.keySet()) {
					F.update();
					if (!F.alive)
						f.remove(F);
				}

				for (Male M : m.keySet()) {
					M.update();
					if (!M.alive)
						m.remove(M);
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
		System.out.println("Total number of pregnant women afftected: " + totpreg);
	}

	/**
	 * Simulates mating of input female list with current adult males randomly
	 * we assume in this simulation that a female only mates once
	 */
	static void mate(HashMap<Female, Integer> mateableF, HashMap<Male, Integer> mateablem) {
		for (Female F : mateableF.keySet()) {
			// generate random index of male to mate with
			Male M = (Male) randomKey(mateablem);
			F.mate(M);
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
			if (i % 30 == 0) // 0.033 is taken as % of humans pregnant
				h.put(new Human(infected, true), 0);
			else
				h.put(new Human(infected), 0);
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
				f.put(new Female(1, age, true), 0);
			else
				f.put(new Female(1, age), 0);
		}
		for (int i = 0; i < mr; i++) {
			int age = r.nextInt(41) + 15;
			m.put(new Male(1, age), 0);
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
		if (!h.infected) {
			h.bitten(f);
			if (h.infected && h.pregnant)
				totpreg++;
		}
	}

	/**
	 * Mosquitoes in f bite humans in h
	 * 
	 * @param f2
	 * @param m
	 */
	static void bite(ConcurrentHashMap<Female, Integer> f2, HashMap<Human, Integer> h) {
		if (h.size() > 0) {
			for (Female F : f2.keySet()) {
				// bite rate is 0.15
				if (r.nextDouble() < 0.15) {
					// generate random index of human to bite

					//int index = (h.size() == 0) ? 0 : r.nextInt(h.size());
					//List<Human> keys = new ArrayList<Human>(h.keySet());

					Human H = (Human) randomKey(h);
					handleBite(H, F);
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
//		System.out.println("Enter wolbachian female population released in month " + j + ":");
//		fw = sc.nextInt();
//		System.out.println("Enter wolbachian male population released in month " + j + ":");
//		mw = sc.nextInt();
//		for (int i = 0; i < fw; i++) {
		for(int i = 0; i < 25500; i++)
			f.put(new Female(0, 15), 0);
//		
//		for (int i = 0; i < mw; i++)
//			m.put(new Male(0, 15), 0);
	}

	/**
	 * prints population statistics
	 * 
	 * @param f2
	 *            array of females
	 * @param m2
	 *            array of males
	 */
	static void print(ConcurrentHashMap<Female, Integer> f2, ConcurrentHashMap<Male, Integer> m2) {
		int count = 0;
		for (Female F : f2.keySet()) {
			if (F.getType() == 0)
				count++;
		}
		System.out.println("Number of female Wulbachians: " + count);
		System.out.println("Number of female Regular: " + (f2.size() - count));
		count = 0;
		for (Male M : m2.keySet()) {
			if (M.getType() == 0)
				count++;
		}
		System.out.println("Number of male Wulbachians: " + count);
		System.out.println("Number of male Regular: " + (m2.size() - count));

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

	public static Object randomKey(HashMap map) {
		Entry[] entries = null;
		try {
			entries = (Entry[]) table.get(map);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int start = r.nextInt(entries.length);
		for (int i = 0; i < entries.length; i++) {
			int idx = (start + i) % entries.length;
			Entry entry = entries[idx];
			if (entry != null)
				return entry.getKey();
		}
		return null;
	}

}
