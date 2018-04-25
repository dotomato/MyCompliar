package com.cj.MyCompliar; 

import java.io.IOException;


public class Acgui {
	public static void main(String[] args) {

		Arrcompliar ac = new Arrcompliar();
		try {
			ac.compliar(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
