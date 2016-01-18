#include <stdint.h>

#define p 17 //real value 
#define q 14 //fixed point value
#define f 1<<q // shifting factor

signed int cnv_t_fp( int n) ;
signed int cnvrt_to_int (signed int n);
signed int cnvrt_rounding(signed int n);
signed int add(signed int x , signed int y );
signed int add_x_n(int x , int y);
signed int sub(signed int x , signed int y );
signed int mul(signed int x , signed int y );
signed int div(signed int x , signed int y );
signed int mul_x_n(signed int x , int n );
signed int div_x_n(signed int x , int n );


