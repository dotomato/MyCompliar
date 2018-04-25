# MyCompliar
一个玩具编译器

很久之前写的一个编译器项目了，大概是大三的时候吧。

### 其中软件包括：
 - MyCompliar 编译器本体，用于将高级语言编译成汇编语言、机器语言、ROM文件，是一个完成的工具链。编译器本身使用JAVA编写。
 - fpga_cpu_sim 仿真器，可以调试机器语言，输出程序每一条指令的运算过程、栈状态、寄存器状态。仿真器本身使用Delphi编写。
 - cpu01 CPU软核，可以在EP4CE6E22芯片上运行。软核本身使用VHDL编写。
 
### 语言定义包括：

一个自定义的高级语言，有点像pascal又有点像c，支持常见的if语句、while语句、四则运算、布尔表达式、子函数、端口输出，总之是一个只是为了我方便而创造出来的语言啦，不过最大特色是支持动态大小的数组这个是真的。
一个例子如下：

	int prime(arg int max)
	int num[max];
	int i;
	int j;
	int k;
	int l;
	int m;
	int n;
	{
	 k=1;
	 num[0]=2;
	 i=1;
	 j=2;
	 while i<max do {
	   n=1;
	   l=0; 
	   j=j+1;
	   while l<i do {
		 m=j / num[l];
		 m=j-num[l]*m;
		 if m==0 then {
		   l=i;
		   n=0;
		 }
		 l=l+1;
	   }
	   if n==1 then {
		 num[i]=j;
		 i=i+1;
	   }
	 };
	 return num[max-1];
	}

	int main(void)
	int i;
	int j;
	int k;
	int l;
	int n;
	{
	 write(0,prime(10));
	}
	eof;

然后编译出来的汇编是：

	GOTO main
	prime:
	PUSH M0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	ARRWR M1
	PUSH 1
	POP M4
	PUSH M1
	PUSH 0
	ADD
	PUSH 2
	MOV
	PUSH 1
	POP M2
	PUSH 2
	POP M3
	L10:
	PUSH M2
	PUSH M0
	CMP
	JGE L2
	GOTO L1
	L1:
	PUSH 1
	POP M7
	PUSH 0
	POP M5
	PUSH M3
	PUSH 1
	ADD
	POP M3
	L7:
	PUSH M5
	PUSH M2
	CMP
	JGE L4
	GOTO L3
	L3:
	PUSH M3
	PUSH M1
	PUSH M5
	ADD
	TABLE
	DIV
	POP M6
	PUSH M3
	PUSH M1
	PUSH M5
	ADD
	TABLE
	PUSH M6
	MUL
	DEC
	POP M6
	PUSH M6
	PUSH 0
	CMP
	JNE L6
	GOTO L5
	L5:
	PUSH M2
	POP M5
	PUSH 0
	POP M7
	L6:
	PUSH M5
	PUSH 1
	ADD
	POP M5
	GOTO L7
	L4:
	PUSH M7
	PUSH 1
	CMP
	JNE L9
	GOTO L8
	L8:
	PUSH M1
	PUSH M2
	ADD
	PUSH M3
	MOV
	PUSH M2
	PUSH 1
	ADD
	POP M2
	L9:
	GOTO L10
	L2:
	PUSH M1
	PUSH M0
	PUSH 1
	DEC
	ADD
	TABLE
	SETRTN
	PUSH FP
	PUSH 3
	ADD
	POP SP
	POP FP
	POP PC
	fac:
	PUSH M0
	PUSH 1
	CMP
	JNE L12
	GOTO L11
	L11:
	PUSH 1
	SETRTN
	GOTO L14
	L12:
	PUSH M0
	PUSH 0
	PUSH L13
	PUSH FP
	PUSH M0
	PUSH 1
	DEC
	PUSH SP
	PUSH 4
	DEC
	POP FP
	GOTO fac
	L13:
	MUL
	SETRTN
	L14:
	PUSH FP
	PUSH 3
	ADD
	POP SP
	POP FP
	POP PC
	add:
	PUSH M0
	PUSH M1
	ADD
	SETRTN
	PUSH FP
	PUSH 3
	ADD
	POP SP
	POP FP
	POP PC
	main:
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH 0
	PUSH L15
	PUSH FP
	PUSH 10
	PUSH SP
	PUSH 4
	DEC
	POP FP
	GOTO prime
	L15:
	WRITE
	PUSH FP
	PUSH 3
	ADD
	POP SP
	POP FP
	POP PC
