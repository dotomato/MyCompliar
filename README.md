# MyCompliar
一个玩具编译器

很久之前写的一个编译器项目了，大概是大三的时候吧。

### 其中软件包括：
 - MyCompliar 编译器本体，用于将高级语言编译成汇编语言、机器语言、ROM文件，是一个完成的工具链。编译器本身使用JAVA编写。
 - fpga_cpu_sim 仿真器，可以调试机器语言，输出程序每一条指令的运算过程、栈状态、寄存器状态。仿真器本身使用Delphi编写。
 - cpu01 CPU软核，可以在EP4CE6E22芯片上运行。软核本身使用VHDL编写。
 
### 语言定义包括：
 - 一个自定义的高级语言:有点像pascal又有点像c，支持常见的if语句、while语句、四则运算、布尔表达式、子函数、端口输出，总之是一个只是为了我方便而创造出来的语言啦，不过最大特色是支持动态大小的数组这个是真的。
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

       int fac(arg int n)
       {
         if n==1 then
           return 1;
         else return n*fac(n-1);
       }

       int add(arg int a;arg int b)
       {
         return a+b;
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
