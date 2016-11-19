package controller;

import java.util.ArrayList;
import java.util.Scanner;

import model.Female;
import model.Male;

public class Main {

	ArrayList<Female> f=new ArrayList();
	ArrayList<Male> m=new ArrayList();
	int day=0;	//day number in simulation
	//population of mosquitos:
	static int fr,	mr, fw, mw;

	public static void main(String[] args) {
		init();
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
	}
	
	/**
	 * input wulbachians you are releasing
	 * i is month number
	 * @param i 
	 */
	static void wulbach(int i){
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter wulbachian female population:");
		fw=sc.nextInt();
		System.out.println("Enter wulbachian male population:");
		mw=sc.nextInt();
		sc.close();
	}
}
