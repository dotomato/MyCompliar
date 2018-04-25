package com.cj.MyCompliar;

public class Token {


	Tt tag;
	String val_s;
	float val_f;
	int val_i;
	
	public static Token NewFloatToken(float arg){
		Token t = new Token();
		t.tag=Tt.FLOAT;
		t.val_f=arg;
		return t;
	}
	

	public static Token NewIntToken(int arg){
		Token t = new Token();
		t.tag=Tt.INT;
		t.val_i=arg;
		return t;
	}
	
	public static Token NewIdToken(String arg){
		Token t = new Token();
		if(arg.equals("if"))
			t.tag=Tt.IF;
		else if(arg.equals("then"))
			t.tag=Tt.THEN;
		else if(arg.equals("else"))
			t.tag=Tt.ELSE;
		else if(arg.equals("eof"))
			t.tag=Tt.EOF;
		else if(arg.equals("int")) 
			t.tag=Tt.TYPE;
		else if(arg.equals("float"))
			t.tag=Tt.TYPE;
		else if(arg.equals("return"))
			t.tag=Tt.RETURN;
		else if(arg.equals("while"))
			t.tag=Tt.WHILE;
		else if(arg.equals("do"))
			t.tag=Tt.DO;
		else if(arg.equals("true"))
			t.tag=Tt.TRUE;
		else if(arg.equals("false"))
			t.tag=Tt.FALSE;
		else if(arg.equals("and"))
			t.tag=Tt.BOOLOP3;
		else if(arg.equals("or"))
			t.tag=Tt.BOOLOP3;
		else if(arg.equals("not"))
			t.tag=Tt.BOOLOP2;
		else if(arg.equals("arg"))
			t.tag=Tt.ARGTYPE;
		else if(arg.equals("write"))
			t.tag=Tt.WRITE;
		else if(arg.equals("read"))
			t.tag=Tt.READ;
		else if(arg.equals("void"))
			t.tag=Tt.VOID;
	/*	else if(arg.equals("write"))
			t.tag=Tt.WRITE;
		else if(arg.equals("read"))
			t.tag=Tt.READ;*/
		else
			t.tag=Tt.ID;
		t.val_s=arg;
		return t;
	}
	
	public static Token NewToken(Tt arg){
		Token t = new Token();
		t.tag=arg;
		return t;
	}
	
	public static Token NewTagToken(Tt arg1,String arg2){
		Token t = new Token();
		t.tag=arg1;
		t.val_s=arg2;
		return t;
	}
	
	public String toString(){
		switch(tag){
		case FLOAT:
			return String.valueOf(val_f);
		case INT:
			return String.valueOf(val_i);
		case SELFADD:
			return "SELFADD";
		case SELFDEC:
			return "SELFDEC";
		case LEFTBRA:
			return "(";
		case RIGHTBRA:
			return ")";
		case ID:
		case MULOP:
		case ADDOP:
		case BOOLOP: 
			return val_s;
		case BOOLOP2:
		case BOOLOP3:
			return val_s.toUpperCase();
		case ASSIGN:
			return "=";
		case ENDF:
			return "ENDF";
		case EXP:
			return "EXP";
		case _EXP:
			return "_EXP";
		case FAC:
			return "FAC";
		case TEM:
			return "TEM";
		case IF:
			return "IF";
		case THEN:
			return "THEN";
		case ELSE:
			return "ELSE";
		case EOF:
			return "EOF";
		case LEA:
			return "LEA";
		case POINTTO:
			return "POINTTO";
		default:
			break;
		}
		return String.valueOf(tag.ordinal());
		
	}
}
