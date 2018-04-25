package com.cj.MyCompliar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

final class SLR{
	private int[][][] slr;
	public void setSLR(int[][][] arg_slr){
		slr=arg_slr;
	}
	int[][][] getSLR(){
		return slr;
	}
}

class Genitem{
	int geindex;
	int pos;
	int indexcode;
	public Genitem(int arg_geindex,int arg_pos){
		geindex=arg_geindex;
		pos=arg_pos;
		indexcode=geindex*1000+arg_pos;
	}
	protected Genitem clone(){
		return new Genitem(geindex,pos);
	}
	public void setpos(int arg_pos){
		pos=arg_pos;
		indexcode=geindex*1000+arg_pos;
	}
}

class Genclo{
	Genitem[] geilist =new Genitem[100];
	int geilistcount=0;
	boolean has(Genitem gei){
		for(int i=0;i<geilistcount;i++ ){
			if(geilist[i].indexcode==gei.indexcode)
				return true;
		}
		return false;
	}
	void addgei(Genitem gei){
		geilist[geilistcount]=gei;
		geilistcount++;
		
	}
	boolean equal(Genclo other){
		if(other.geilistcount!=geilistcount)
			return false;
		for(int i=0;i<geilistcount;i++){
			if(geilist[i].indexcode!=other.geilist[i].indexcode)
				return false;
		}
		return true;
	}
}

public class SLRGenerator {
	public Genexp[] gelist;
	private int gelistcount;
	final private int maxgc=200;
	private Genclo[] gclist=new Genclo[maxgc];
	private int gclistcount=0;
	public int[][][] slr = new int[maxgc][Tt.values().length][2];
	private boolean[][] follow = new boolean[Tt.values().length][Tt.values().length];
	
	public SLRGenerator(){
		for (int i=0;i<maxgc;i++)
			for (int j=0;j<Tt.values().length;j++)
				slr[i][j][0]='e';
	}
	
	private int addgc(Genclo newgc){
		gclist[gclistcount]=newgc;
		gclistcount++;
		return gclistcount-1;
	}
	
	private void closure(Genclo gc){
		boolean b;
		do{
			b=false;
			for(int i1=0;i1<gc.geilistcount;i1++){
				if(gc.geilist[i1].pos==gelist[gc.geilist[i1].geindex].genlen)
					continue;
				if(i1>=gc.geilistcount) break; 
				Tt t1 = gelist[gc.geilist[i1].geindex].Rtokentag[gc.geilist[i1].pos];
				for (int i2=0;i2<gelistcount;i2++){
					Genitem t2 = new Genitem(i2,0);
					if((gelist[i2].Ltokentag==t1) && !gc.has(t2)){
						gc.addgei(t2);
						b=true;
					}
				}
			}
		}while(b);
		Genitem t3;
		for (int i=0;i<gc.geilistcount-1;i++)
			for (int j=i+1;j<gc.geilistcount;j++)
				if(gc.geilist[i].indexcode>gc.geilist[j].indexcode){
					t3=gc.geilist[i];
					gc.geilist[i]=gc.geilist[j];
					gc.geilist[j]=t3;
				}
	}
	

	private void ss(int state, Tt tt, int actype,int arg) {
		if(slr[state][tt.ordinal()][0]=='r') {
			if(actype=='s' || actype=='a'){
	//			System.out.print("over write r to s or a->");
				slr[state][tt.ordinal()][0] = actype;
				slr[state][tt.ordinal()][1] = arg;
			} else {
	//			System.out.print("refuse over write ->");
			}
		} else {
			slr[state][tt.ordinal()][0] = actype;
			slr[state][tt.ordinal()][1] = arg;
		}
	//	System.out.printf("%d:%s:%c%d\n", state,tt.toString(),(char)actype,arg);
	}
	
