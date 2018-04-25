program Project1;

{$APPTYPE CONSOLE}

uses
  SysUtils,Classes,Windows;
type
  arr=record
    asp:integer;
    asize:integer;
  end;

var
  F:Textfile;
  filename:string;
  s:string;
  stl:TStringList;
  si0:array [0..1000] of integer;
  si1:array [0..1000] of integer;
  sacount,i,j,k:longint;
  sta:array[0..100000000] of longint;
  sp,fp,pc:integer;      //sp,fp,pc
  regf:integer;
  c1:int64;
  t1,t2:int64;
  r1:double;
  pt:longint;
  arrsta:array [0..1000] of arr;
  arrcount:integer;
  mydebug:boolean;
  inputt:integer;
  regt:integer;


begin
  mydebug:=true;
  QueryPerformanceFrequency(c1);

  filename:=ParamStr(1);
  Assignfile(F,filename);
  reset(F);
  stl:=TStringList.Create;
  stl.Delimiter:=' ';
  sacount:=0;
  while(not eof(F)) do begin
      read(f,si0[sacount],si1[sacount]);
      inc(sacount);
  end;
  //write(sacount);
  sp:=2;
  fp:=0;
  pc:=0;
  sta[1]:=-2;
  pt:=0;
  arrcount:=0;
  regf:=0;
  QueryPerformanceCounter(t1);
  while(pc<>-1) do begin
    if(mydebug=true) then begin
      write('sp:',sp,' fp:',fp,' pc:',pc+1);
      for i := 0 to 15 do
        write(' ',sta[i]);
      writeln;
    end;
    pt:=pt+1;                     //sp,fp,pc
    case(si0[pc])of
    0: ;
    1:begin sp:=sp+1; sta[sp]:=si1[pc]; end;
    2:begin sp:=sp+1;
      case (si1[pc]) of
       0: sta[sp]:=sp;
       1: sta[sp]:=fp;
       2: sta[sp]:=pc;
      end;
    end;
    3:begin sp:=sp+1; sta[sp]:=sta[fp+3+si1[pc]]; end;
    4:begin
      case (si1[pc]) of
        0:sp:=sta[sp];
        1:fp:=sta[sp];
        2:pc:=sta[sp];
      end;
      sp:=sp-1;
    end;
    5:begin sta[fp+3+si1[pc]]:=sta[sp]; sp:=sp-1; end;
    6:pc:=si1[pc];
    7:begin regf:=sta[sp-1]-sta[sp]; sp:=sp-2; end;
    8:if regf>0 then pc:=si1[pc];
    9:if regf>=0 then pc:=si1[pc];
    10:if regf<0 then pc:=si1[pc];
    11:if regf<=0 then pc:=si1[pc];
    12:if regf=0 then pc:=si1[pc];
    13:if regf<>0 then pc:=si1[pc];
    14:begin sta[sp-1]:=sta[sp-1]+sta[sp]; sp:=sp-1; end;
    15:begin sta[sp-1]:=sta[sp-1]-sta[sp]; sp:=sp-1; end;
    16:begin sta[sp-1]:=sta[sp-1] div sta[sp]; sp:=sp-1; end;
    17:begin sta[sp-1]:=sta[sp-1]*sta[sp]; sp:=sp-1; end;
    18:sta[fp]:=sta[sp];
    19:begin
      regt:=sp;
      sp:=sp+sta[fp+3+si1[pc]];
      sta[fp+3+si1[pc]]:=regt+1;
    end;
    20:begin
      sta[sp]:=sta[ sta[ sp]];
    end;
    21:begin
      sta[ sta[sp-1]]:=sta[sp];
    end;
    22:begin sp:=sp+1; sta[sp]:=fp+3+si1[pc]; end;
    23:begin sp:=sp+1; end;
    24:begin sp:=sp-1; end;
    25:begin writeln(sta[sp-1],':',sta[sp]); sp:=sp-2; end;
    26:begin read(inputt); sp:=sp+1; sta[sp]:=inputt; end;
    end;
    pc:=pc+1;
  {  if mydebug=true then readln;     }
  end;
  QueryPerformanceCounter(t2);
  r1:=(t2-t1)/c1*1000;//取得计时时间，单位毫秒(ms)

  writeln('exit ',sta[0]);
  writeln('time: ',r1:0:4,'ms  pt: ',pt);
  while(true)do begin
    sleep(100);
  end;
end.
