package com.cj.MyCompliar;

import java.io.IOException;
import java.io.InputStream;

public class Lex{
	private String s="";
	private int i=0;
	private InputStream in;
	private int state = 0;
	private char c,lc;
	private boolean retracted=false;
	private int k[]=new int[10];//parse helper: 
	private int rposl=0;
	private int rposc=0;
	
	/*0  0:no dot. 1:has dot;
	 *1  0:e>=0   1:e<0
	 * 
	 */
	
	private void nextchar(){
		i++;
		if(retracted){
			c=lc;
			if(s.length()==0)
				s=""+c;
			retracted=false;
		} else {
			try {
				c= (char)in.read();
				if(c==13) {
					rposl++;
					rposc=0;
				}else
					rposc++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			lc=c;
			s=s+c;
		}
	}
	
	private void retract(){
		retracted=true;
		c=lc;
		i--;
	}
	
	private boolean isdigit(){
		return ((c>='0') && (c<='9'));
	}
	
	private boolean isletter(){
		return (Character.isLetter(c) || c=='_' );
	}
	
	public void printnowreadpos(){
		System.out.printf("line %d,%d token \"%s\"",rposl+1,rposc,s);
	}
	
	public Token getnexttoken(){
		state=0;
		s="";
		i=0;
		k[0]=0;
		k[1]=0;
		while(true){
	//		System.out.printf("%c%d:%d:%d\n",c, state,i,be);
			switch(state){
			case 0:
				nextchar();
				if(isdigit()) {
					state=1;
					break;
				}
				if(isletter()){
					state=28;
					break;
				}
				switch(c){
				case ' ': 
				case 13:
				case 10:
				case 9:
					i=0;
					s="";
					break;
				case '/':
					return Token.NewTagToken(Tt.MULOP, "DIV");
				case '*':
					return Token.NewTagToken(Tt.MULOP, "MUL");
				case '+':
					state=10; break;
				case '-':
					state=13; break;
				case '(':
					return Token.NewToken(Tt.LEFTBRA);
				case ')':
					return Token.NewToken(Tt.RIGHTBRA);
				case '<':
					state=18; break;
				case '=':
					state=19; break;
				case '>':
					state=20; break;
				case ';':
					return Token.NewToken(Tt.ENDF);
				case '\0':
					return Token.NewToken(Tt.EOF);
				case '{':
					return Token.NewToken(Tt.LEFTBIGBRA);
				case '}':
					return Token.NewToken(Tt.RIGHTBIGBRA);
				case ',':
					return Token.NewToken(Tt.COMMA);
				case '[':
					return Token.NewToken(Tt.LEFTQBRA);
				case ']':
					return Token.NewToken(Tt.RIGHTQBRA);
				case '&':
					return Token.NewToken(Tt.LEA);
				case '^':
					return Token.NewToken(Tt.POINTTO);
					
				default:
					state=-1;
					break;	
				}
				break;
			case 1:
				nextchar();
				if(isdigit()) 
					break;
				if(c=='.') {
					state=2;
					break;
				}
				if(c=='e') {
					state = 4;
					break;
				}
				state=7;
				break;
			case 2:
				k[0]=1;
				nextchar();
				if(isdigit()) {
					state=3;
					break;
				}
				state=-1;
				break;
			case 3:
				nextchar();
				if(isdigit())
					break;
				if(c=='e'){
					state=4;
					break;
				}
				state=7;
				break;
			case 4:
				k[1]=1;
				nextchar();
				if((c=='+') || (c=='-')) {
					state=5;
					break;
				}
				if(isdigit()){
					state=6;
					break;
				}
				state=-1;
				break;
			case 5:
				nextchar();
				if(isdigit()){
					state=6;
					break;
				}
				state=-1;
				break;
			case 6:
				nextchar();
				if(isdigit()){
					break;
				}
				state=7;
				break;
			case 7:
				retract();
				if((k[0]==0)&&(k[1]==0))
					return Token.NewIntToken(Integer.parseInt(s.substring(0, i)));
				else
					return Token.NewFloatToken(Float.parseFloat(s.substring(0, i)));
			case 10:
				nextchar();
				if(c=='+')
					return Token.NewToken(Tt.SELFADD); 
				retract();
				return Token.NewTagToken(Tt.ADDOP, "ADD");
			case 13:
				nextchar();
				if(c=='-')
					return Token.NewToken(Tt.SELFDEC);
				retract();
				return Token.NewTagToken(Tt.ADDOP, "DEC");
			case 18:
				nextchar();
				if(c=='=') 
					return Token.NewTagToken(Tt.BOOLOP,"JG");
				if(c=='>')
					return Token.NewTagToken(Tt.BOOLOP,"JE");
				retract();
				return Token.NewTagToken(Tt.BOOLOP,"JGE");
			case 19:
				nextchar();
				if(c=='=')
					return Token.NewTagToken(Tt.BOOLOP,"JNE");
				retract();
				return Token.NewToken(Tt.ASSIGN);
			case 20:
				nextchar();
				if(c=='=')
					return Token.NewTagToken(Tt.BOOLOP,"JL");
				retract();
				return Token.NewTagToken(Tt.BOOLOP,"JLE");
			case 28:
				nextchar();
				if(isdigit() || isletter())
					break;
				retract();
				return Token.NewIdToken(s.substring(0, i));
			case -1:
				return null;
			}
		}
	}
	
	public Lex(InputStream arg_in){
		 in=arg_in;
	}
}
