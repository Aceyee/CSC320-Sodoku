/*
 *  CSC 320 project
 *
 *	Yuwei Zhang	V00805647
 *	Zihan Ye	V00793984
 *
 *	reference from Sudoku-as-SAT.pdf on connex
 */
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;


public class CNF{
	public static void main(String[] args)throws IOException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
		}else{
			s = new Scanner(System.in);
		}
		int num=9;
		
		
		int clauses = 0;
		int [][]encode = new int[num][num];
		String filename = "temp.txt";		//temp.txt stores only values without header, need modifying
		FileWriter fw = new FileWriter(filename, false);
		BufferedWriter out = new BufferedWriter(fw);
		String line = s.next();		
		
		/* These for loops will: 
		 * 1. Read value from the input .txt file
		 * 2. detect the illegal values and change those values to 0
		 * 3. Store all the corrected value in a 2D array 
		 */
		for(int i=0;i<num;i++){
			for(int j = 0; j < num; j++) {
				char temp = line.charAt(i*9+j);
				/*Elimate the . ? * symbols if appeared, change them to 0*/
				if(temp=='.'||temp=='?'||temp=='*'||temp=='0'){
					temp='0';
				}
				encode[i][j] =  Character.getNumericValue(temp);
			}
		}
		
		int decimal = 0;
		
		/* These for loops will:  
		 * 1. read all index values in the table
		 * 2. transform those values into base 9
		 * 3. write the transformed data into the .txt file
		 * 4. each value ends with 0
		 */
		for(int i=0;i<num;i++){
			for(int j = 0; j < num; j++) {
				if(encode[i][j]!=0){
					decimal = toDecimal(i+1,j+1, encode[i][j]);
					out.write(String.valueOf(decimal)+ " 0");
					out.newLine();
					clauses++;
					//System.out.println(i+ " " + j+ " "+encode[i][j]+" "+ decimal);
				}
			}
		}
		
		/*  At least one number in each entry:

		    i(1,9) * j(1,9) * k(1,9)	explain: i, j, k domain from 1 to 9 inclusive.  

		 *  These for loops will: 
		 *  1. for 1 <= i,j,k <= 9, generate 3-digit numbers in the format of ijk (or (i,j,k))
		 *  2. transform the base-9 number ijk into a decimal by calling self-deined method toDecimal();
		 *  3. write the value of the new base-9 number into the .txt file
		 *  4. each value ends with 0
		 */
		for(int i=1;i<num+1;i++){
			for(int j = 1; j < num+1; j++) {
				for(int k = 1; k < num+1; k++) {
					//System.out.println(i+ " "+ j+ " "+ k);
					decimal = toDecimal(i, j, k);
					out.write(String.valueOf(decimal)+ " ");
				}
				out.write("0");
				out.newLine();
				clauses++;
			}
		}
		int decimal1 = 0;
		int decimal2 = 0;

		/* Each number appears at most once in each row: 

		   j(1,9) * k(1,9) * i(1,8) * d(i+1, 9)		

		 * These for loops will create a row view for cnf:  
		 * 1. keep j, k going from 1 to 9, and i going from 1 to 8 
		 * 2. transform (i,j,k) and (i+1,j,k) into decimal
		 * 3. write the decimals into .txt file
		 * 4. each line ends with 0
		 */
		for(int j = 1; j < num+1; j++) {
			for(int k = 1; k < num+1; k++) {
				for(int i=1;i<num;i++){
					//System.out.println(i, i+1);
					for(int d=i+1;d<num+1;d++){
						decimal1 = toDecimal(i, j, k);
						decimal2 = toDecimal(d, j, k);
						out.write("-" + String.valueOf(decimal1) + " -" + String.valueOf(decimal2)+ " 0");
						out.newLine();
						clauses++;
					}
				}
			}
		}

		/* Each number appears at most once in each column: 

		   i(1,9) * k(1,9) * j(1,8) * d(j+1, 9)

		 * These for loops will create a column view for cnf:  
		 * 1. keep i, k going from 1 to 9, and j going from 1 to 8 
		 * 2. transform (i,j,k) and (i,j+1,k) into decimal
		 * 3. write the decimals into .txt file
		 * 4. each line ends with 0 
		 */
		for(int i = 1; i < num+1; i++) {			
			//System.out.println(i);
			for(int k = 1; k < num+1; k++) {
				for(int j=1;j<num;j++){
					//System.out.println(j, j+1);
					for(int d=j+1;d<num+1;d++){
						decimal1 = toDecimal(i, j, k);
						decimal2 = toDecimal(i, d, k);
						out.write("-" + String.valueOf(decimal1) + " -" + String.valueOf(decimal2)+ " 0");  
						out.newLine();
						clauses++;
					}
				}
			}
		}		
		/* Each number appears at most once in each 3x3 sub-grid:

		    k(1,9) * Xstart(1,3) * Ystart(1,3) * i(1,4) * j(1,4) * n(j+1, 4)
		    k(1,9) * Xstart(1,3) * Ystart(1,3) * i(1,4) * j(1,4) * n(j+1, 4) * m(1,3)

		 * These for loops will create a 3x3 box view for cnf:  
		 * 1. keep k going from 1 to 9
		 * 2. transform every two base-9 number, which in the same row, into decimal
		 * 3. transform every two base-9 number, which in the coner side, into decimal
		 * 4. write the decimals into .txt file
		 * 5. each line ends with 0
		 */

		for(int k = 1; k < num+1; k++) {
			for(int Xstart = 0; Xstart<3; Xstart++){
				//System.out.println(Xstart);
				for(int Ystart = 0; Ystart<3; Ystart++){
					//System.out.println(Ystart);
					for(int i=1; i<4; i++){
						for(int j=1; j<4; j++){
							for(int n=j+1; n<4; n++){
								decimal1 = toDecimal(Xstart*3+i, Ystart*3+j, k);
								decimal2 = toDecimal(Xstart*3+i, Ystart*3+n, k);
								out.write("-" + String.valueOf(decimal1) + " -" + String.valueOf(decimal2)+ " 0");
								out.newLine();
								clauses++;
							}
							for(int n=i+1; n<4; n++){
								for(int m=1; m<4; m++){
								     decimal1 = toDecimal(Xstart * 3 + i, Ystart * 3 + j, k);
								     decimal2 = toDecimal(Xstart * 3 + n, Ystart * 3 + m, k);
								     out.write("-" + String.valueOf(decimal1) + " -" + String.valueOf(decimal2)+ " 0");
								     out.newLine();
								     clauses++;
								}
							}
						}
					}
				}
			}
		}
		
//		System.out.println(clauses);
		out.close();
		
		filename = "SATinput.txt";
		fw = new FileWriter(filename, false);
		BufferedWriter out2 = new BufferedWriter(fw);

		String fileName = "temp.txt";		//new modify temp.txt, the complete format will be stored in SATinput.txt
		line = "p cnf 729 "+clauses;
		out2.write(line);
		out2.newLine();
		try {
		    FileReader fileReader = new FileReader(fileName);
			
		    BufferedReader bufferedReader = new BufferedReader(fileReader);

		    while((line = bufferedReader.readLine()) != null) {
		    	out2.write(line);
		    	out2.newLine();
		    }   
		    bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
		    System.out.println(
		        "Unable to open file '" + fileName + "'");                
		}catch(IOException ex) {
		    System.out.println(
		        "Error reading file '" + fileName + "'"); 
		}
		out2.close();
		System.out.println("Encoded! The output file name is: "+filename);
	}

	/* a method transform a base9 number into decimal*/	
	public static int toDecimal (int i, int j, int k){	
		return  81*(i-1)+9*(j-1)+(k-1)+1;
	}
}

