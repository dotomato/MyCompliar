package com.cj.MyCompliar;
public abstract class Genexp {
	Tt Ltokentag;
	Tt[] Rtokentag;
	int genlen;
	int geindex;
//	String syntaxcode;
	
	public Genexp(Tt arg_Ltokentag,Tt... arg_Rtokentag){
		Ltokentag=arg_Ltokentag;
		Rtokentag=arg_Rtokentag;
		genlen=Rtokentag.length;
	}
	
	/*public Genexp syntaxcode(String arg_syntaxcode){
		syntaxcode=arg_syntaxcode;
		return this;
	}*/
	
	public abstract void syntax(SyntaxStatck sta);
}
