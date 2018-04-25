package com.cj.MyCompliar;

import java.io.*;
import java.util.ArrayList;

public class Arrcompliar {
	public void compliar(String argfile) throws IOException {
		
		ArrayList<Genexp> agelist=new ArrayList<Genexp>();	
		
		agelist.add(new Genexp(Tt.PROGRAM,Tt.PROCESSES,Tt.EOF){
			@Override
			public void syntax(SyntaxStatck sta) {
				Codeline cl=sta.et[sta.sp].codeline;
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("GOTO main");
				sta.et[sta.sp].addcodeline(cl);
				sta.printcode(sta.et[sta.sp].codeline);
			}
		});

		agelist.add(new Genexp(Tt.PROCESSES,Tt.PROCESS){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});
		
		agelist.add(new Genexp(Tt.PROCESSES,Tt.PROCESSES,Tt.PROCESS){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+1].codeline);
			}
		});
		
		agelist.add(new Genexp(Tt.PROCESS,Tt.TYPE, Tt.ID,Tt.LEFTBRA,Tt.ARGDECLARES,Tt.RIGHTBRA,Tt.DECLARES,Tt.LEFTBIGBRA, Tt.STEMS,Tt.RIGHTBIGBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode(sta.tokenstatck[sta.sp+1].val_s+":");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+5].codeline);
			//	sta.et[sta.sp].addcode("#BACKWRITEARRAY");
				for(int i=0;i<sta.arrcount;i++)
					sta.et[sta.sp].addcode("ARRWR "+sta.getidmen(sta.arrstatck[i]));
				
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+7].codeline);
				sta.et[sta.sp].addcode("PUSH FP");
				sta.et[sta.sp].addcode("PUSH 3");
				sta.et[sta.sp].addcode("ADD");
				sta.et[sta.sp].addcode("POP SP");
				sta.et[sta.sp].addcode("POP FP");
				sta.et[sta.sp].addcode("POP PC");
				sta.delmensta();
			}
		});
		
		agelist.add(new Genexp(Tt.PROCESS,Tt.TYPE, Tt.ID,Tt.LEFTBRA,Tt.ARGDECLARES,Tt.RIGHTBRA,Tt.LEFTBIGBRA, Tt.STEMS,Tt.RIGHTBIGBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode(sta.tokenstatck[sta.sp+1].val_s+":");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+6].codeline);
				sta.et[sta.sp].addcode("PUSH FP");
				sta.et[sta.sp].addcode("PUSH 3");
				sta.et[sta.sp].addcode("ADD");
				sta.et[sta.sp].addcode("POP SP");
				sta.et[sta.sp].addcode("POP FP");
				sta.et[sta.sp].addcode("POP PC");
				sta.delmensta();
			}
		});
		
		agelist.add(new Genexp(Tt.DECLARES,Tt.DECLARES,Tt.DECLARE){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+1].codeline);
			}
		});
		
		agelist.add(new Genexp(Tt.DECLARES,Tt.DECLARE){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});
		
		agelist.add(new Genexp(Tt.DECLARE,Tt.TYPE,Tt.ID,Tt.LEFTQBRA,Tt.EXP,Tt.RIGHTQBRA,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+3].codeline;
				sta.newarraymen(sta.tokenstatck[sta.sp+1].val_s);
			}
		});
		
		
		agelist.add(new Genexp(Tt.DECLARE,Tt.TYPE,Tt.ID,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("PUSH 0");
				sta.newidmen(sta.tokenstatck[sta.sp+1].val_s);
			}
		});
		

		agelist.add(new Genexp(Tt.ARGDECLARES,Tt.ARGDECLARES,Tt.ENDF, Tt.ARGDECLARE){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
			}
		});
		
		agelist.add(new Genexp(Tt.ARGDECLARES, Tt.ARGDECLARE){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});
		
		agelist.add(new Genexp(Tt.ARGDECLARE,Tt.ARGTYPE,Tt.TYPE,Tt.ID){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
		//		sta.et[sta.sp].addcode("ADDSP");
				sta.newidmen(sta.tokenstatck[sta.sp+2].val_s);
			}
		});
		

		agelist.add(new Genexp(Tt.ARGDECLARE,Tt.VOID){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
			}
		});
		
		agelist.add(new Genexp(Tt.BLOCK,Tt.LEFTBIGBRA,Tt.STEMS,Tt.RIGHTBIGBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
			}
		});
		
		agelist.add(new Genexp(Tt.BLOCK,Tt.LEFTBIGBRA,Tt.RIGHTBIGBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
			}
		});
		
		agelist.add(new Genexp(Tt.BLOCK,Tt.STEM){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});

		agelist.add(new Genexp(Tt.STEMS,Tt.STEMS,Tt.STEM){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+1].codeline);
			}
		});
		
		agelist.add(new Genexp(Tt.STEMS,Tt.STEM){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.IF,Tt.BOOLEXP3,Tt.THEN,Tt.BLOCK){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
				if(sta.et[sta.sp+1].op_true!=null){
					sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_true+":");
					sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
					if(sta.et[sta.sp+1].op_false!=null)
						sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_false+":");
				}
		}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.IF,Tt.BOOLEXP3,Tt.THEN,Tt.BLOCK,Tt.ELSE,Tt.BLOCK){
			@Override
			public void syntax(SyntaxStatck sta) {
				String l=sta.getnewlabel();
				boolean b=false;
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
				if(sta.et[sta.sp+1].op_true!=null){
					sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_true+":");
					sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
					sta.et[sta.sp].addcode("GOTO "+l);
					b=true;
				}
				if(sta.et[sta.sp+1].op_false!=null){
					sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_false+":");
					sta.et[sta.sp].addcodeline(sta.et[sta.sp+5].codeline);
				}
				if(b)
					sta.et[sta.sp].addcode(l+":");
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.WHILE,Tt.BOOLEXP3,Tt.DO,Tt.BLOCK){
			@Override
			public void syntax(SyntaxStatck sta) {
				String l=sta.getnewlabel();
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode(l+":");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+1].codeline);
				sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_true+":");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
				sta.et[sta.sp].addcode("GOTO "+l);
				sta.et[sta.sp].addcode(sta.et[sta.sp+1].op_false+":");
		}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.ID,Tt.ASSIGN,Tt.EXP,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+2].codeline;
				sta.et[sta.sp].addcode("POP "+sta.getidmen(sta.tokenstatck[sta.sp].val_s));
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.FAC,Tt.POINTTO,Tt.ASSIGN,Tt.EXP,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+3].codeline);
				sta.et[sta.sp].addcode("MOV");
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.ID,Tt.LEFTQBRA,Tt.EXP,Tt.RIGHTQBRA,Tt.ASSIGN,Tt.EXP,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				String m=sta.getidmen(sta.tokenstatck[sta.sp].val_s);
				int mi=Integer.parseInt(m.substring(1));
				sta.et[sta.sp].addcode("PUSH M"+String.valueOf(mi));
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
		/*		int oi=sta.et[sta.sp+2].op2;
				for(int i=oi;i>1;i--){
					sta.et[sta.sp].addcode("PUSH M"+String.valueOf(mi+i));
					sta.et[sta.sp].addcode("MUL");
					sta.et[sta.sp].addcode("ADD");
				}*/
				sta.et[sta.sp].addcode("ADD");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+5].codeline);
				sta.et[sta.sp].addcode("MOV");
			}
		});
		

		
		agelist.add(new Genexp(Tt.STEM,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.RETURN,Tt.EXP,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
				sta.et[sta.sp].addcode("SETRTN");
			}
		});
		
		agelist.add(new Genexp(Tt.STEM,Tt.FAC,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcode("DECSP");
			}
		});
		

		agelist.add(new Genexp(Tt.STEM,Tt.WRITE,Tt.LEFTBRA,Tt.EXP,Tt.COMMA,Tt.EXP,Tt.RIGHTBRA,Tt.ENDF){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+2].codeline;
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+4].codeline);
				sta.et[sta.sp].addcode("WRITE");
			}
		});
		
		

		agelist.add(new Genexp(Tt.BOOLEXP3,Tt.BOOLEXP2){
			@Override
			public void syntax(SyntaxStatck sta) {
			}	
		});
		
		agelist.add(new Genexp(Tt.BOOLEXP3,Tt.BOOLEXP3,Tt.BOOLOP3,Tt.BOOLEXP2){
			@Override
			public void syntax(SyntaxStatck sta) {
				String st=sta.tokenstatck[sta.sp+1].val_s;
				if(st.equals("AND")){
					//			sta.et[sta.sp].op_false=sta.et[sta.sp].op_false;
					if(sta.et[sta.sp].op_true!=null) {
						sta.et[sta.sp].addcode(sta.et[sta.sp].op_true+":");
						sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
						sta.et[sta.sp].addcode(sta.et[sta.sp+2].op_false+":"
							+" GOTO "+sta.et[sta.sp].op_false);
						sta.et[sta.sp].op_true=sta.et[sta.sp+2].op_true;
					} else {
						sta.et[sta.sp].addcode("GOTO "+sta.et[sta.sp].op_false);
					}
				} else
				if(st.equals("OR")){
		//			sta.et[sta.sp].op_true=sta.et[sta.sp].op_true;
					if(sta.et[sta.sp].op_false!=null) {
						sta.et[sta.sp].addcode(sta.et[sta.sp].op_false+":");
						sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
						sta.et[sta.sp].addcode(sta.et[sta.sp+2].op_true+":"
							+" GOTO "+sta.et[sta.sp].op_true);
						sta.et[sta.sp].op_false=sta.et[sta.sp+2].op_false;
					} else {
						sta.et[sta.sp].addcode("GOTO "+sta.et[sta.sp].op_true);
					}
				}
			}	
		});
		
		
		agelist.add(new Genexp(Tt.BOOLEXP2,Tt.BOOLOP2, Tt.BOOLEXP){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
				sta.et[sta.sp].op_true=sta.et[sta.sp+1].op_false;
				sta.et[sta.sp].op_false=sta.et[sta.sp+1].op_true;
			}
		});
		
		agelist.add(new Genexp(Tt.BOOLEXP2,Tt.BOOLEXP){
			@Override
			public void syntax(SyntaxStatck sta) {
			}	
		});
	
		
		agelist.add(new Genexp(Tt.BOOLEXP,Tt.EXP,Tt.BOOLOP,Tt.EXP){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].op_true=sta.getnewlabel();
				sta.et[sta.sp].op_false=sta.getnewlabel();
				sta.et[sta.sp].op=sta.tokenstatck[sta.sp+1].val_s;
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
				sta.et[sta.sp].addcode("CMP");
				sta.et[sta.sp].addcode(sta.et[sta.sp].op+" "+sta.et[sta.sp].op_false);
				sta.et[sta.sp].addcode("GOTO "+sta.et[sta.sp].op_true);
			}	
		});

		agelist.add(new Genexp(Tt.BOOLEXP,Tt.LEFTBRA,Tt.BOOLEXP3,Tt.RIGHTBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline =sta.et[sta.sp+1].codeline;
				sta.et[sta.sp].op_false =sta.et[sta.sp+1].op_false;
				sta.et[sta.sp].op_true =sta.et[sta.sp+1].op_true;
			}	
		});
		
		agelist.add(new Genexp(Tt.BOOLEXP,Tt.TRUE){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].op_true=sta.getnewlabel();
				sta.et[sta.sp].op_false=null;
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("GOTO "+sta.et[sta.sp].op_true);
			}	
		});
		
		agelist.add(new Genexp(Tt.BOOLEXP,Tt.FALSE){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].op_false=sta.getnewlabel();
				sta.et[sta.sp].op_true=null;
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("GOTO "+sta.et[sta.sp].op_false);
			}	
		});
		
		agelist.add(new Genexp(Tt.EXP,Tt.EXP,Tt.ADDOP,Tt.TEM){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
				sta.et[sta.sp].addcode(sta.tokenstatck[sta.sp+1].toString());
			}
		});
		
		agelist.add(new Genexp(Tt.EXP,Tt.TEM){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});
		
		agelist.add(new Genexp(Tt.TEM,Tt.TEM,Tt.MULOP,Tt.FAC){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
				sta.et[sta.sp].addcode(sta.tokenstatck[sta.sp+1].toString());
			}
		});
		
		agelist.add(new Genexp(Tt.TEM,Tt.FAC){
			@Override
			public void syntax(SyntaxStatck sta) {
			}
		});

		agelist.add(new Genexp(Tt.FAC,Tt.LEFTBRA,Tt.EXP,Tt.RIGHTBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
			}
		});
		
		agelist.add(new Genexp(Tt.FAC,Tt.INT){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("PUSH "+sta.tokenstatck[sta.sp].toString());
			}
		});
		
		agelist.add(new Genexp(Tt.FAC,Tt.ADDOP,Tt.EXP){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				if(sta.tokenstatck[sta.sp].val_s.equals("ADD"))
					sta.et[sta.sp].codeline=sta.et[sta.sp+1].codeline;
				else {
					sta.et[sta.sp].codeline=null;
					sta.et[sta.sp].addcode("PUSH 0");
					sta.et[sta.sp].addcodeline(sta.et[sta.sp+1].codeline);
					sta.et[sta.sp].addcode("DEC");
				}
			}
		});
		
		agelist.add(new Genexp(Tt.FAC,Tt.ID){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("PUSH "+sta.getidmen(sta.tokenstatck[sta.sp].val_s));
			}
		});
		
		agelist.add(new Genexp(Tt.FAC,Tt.ID,Tt.LEFTQBRA,Tt.EXP,Tt.RIGHTQBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				String m=sta.getidmen(sta.tokenstatck[sta.sp].val_s);
				int mi=Integer.parseInt(m.substring(1));
				sta.et[sta.sp].addcode("PUSH M"+String.valueOf(mi));
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
		/*		int oi=sta.et[sta.sp+2].op2;
				for(int i=oi;i>1;i--){
					sta.et[sta.sp].addcode("PUSH M"+String.valueOf(mi+i));
					sta.et[sta.sp].addcode("MUL");
					sta.et[sta.sp].addcode("ADD");
				}*/
				sta.et[sta.sp].addcode("ADD");
				sta.et[sta.sp].addcode("TABLE");
			}
		});
		
		
		agelist.add(new Genexp(Tt.FAC, Tt.ID,Tt.LEFTBRA, Tt.ARGS,Tt.RIGHTBRA){
			@Override
			public void syntax(SyntaxStatck sta) {				
				String l;
				l=sta.getnewlabel();
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("PUSH 0");
				sta.et[sta.sp].addcode("PUSH "+l);
				sta.et[sta.sp].addcode("PUSH FP");
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
				sta.et[sta.sp].addcode("PUSH SP");
				sta.et[sta.sp].addcode("PUSH "+String.valueOf(sta.et[sta.sp+2].op2+3));
				sta.et[sta.sp].addcode("DEC");
				sta.et[sta.sp].addcode("POP FP");
				sta.et[sta.sp].addcode("GOTO "+sta.tokenstatck[sta.sp].val_s);
				sta.et[sta.sp].addcode(l+":");
			}
		});
		
		agelist.add(new Genexp(Tt.FAC, Tt.ID,Tt.LEFTBRA,Tt.RIGHTBRA){
			@Override
			public void syntax(SyntaxStatck sta) {				
				String l;
				l=sta.getnewlabel();
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("PUSH 0");
				sta.et[sta.sp].addcode("PUSH "+l);
				sta.et[sta.sp].addcode("PUSH FP");
				sta.et[sta.sp].addcode("PUSH SP");
				sta.et[sta.sp].addcode("PUSH 3");
				sta.et[sta.sp].addcode("DEC");
				sta.et[sta.sp].addcode("POP FP");
				sta.et[sta.sp].addcode("GOTO "+sta.tokenstatck[sta.sp].val_s);
				sta.et[sta.sp].addcode(l+":");
			}
		});
			
		agelist.add(new Genexp(Tt.FAC,Tt.READ,Tt.LEFTBRA,Tt.RIGHTBRA){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("READ");
			}
		});
		
		agelist.add(new Genexp(Tt.FAC, Tt.LEA,Tt.ID){
			@Override
			public void syntax(SyntaxStatck sta) {	
				sta.et[sta.sp].codeline=null;
				sta.et[sta.sp].addcode("LEA "+sta.getidmen(sta.tokenstatck[sta.sp+1].val_s));
			}
		});		
		
		agelist.add(new Genexp(Tt.FAC, Tt.FAC,Tt.POINTTO){
			@Override
			public void syntax(SyntaxStatck sta) {	
				sta.et[sta.sp].addcode("TABLE");
			}
		});
		
		agelist.add(new Genexp(Tt.ARGS,Tt.EXP){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].op2=1;
			}
		});
		
		agelist.add(new Genexp(Tt.ARGS,Tt.ARGS,Tt.COMMA,Tt.EXP){
			@Override
			public void syntax(SyntaxStatck sta) {
				sta.et[sta.sp].addcodeline(sta.et[sta.sp+2].codeline);
				sta.et[sta.sp].op2++;
			}
		});
		
		Genexp[] gelist = new Genexp[agelist.size()];
		agelist.toArray(gelist);
		
		SLR slr=new SLR();
		SLRGenerator sg=new SLRGenerator();
		slr.setSLR(sg.genera(0, gelist));
		sg=null;
		InputStream fin=new FileInputStream(argfile);
		Lex lex=new Lex(fin);
		String argname = argfile.split("\\.")[0];
		File miofile = new File(argname+".mio");
		Parse paser = new Parse(lex,slr,gelist,miofile);
		paser.startparse();
		File mirfile = new File(argname+".mir");
		File miffile = new File(argname+".mif");
		Mimake mimake=new Mimake(miofile,mirfile,miffile);
		mimake.make();
	}
}
