package com.cj.MyCompliar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Mimake {
	private String[] sa=new String[1000];
	private String[] pss;
	private int psscount;
	//							1     2       3    4    5     6   7   8  9  10  11 12  13  14  15  16 17   18     19     20   21  22  23     24   25    26
	private final String pssp="PUSH,PUSH_R,PUSH_M,POP,POP_M,GOTO,CMP,JG,JGE,JL,JLE,JE,JNE,ADD,DEC,DIV,MUL,SETRTN,ARRWR,TABLE,MOV,LEA,ADDSP,DECSP,WRITE,READ";
	private String[] menpss;
	private int mencount;
	private final String menpssp="SP,FP,PC";
	private String[][] sc;
	private int[][] si;
	private int sacount=0;
	private String lsta[]=new String[100];
	private int lstai[]=new int[100];
	private int lcount=0;
	private int mentype;
	File mirfile,miffile;
	public Mimake(File miofile,File argmirfile,File argmiffile ){
		String st;
		mirfile=argmirfile;
		miffile=argmiffile;
		BufferedReader fw = null;
		try {
			fw = new BufferedReader(new InputStreamReader (new FileInputStream(miofile), "UTF-8"));
			st=fw.readLine();
			while(st!=null){
				sa[sacount]=st;
				sacount++;
				st=fw.readLine();
			}
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
		pss=pssp.split(",");
		psscount=pss.length;
		menpss=menpssp.split(",");
		mencount=menpss.length;
	}
	
	private int setlabel(String l,int line){
		int i;
		String lt;
		lt=l.substring(0, l.length()-1);
		for(i=0;i<lcount;i++)
			if(lsta[i].equals(lt))
				break;
		if(i==lcount){
			lsta[i]=lt;
			lstai[i]=line;
			lcount++;
		}
		return i;
	}
	
	private int findlabel(String l){
		int i;
		for(i=0;i<lcount;i++)
			if(lsta[i].equals(l))
				break;
		if(i==lcount){
			return -1;
		} else {
			return lstai[i];
		}
	}

	public void make(){
		sc=new String[sacount][2];
		si=new int[sacount][2];
		int j;
		for(int i=0;i<sacount;i++){
			sc[i]=sa[i].split(" |,");
			for(j=0;j<psscount;j++)
				if(sc[i][0].equals(pss[j])) {
					si[i][0]=j+1;
					break;
				}
			if(j==psscount){
				setlabel(sc[i][0],i);
				si[i][0]=0;
			}
			if(sc[i].length>=2) {
				si[i][1]=setmen(sc[i][1]);
				switch(si[i][0]){
				case 1:si[i][0]+=mentype; break;
				case 4:si[i][0]+=mentype-1; break;
				}
			}
		}
		for(int i=0;i<sacount;i++){
			for(int k=0;k<sc[i].length;k++){
				int it=findlabel(sc[i][k]);
				if(it!=-1)
					si[i][k]=it;
			}
		}
		System.out.print("");
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mirfile, false), "UTF-8"));
			for(int i=0;i<sacount;i++){
				for (int k=0;k<2;k++)
					fw.append(String.valueOf(si[i][k])+' ');
				if(i!=sacount-1)
					fw.newLine();
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
		
		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(miffile, false), "UTF-8"));
			fw.append("-- ArrCompliar\n");
			fw.append("-- Quartus II generated Memory Initialization File (.mif)\n");
			fw.newLine();
			fw.append("WIDTH=16;\n");
			fw.append("DEPTH=256;\n");
			fw.newLine();
			fw.append("ADDRESS_RADIX=UNS;\n");
			fw.append("DATA_RADIX=UNS;\n");
			fw.newLine();
			fw.append("CONTENT BEGIN\n");
			for(int i=0;i<sacount;i++){
				fw.append(String.valueOf(i)+"  :  "+String.valueOf((si[i][0]<<8)+si[i][1])+";\n");
			}
			fw.append("["+String.valueOf(sacount)+"..255]  :  0;\n");
			fw.append("END;\n");
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
	
	private int setmen(String arg){
		int i;
		for(i=0;i<mencount;i++)
			if(menpss[i].equals(arg))
				break;
		if(i!=mencount){      //寄存器寻址
			mentype=1;
			return i;
		}
		if(arg.charAt(0)=='M'){  //200  M寻址   FP+3+MX
			mentype=2;
			return Integer.parseInt(arg.substring(1));
		}
		int j=0;
		mentype=0;
		try{              //000  立即数
			j=Integer.parseInt(arg);
		} catch(NumberFormatException e){
			
		} 
		return j;
	}
}
