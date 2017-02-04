import java.util.Scanner;
import java.util.Arrays;
import java.io.*;


public class Result{
	public static void main(String[] args)throws IOException{
		/* Scaner the input from user*/
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
		String line;
		
		/* create a array with size of 9x9x9 */
		int [] data = new int[num*num*num];
		int count = 0;	
		while(s.hasNext()){
			int temp = Integer.parseInt(s.next());
			data[count] = temp;
			//System.out.println(data[count]);
			count++;
		}
		FileWriter fw = new FileWriter("solution.txt", false);
		BufferedWriter out = new BufferedWriter(fw);

		/* These for loops will:
		 * 1. transfer the (j,i,k) into decimal, is convertable
		 * 2. get the value in the array with that decimal index 
		 * 3. write the result for every line 
		 */
		for(int j=0; j<num; j++){
			line = "";
			for(int i=0; i<num; i++){
				for(int k=0; k<num; k++){
					if(data[j*81 + i*9 +k]>=0){
						line = line + (k+1) + " ";
						break;
					}
				}
			}
			out.write(line);
			out.newLine();
			//System.out.println(line);
		}
		out.close();
		System.out.println("Decoded! The result of the puzzle stored in: solution.txt");
	}

	public static int toDecimal (int x, int y, int z){	
		return  81*(x-1)+9*(y-1)+(z-1)+1;
	}
}