	private void makefollow(int firstge){
		for(int i=0;i<Tt.values().length;i++)
			for(int j=0;j<Tt.values().length;j++)
				follow[i][j]=false;
		/*
		 * namefollow Version 2
		 */
	
		int[][] mw= new int[Tt.values().length][Tt.values().length];
		int[][] mf= new int[Tt.values().length][Tt.values().length];
		for(int i=0;i<Tt.values().length;i++)
			for(int j=0;j<Tt.values().length;j++) {
				mf[i][j]=0;
				mw[i][j]=0;
			}
		for(Tt it:Tt.values()){
			mw[it.ordinal()][it.ordinal()]=1;
			mf[it.ordinal()][it.ordinal()]=1;
		}
		boolean b;
		for(Tt it:Tt.values()){
			b=true;
			while(b){
				b=false;
				for(Tt it2:Tt.values()){
					if(mw[it.ordinal()][it2.ordinal()]==1){
						mw[it.ordinal()][it2.ordinal()]=2;
						for(int i=0;i<gelistcount;i++)
							if (gelist[i].Ltokentag==it2)
								if(mw[it.ordinal()]
										[gelist[i].Rtokentag[gelist[i].genlen-1].ordinal()]==0){
									b=true;
									mw[it.ordinal()]
											[gelist[i].Rtokentag[gelist[i].genlen-1].ordinal()]=1;
								}
					}
					
				}
			}
		}	
		
		for(Tt it:Tt.values()){
			b=true;
			while(b){
				b=false;
				for(Tt it2:Tt.values()){
					if(mf[it.ordinal()][it2.ordinal()]==1){
						mf[it.ordinal()][it2.ordinal()]=2;
						for(int i=0;i<gelistcount;i++)
							if (gelist[i].Ltokentag==it2)
								if(mf[it.ordinal()]
										[gelist[i].Rtokentag[0].ordinal()]==0){
									b=true;
									mf[it.ordinal()]
											[gelist[i].Rtokentag[0].ordinal()]=1;
								}
					}
					
				}
			}
		}	
		

		for(int i=0;i<gelistcount;i++)
			for (int j=0;j<gelist[i].genlen-1;j++) {
				Tt t1=gelist[i].Rtokentag[j];
				Tt t2=gelist[i].Rtokentag[j+1];
				for(Tt it1:Tt.values())
					for(Tt it2:Tt.values())
						if(mw[t1.ordinal()][it1.ordinal()]==2 && mf[t2.ordinal()][it2.ordinal()]==2)
							follow[it1.ordinal()][it2.ordinal()]=true;
			}
			
		/*
		 * makefollow Version 1 is a shit!
		 */
		
		/*
		for(Tt it:Tt.values()){
			Tt[] t=new Tt[100];
			int tcount=1;
			t[0]=it;
			for (int i=0;i<tcount;i++) {
				Tt t2=t[i];
				for(Genexp it2:gelist){
					if (it2==null) break;
					if(it2.Rtokentag[it2.genlen-1]==t2){
						Tt t3=it2.Ltokentag;
						int j;
						for (j=0;j<tcount;j++)
							if(t[j]==t3) break;
						if(j==tcount){
							tcount=j+1;
							t[j]=t3;
						}
					}
				}
			}
			System.out.print("");
			for (int k=0;k<tcount;k++)
			for(int i=0;i<gelistcount;i++)
				for (int j=0;j<gelist[i].genlen-1;j++)
					if(gelist[i].Rtokentag[j+1].ordinal()<=Tt.ENDF.ordinal()
							&&gelist[i].Rtokentag[j]==t[k])
					follow[it.ordinal()][gelist[i].Rtokentag[j+1].ordinal()]=true;
		}*/
	}

	public int[][][] genera(int firstge,Genexp[] arg_gelist){
		long endtime;
		long starttime;
		starttime=System.nanoTime();
		gelist=arg_gelist;
		gelistcount=gelist.length;
		makefollow(firstge);
		addgc(new Genclo());
		gclist[0].addgei(new Genitem(firstge,0));
		closure(gclist[0]);
		int i=0;
		char endchar;
		do{
			for(Tt it:Tt.values()){
				if(it.ordinal()<=Tt.ENDF.ordinal())
					endchar='s';
				else 
					endchar='g';
				Genclo gc=new Genclo();
				for(int j=0;j<gclist[i].geilistcount;j++) {
					Genitem gi=gclist[i].geilist[j];
					if( gelist[gi.geindex].genlen==gi.pos) {
						if (gi.geindex==firstge)
							ss(i,it,'a',firstge);
						else 
					//		if(endchar=='s') {
								if(follow[ gelist[gi.geindex].Rtokentag[gi.pos-1].ordinal() ]
										[it.ordinal()] )
									ss(i,it,'r',gi.geindex);
					//		}
						continue;
					} 
					if(it==gelist[gi.geindex].Rtokentag[gi.pos] ){
						Genitem gi2=gi.clone();
						gi2.setpos(gi2.pos+1);
						gc.addgei(gi2);
					}
				}
				closure(gc);
				boolean b1=true;
				if(gc.geilistcount==0)
					b1=false;
				else
					for(int j=0;j<gclistcount;j++) 
						if(gc.equal(gclist[j])) {
							ss(i,it,endchar,j);
							b1=false;
							break;
						}
				if(b1) {
					int i2=addgc(gc);
					ss(i,it,endchar,i2);
				}
			}
			i++;
		}while(i<gclistcount);
		endtime=System.nanoTime();
		System.out.printf("SLR Genera done, GC:%d, used time:%fms\n",gclistcount,(endtime-starttime)/1000000.0);
		
		File file = new File("D:\\text1_slr.slr");
		file.delete();
		BufferedWriter fw = null;

		if(mydebug.debug)
		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			fw.append("Tab"+"\t");
			for(Tt it:Tt.values())
				fw.append(it.toString()+"\t");
			fw.newLine();
			for(int it2=0;it2<gclistcount;it2++){
				fw.append(String.valueOf(it2)+"\t");
				for(Tt it:Tt.values()) {
					if(slr[it2][it.ordinal()][0]=='e')
						fw.append("_\t");
					else
						fw.append(String.valueOf((char) slr[it2][it.ordinal()][0])
							+String.valueOf(slr[it2][it.ordinal()][1])
							+"\t");
				}
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
		return slr;
	}
}
