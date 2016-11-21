package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Female;
import model.Male;

/**
 * runs the actual simulation
 * 
 * @author aditi some assumptions: 1. female mosquito only mates once 2. female
 *         mosquito produces one female mosquito at 20th day of adult phase and
 *         4 males at 0, 10, 30 and 40th day. This has been assumed to keep
 *         population stable
 */
public class Main {
	static Scanner sc = new Scanner(System.in);

	static HashMap<Female , Object> f = new HashMap();
	static HashMap<Male, Object> m = new HashMap();
	static int day = 0; // day number in simulation
	// population of mosquitos:
	static int fr, mr, fw, mw;
	static int totMonths;
	static int month = 0;

	public static void main(String[] args) {
		init();
		// females that ought to mate
		HashMap<Female, Object> mateablef = new HashMap();
		// males that ought to mate
		HashMap<Male, Object> mateablem = new HashMap();
		for (Female F : f.keySet()) {
			if (F.isAdult())
				mateablef.put(F, null);
		}
		for (Male M : m.keySet()) {
			if (M.canMate())
				mateablem.put(M, null);
		}
		mate(mateablef, mateablem);
		mateablef.clear();
		mateablem.clear();
		while (month < totMonths) {
			// 30 days to the month
			for (int day = 0; day < 30; day++) {
				// first all the mosquito spawning
				for (Female F : f.keySet()) {
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
				// now all our mosquitoes age, we weed out the dead
				HashMap<Female, Object> fClone = new HashMap(f);
				HashMap<Male, Object> mClone = new HashMap(m);
				
				for (Female F : fClone.keySet()) {
					F.update();
					if (!F.alive)
						f.remove(F);
				}
				for (Male M : mClone.keySet()) {
					M.update();
					if (!M.alive)
						m.remove(M);
				}
				// mating again
				for (Female F : f.keySet()) {
					if (F.canMate())
						mateablef.put(F, null);
				}
				for (Male M : m.keySet()) {
					if (M.canMate())
						mateablem.put(M, null);
				}
				mate(mateablef, mateablem);
				mateablef.clear();
				mateablem.clear();
				// end of day

				if (day % 5 == 0) {
					print(f, m);
				}
			}
			month++;
			wulbach(month);
		}
	}

	/**
	 * Simulates mating of input female list with current adult males randomly
	 * we assume in this simulation that a female only mates once
	 */
	static void mate(HashMap<Female, Object> mateableF , HashMap<Male, Object> mateablem) {
		for (Female F : mateableF.keySet()) {
			// generate random index of male to mate with
			Random r = new Random(mateablem.size());
			int index = r.nextInt(mateablem.size());
			List<Male> keys = new ArrayList<Male>(mateablem.keySet());
			F.mate(keys.get(index));
		}
	}

	/**
	 * Accepts initial conditions from users
	 */
	static void init() {
		System.out.println("Enter orignal regular female population:");
		fr = sc.nextInt();
		System.out.println("Enter original regular male population:");
		mr = sc.nextInt();
		wulbach(0);
		System.out.println("Enter number of months the simulation should run:");
		totMonths = sc.nextInt();
		for (int i = 0; i < fr; i++) {
			f.put(new Female(1, (int) (i / ((double) (40 / fr))) + 15), null);
		}
		for (int i = 0; i < mr; i++) {
			m.put(new Male(1, (int) (i / ((double) (10 / mr))) + 15), null);
		}
	}

	/**
	 * input wulbachians you are releasing i is month number assume all adults
	 * are newly hatched
	 * 
	 * @param i
	 */
	static void wulbach(int j) {
		System.out.println("Enter wulbachian female population released in month " + j + ":");
		fw = sc.nextInt();
		System.out.println("Enter wulbachian male population released in month " + j + ":");
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

		System.out.println();
	}
}
