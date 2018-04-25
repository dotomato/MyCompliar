package com.cj.MyCompliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

class Codeline{
	String code;
	Codeline next=null;
}

class Et{
	String op;
	Codeline codeline=null;
	int op2;
	String op_true;
	String op_false;
	
	void addcode(String arg_code){
		if (codeline==null) {
			codeline=new Codeline();
			codeline.code=arg_code;
		} else {
			Codeline cl=codeline;
			while(cl.next!=null){
				cl=cl.next;
			}
			cl.next=new Codeline();
			cl.next.code=arg_code;
		}
	}
	
	void addcodeline(Codeline arg_cl){
		if(codeline==null){
			codeline=arg_cl;
		} else {
			Codeline cl=codeline;
			while(cl.next!=null){
			cl=cl.next;
			}
			cl.next=arg_cl;
		}
	}
	
}

public class SyntaxStatck {
	
	final int MAXET=1000;
	final int MAXMEN=1000;
	final int MAXID=100;
	Et[] et;
	Token[] tokenstatck;
	String[] idstatck;
	String[] arrstatck;
	int arrcount=0;
	int idcount=0;
	int sp;
	float lexval;
	int labelcount=0;
	File outfile;
	
	public SyntaxStatck(File argfile){
		et = new Et[MAXET];
		for(int i=0;i<MAXET;i++){
			et[i]=new Et();
		}
		tokenstatck = new Token[MAXET];
		sp=0;
		idstatck=new String[MAXID];
		arrstatck=new String[MAXID];
		outfile=argfile;
	}
	

	public void newidmen(String name){
		idstatck[idcount]=name;
		if(mydebug.debug){
			System.out.println("% "+name+" M"+String.valueOf(idcount)+" %");
		}
		idcount++;
	}
	
	public void newarraymen(String name){

		idstatck[idcount]=name;
		if(mydebug.debug){
			System.out.println("% "+name+" M"+String.valueOf(idcount)+" %");
		}
		arrstatck[arrcount]=name;
		arrcount++;
		idcount++;
	}
	
	public String getidmen(String name){
		int i;
		String s;
		for(i=0;i<idcount;i++)
			if(name.equals(idstatck[i]))
				break;
		if(i==idcount){
			System.out.println("undefine var \""+name+"\"");
			System.exit(-2);
		}
		s="M"+String.valueOf(i);
		return s;
	}
	
	public String getnewlabel(){
		labelcount++;
		return "L"+String.valueOf(labelcount);
	}

	
	public void delmensta(){
		idcount=0;
		arrcount=0;
	}
	
	public int getmentop(){
		return idcount;
	}
	
	void printcode(Codeline argcl){
		Codeline cl=argcl;
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile, false), "UTF-8"));

			while(cl!=null){
				fw.append(cl.code);
				fw.newLine();
				cl=cl.next;
			}
			
			fw.flush(); // 全部写入缓存中的内容
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
