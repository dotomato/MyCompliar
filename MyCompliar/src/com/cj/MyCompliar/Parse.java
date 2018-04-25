package com.cj.MyCompliar;

import java.io.File;

class Parse {
	private int[] slrstack = new int[1000];
	private int[][][] aslr;
	private Lex lex;
	public boolean accepted;
	private Token lookahead;
	private Genexp[] gelist;
	SyntaxStatck sta;
	private int maxrecord=0;

	public Parse(Lex l, SLR arg_slr,Genexp[] arg_gelist,File argfile) {
		lex = l;
		aslr=arg_slr.getSLR();
		gelist=arg_gelist;
		sta=new SyntaxStatck(argfile);
	}
	
	public void startparse(){
	//	lex.readnextline();
		long endtime;
		long starttime;
		starttime=System.nanoTime();
		accepted = false;
		slrstack[0] = 0;
		getin();
		while (!accepted) {
			if(mydebug.debug){
				for (int i = 0; i < sta.sp + 1; i++)
					System.out.print(slrstack[i] + " ");
				System.out.print('\n');
			}
			takeaction();
		}
		endtime=System.nanoTime();
		System.out.printf("Build success! time:%fms, sta.sp:%d\n",(endtime-starttime)/1000000.0,maxrecord);
	}



	private void takeaction() {
		int tai = aslr[slrstack[sta.sp]][lookahead.tag.ordinal()][1];
		switch (aslr[slrstack[sta.sp]][lookahead.tag.ordinal()][0]) {
		case 's':
			sta.sp++;
			slrstack[sta.sp] = tai;
			sta.tokenstatck[sta.sp]=lookahead;
			
			getin();
			break;
		case 'r':
			sta.sp = sta.sp - gelist[tai].genlen + 1;
		/*	if ((sta.sp==1) && (tai==0)){
				accepted=true;
				gelist[tai].syntax(sta);
				break;
			}*/
			gelist[tai].syntax(sta);
			slrstack[sta.sp] = aslr[slrstack[sta.sp - 1]][gelist[tai].Ltokentag.ordinal()][1];
			break;
		case 'a':
			sta.sp = sta.sp - gelist[tai].genlen + 1;
			gelist[tai].syntax(sta);
			accepted = true;
			break;
		case 'e':
			System.out.print("Error in ");
			lex.printnowreadpos();
			System.out.println(" Token:"+lookahead.toString());
			System.exit(1);
			break;
		default:
			break;
		}
		if (sta.sp>maxrecord)
			maxrecord=sta.sp;
	}

	private void getin() {
		lookahead = lex.getnexttoken();
	}
}