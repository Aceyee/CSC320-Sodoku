# CSC320-Sodoku
Yuwei Zhang V00805647
Zihan Ye    V00793984

This .zip file includes 2 program classes: 

	CNF.java
	
	Result.java

The description of each method is included in CNF.java and Result.java as comments, please check there if needed.

How to encode (transform a puzzle into a standard cnf format):

1. Compile CNF.java:
		
		
			
	javac CNF.java

	
		
2. Run CNF with the input file *.txt, for example:
	

		
	java CNF test.txt


	
3. The encoded file SATinput.txt is created

Run SAT Solver with SATinput.txt as an input, get the output file from SAT

	SAToutput.txt

( Note:The SAT Solver we are using is Satz (contributed by Chu-Min Li), 
  which can be found at http://www.cs.ubc.ca/~hoos/SATLIB/solvers.html)


How to decode (get the result from SAToutput):

1. Compile Result.java:

	javac Result.java

2. Run Result with the SAToutput.txt as an argument:		

	java Result SAToutput.txt

3. The solution of the puzzle stored in solution.txt

