package com.ADS.mst;

import java.util.Random;

public class RandomNumberGenerator {
	
	/* Method: getRandomNumber()
	 * Input: Integer
	 * Output: Integer
	 * Description:	This method generates an integer random number between 0 and 'seek' 
	 */	
	public static int getRandomNumber (int seek) {
		Random rand = new Random();
		return rand.nextInt(seek);
	}
}