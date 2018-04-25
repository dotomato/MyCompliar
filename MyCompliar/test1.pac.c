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