package com.cj.MyCompliar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Mif_make {

	private int sacount=0;
	private String[] sa=new String[1000];
	File mif0file,mif1file;
	public Mif_make(File mirfile,File argmif0file,File argmif1file){
		mif0file=argmif0file;
		mif1file=argmif1file;
		BufferedReader fw = null;
		String st;
		try {
			fw = new BufferedReader(new InputStreamReader (new FileInputStream(mirfile), "UTF-8"));
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

		
		
	}
	
	public void make(){
		String[][] saw=new String[sacount][2];
		for(int i=0;i<sacount;i++){
			saw[i]=sa[i].split(" |,");
		}

		BufferedWriter fww = null;
		try {
			fww = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mif0file, false), "UTF-8"));
			fww.append("-- ArrCompliar\n");
			fww.append("-- Quartus II generated Memory Initialization File (.mif)\n");
			fww.newLine();
			fww.append("WIDTH=8;\n");
			fww.append("DEPTH=256;\n");
			fww.newLine();
			fww.append("ADDRESS_RADIX=UNS;\n");
			fww.append("DATA_RADIX=UNS;\n");
			fww.newLine();
			fww.append("CONTENT BEGIN");
			for(int i=0;i<sacount;i++){
				fww.append(String.valueOf(i)+"  :  "+saw[i][0]+";\n");
			}
			fww.append("["+String.valueOf(sacount)+"..255]  :  0;\n");
			fww.append("END;\n");
			fww.flush(); // 全部写入缓存中的内容
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (fww != null) {
				try {
					fww.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			fww = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mif1file, false), "UTF-8"));
			fww.append("-- ArrCompliar\n");
			fww.append("-- Quartus II generated Memory Initialization File (.mif)\n");
			fww.newLine();
			fww.append("WIDTH=8;\n");
			fww.append("DEPTH=256;\n");
			fww.newLine();
			fww.append("ADDRESS_RADIX=UNS;\n");
			fww.append("DATA_RADIX=UNS;\n");
			fww.newLine();
			fww.append("CONTENT BEGIN");
			for(int i=0;i<sacount;i++){
				fww.append(String.valueOf(i)+"  :  "+saw[i][1]+";\n");
			}
			fww.append("["+String.valueOf(sacount)+"..255]  :  0;\n");
			fww.append("END;\n");
			fww.flush(); // 全部写入缓存中的内容
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (fww != null) {
				try {
					fww.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
