package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Female;
import model.Male;

/**
 * runs the actual simulation
 * @author aditi
 * some assumptions:
 * 1. female mosquito only mates once
 * 2. female mosquito produces one female mosquito at 20th
 * 	day of adult phase and 4 males at 0, 10, 30 and 40th day.
 * 	This has been assumed to keep population stable
 */
public class Main {

	static ArrayList<Female> f=new ArrayList();
	static ArrayList<Male> m=new ArrayList();
	static int day=0;	//day number in simulation
	//population of mosquitos:
	static int fr,	mr, fw, mw;
	static int totMonths;
	static int month=0;

	public static void main(String[] args) {
		init();
		ArrayList<Female> mateable=new ArrayList<>();
		for (Female F:f)	{
			if(F.isAdult())
				mateable.add(F);
		}
		mate(mateable,m);
		mateable.clear();
		while(month<totMonths)	{
			//30 days to the month
			for(int day=0; day<30; day++)	{
				
			}
		}
	}

	/**
	 * Simulates mating of input female list with
	 * current adult males randomly
	 * we assume in this simulation that a female only mates once
	 */
	static void mate(List<Female> f, List<Male> m)	{
		for (Female F:f)	{
			//generate random index of male to mate with
			int random= (int) (Math.random()*(m.size()-1));
			F.mate(m.get(random));
		}
	}

	/**
	 * Accepts initial conditions from users
	 */
	static void init()	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter orignal regular female population:");
		fr=sc.nextInt();
		System.out.println("Enter original regular male population:");
		mr=sc.nextInt();
		sc.close();
		wulbach(0);
		System.out.println("Enter number of months the simulation should run:");
		totMonths=sc.nextInt();
		for(int i=0; i<fr; i++){
			f.add(new Female(1, (int)(i/((double)(40/fr)))+15));
		}
		for(int i=0; i<mr; i++){
			m.add(new Male(1, (int)(i/((double)(10/mr)))+15));
		}
	}

	/**
	 * input wulbachians you are releasing
	 * i is month number
	 * assume all adults are newly hatched
	 * @param i 
	 */
	static void wulbach(int j){
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter wulbachian female population released in month "+j+":");
		fw=sc.nextInt();
		System.out.println("Enter wulbachian male population released in month "+j+":");
		mw=sc.nextInt();
		sc.close();
		for(int i=0; i<fw; i++){
			f.add(new Female(0, 15));
		}
		for(int i=0; i<mw; i++)
			m.add(new Male(0,15));
	}
}
