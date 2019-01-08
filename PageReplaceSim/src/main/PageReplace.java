package main;
/*****************************************************
 * Seth Lambert
 * 5/7/18
 * Operating Systems
 *
 * Program 4
 * 
 * Implements FIFO, LRU, and Optimal algorithms for 
 * 	page references.
 *
 *****************************************************/
import java.util.Scanner;
import java.util.Random;

public class PageReplace {
	
	static int length, pages;
	static int[] pgNums;

public static void main(String[] args){
	
	//Get user inputs
	Scanner scr = new Scanner(System.in);
	System.out.println("Enter number of pages: ");
	pages = scr.nextInt();
	System.out.println("Enter length of reference string: ");
	length = scr.nextInt();
	scr.close();
	
	// Generate the random string of page references
	shuffle();
	
	// run the algorithms
	FIFO();
	LRU();
	Optimal();
	
}//end main

public static void shuffle(){
	int[] shuffle = new int[length];
	Random rand = new Random();
	
	for(int i = 0; i < length; i++){
		int random = rand.nextInt(10);
		shuffle[i] = random;
	}
	
	pgNums = shuffle;
	
	System.out.print("Page reference string: ");
	for(int i = 0; i < length; i++){
		System.out.print(pgNums[i] + " ");
	}System.out.print("\n");
}

public static void FIFO(){
	String[] display = new String[pages+2];
	for(int i = 0; i < display.length; i++)
		display[i] = "";
	int k = 0, faults = 0;// keeps track of oldest (first in) page
	boolean pgFault = false, check = false;
	
	int[][] pgTbl =  new int[pages][length];
	for(int i = 0; i < pages; i++){
		for(int j = 0; j < length; j++){
			pgTbl[i][j] = -1;
		}
	}
	
	for(int i = 0; i < length; i++){// goes through the references
		if(i > 0){// updating page table with current state
			for(int j = 0; j < pages; j++){
				pgTbl[j][i] = pgTbl[j][i-1];
			}
		}
		for(int j = 0; j < pages; j++){// checking for match
			if(pgTbl[j][i] == pgNums[i]){
				check = true;
				break;
			}
		}if(check == false){// checking for empty spaces
			for(int j = 0; j < pages; j++){
				if(pgTbl[j][i] == -1){
						pgTbl[j][i] = pgNums[i];
						pgFault = true;
						check = true;
						faults++;
						break;
				}// end if
			}// end for
		}//end if
		if(check == false){// enter page reference in page k
			pgTbl[k][i] = pgNums[i];
			pgFault = true;
			check = true;
			faults++;
			if(k < pages)
				k++;
			if(k == pages)// resets page pointer to zero
				k = 0;
		}// end else if
		display[0] += (pgNums[i] + " ");
		for(int l = 0; l < pages; l++){// setting output
			if(pgTbl[l][i]==-1)
				display[l+1] += "_ ";
			else
				display[l+1] += (pgTbl[l][i] + " ");
		}
		if(pgFault == true)
			display[pages+1] += "X ";
		if(pgFault == false)
			display[pages+1] += "  ";
		pgFault = false;
		check = false;
	}// end for
	
	System.out.println("\nFIFO: ");
	for(int i = 0; i < display.length; i++){
		System.out.println(display[i]);
	}System.out.println("Page faults: " + faults + "\n");
	
}
public static void LRU(){
	String[] display = new String[pages+2];
	for(int i = 0; i < display.length; i++)
		display[i] = "";
	int faults = 0;
	boolean pgFault = false, check = false;
	
	int[][] pgTbl =  new int[pages][length];
	for(int i = 0; i < pages; i++){
		for(int j = 0; j < length; j++){
			pgTbl[i][j] = -1;
		}
	}
	
	for(int i = 0; i < length; i++){// goes through the references
		if(i > 0){// updating page table with current state
			for(int j = 0; j < pages; j++){
				pgTbl[j][i] = pgTbl[j][i-1];
			}
		}
		for(int j = 0; j < pages; j++){// checking for match
			if(pgTbl[j][i] == pgNums[i]){
				check = true;
				break;
			}
		}if(check == false){// checking for empty spaces
			for(int j = 0; j < pages; j++){
				if(pgTbl[j][i] == -1){
						pgTbl[j][i] = pgNums[i];
						pgFault = true;
						check = true;
						faults++;
						break;
				}// end if
			}// end for
		}//end if
		if(check == false){// find least recently used
			int d = -1, p = 0, c=0;
			for(int j = 0; j < pages; j++){
				for(int k = i-1; k > -1; k--){
					if(pgTbl[j][i-1] == pgNums[k]){
						if(c > d){
							d = c;
							c = -999;
							p = j;
						}break;
					}
					if(c<pages)
						c++;
				}c=0;
			}
			pgTbl[p][i] = pgNums[i];
			pgFault = true;
			check = true;
			faults++;
		}// end else if
		display[0] += (pgNums[i] + " ");
		for(int l = 0; l < pages; l++){// setting output
			if(pgTbl[l][i]==-1)
				display[l+1] += "_ ";
			else
				display[l+1] += (pgTbl[l][i] + " ");
		}
		if(pgFault == true)
			display[pages+1] += "X ";
		if(pgFault == false)
			display[pages+1] += "  ";
		pgFault = false;
		check = false;
	}// end for
	
	System.out.println("\nLRU: ");
	for(int i = 0; i < display.length; i++){
		System.out.println(display[i]);
	}System.out.println("Page faults: " + faults + "\n");
}
public static void Optimal(){
	String[] display = new String[pages+2];
	for(int i = 0; i < display.length; i++)
		display[i] = "";
	int faults = 0, p = -1, max = 0;
	int[] d = new int[pages];
	for(int i = 0; i < pages; i++)
		d[i] = 999;
	boolean pgFault = false, check = false;
	
	int[][] pgTbl =  new int[pages][length];
	for(int i = 0; i < pages; i++){
		for(int j = 0; j < length; j++){
			pgTbl[i][j] = -1;
		}
	}
	
	for(int i = 0; i < length; i++){// goes through the references
		if(i > 0){// updating page table with current state
			for(int j = 0; j < pages; j++){
				pgTbl[j][i] = pgTbl[j][i-1];
			}
		}
		for(int j = 0; j < pages; j++){// checking for match
			if(pgTbl[j][i] == pgNums[i]){
				check = true;
				break;
			}
		}if(check == false){// checking for empty spaces
			for(int j = 0; j < pages; j++){
				if(pgTbl[j][i] == -1){
						pgTbl[j][i] = pgNums[i];
						pgFault = true;
						check = true;
						faults++;
						break;
				}// end if
			}// end for
		}//end if
		if(check == false){// find optimal
			for(int j = 0; j < pages; j++){// find distance to next
				for(int k = i; k < length; k++){
					if(pgTbl[j][i] == pgNums[k])
						d[j] = k;
				}
			}for(int j = 0; j < pages-1; j++){// find max distance
				for(int k = j+1; k < pages; k++){
					if(d[j] > d[k] && d[j] > max){
						max = d[j];
						p = j;
					}if(d[k] > d[j] && d[k] > max){
						max = d[k];
						p = k;
					}
				}
			}			
			pgTbl[p][i] = pgNums[i];
			pgFault = true;
			check = true;
			faults++;
		}// end else if
		display[0] += (pgNums[i] + " ");
		for(int l = 0; l < pages; l++){// setting output
			if(pgTbl[l][i]==-1)
				display[l+1] += "_ ";
			else
				display[l+1] += (pgTbl[l][i] + " ");
		}
		if(pgFault == true)
			display[pages+1] += "X ";
		if(pgFault == false)
			display[pages+1] += "  ";
		pgFault = false;
		check = false;
	}// end for
	
	System.out.println("\nOptimal: ");
	for(int i = 0; i < display.length; i++){
		System.out.println(display[i]);
	}System.out.println("Page faults: " + faults + "\n");
}


}
