package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Female;
import model.Male;

/**
 * runs the actual simulation
 * 
 * @author aditi 
 * some assumptions: 1. female mosquito only mates once 2. female
 * mosquito produces one female mosquito at 20th day of adult phase and
 * 4 males at 0, 10, 30 and 40th day. This has been assumed to keep
 * population stable
 */
public class Main {
	static Scanner sc = new Scanner(System.in);

	static ArrayList<Female> f = new ArrayList();
	static ArrayList<Male> m = new ArrayList();
	static int day = 0; // day number in simulation
	// population of mosquitos:
	static int fr, mr, fw, mw;
	static int totMonths;
	static int month = 0;

	public static void main(String[] args) {
		init();
		// females that ought to mate
		ArrayList<Female> mateablef = new ArrayList();
		// males that ought to mate
		ArrayList<Male> mateablem = new ArrayList();
		for (Female F : f) {
			if (F.isAdult())
				mateablef.add(F);
		}
		for (Male M : m) {
			if (M.isAdult())
				mateablem.add(M);
		}
		mate(mateablef, mateablem);
		mateablef.clear();
		mateablem.clear();
		while (month < totMonths) {
			if(month!=0)
				wulbach(month);
			// 30 days to the month
			for (int day = 0; day < 30; day++) {
				// first all the mosquito spawning
				ArrayList<Female> Clone = new ArrayList(f);
				for (Female F : Clone) {
					switch (F.produceChild()) {
					case 0:
						break;
					case 1:
						m.add(new Male(F.childType(), 0));
						break;
					case 2:
						f.add(new Female(F.childType(), 0));
					}
				}
				// mating again
				for (Female F : f) {
					if (F.age==15)
						mateablef.add(F);
				}
				for (Male M : m) {
					if (M.age>15)
						mateablem.add(M);
				}
				mate(mateablef, mateablem);
				mateablef.clear();
				mateablem.clear();
				// now all our mosquitoes age, we weed out the dead
				ArrayList<Female> fClone = new ArrayList(f);
				ArrayList<Male> mClone = new ArrayList(m);

				for (Female F : fClone) {
					F.update();
					if (!F.alive)
						f.remove(F);
				}
				for (Male M : mClone) {
					M.update();
					if (!M.alive)
						m.remove(M);
				}
				// end of day

				if (day % 5 == 0) {
					print(f, m);
				}
			}
			month++;
		}
	}

	/**
	 * Simulates mating of input female list with current adult males randomly
	 * we assume in this simulation that a female only mates once
	 */
	static void mate(List<Female> f, List<Male> m) {
		if(m.size()>0){
			for (Female F : f) {
				// generate random index of male to mate with
				int random = (int) (Math.random() * (m.size() - 1));
				F.mate(m.get(random));
			}
		}
	}

	/**
	 * Accepts initial conditions from users,
	 * initialises mosquito population with random ages
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
			f.add(new Female(1, (int) (i / ((double) (40 / fr))) + 15));
		}
		for (int i = 0; i < mr; i++) {
			m.add(new Male(1, (int) (i / ((double) (10 / mr))) + 15));
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
			f.add(new Female(0, 15));
		}
		for (int i = 0; i < mw; i++)
			m.add(new Male(0, 15));
	}

	/**
	 * prints population statistics
	 * 
	 * @param f
	 *            array of females
	 * @param m
	 *            array of males
	 */
	static void print(ArrayList<Female> f, ArrayList<Male> m) {
		int count = 0;
		for (Female F : f) {
			if (F.getType() == 0)
				count++;
		}
		System.out.println("Number of female Wulbachians: " + count);
		System.out.println("Number of female Regular: " + (f.size() - count));
		count = 0;
		for (Male M : m) {
			if (M.getType() == 0)
				count++;
		}
		System.out.println("Number of male Wulbachians: " + count);
		System.out.println("Number of male Regular: " + (m.size() - count));

		System.out.println();
	}
}
