#include "threads/arithmetic.h"
#include <stdio.h>

/* converts an integer to a float point */
signed int cnv_t_fp( int n)
{
	return n*(f);
}

/* converts a float to integer */
signed int cnvrt_to_int (signed int n)
{
	return n/(f);
}

/* converts an float to a rounded int */
signed int cnvrt_rounding(signed int x)
{
	if(x >= 0)
	{
		return (x +(f)/2)/(f);
	}
	else
	{
		return (x-(f)/2)/(f);
	}
}

/* adds two integers x and y */
signed int add(signed int x , signed int y )
{
	return x+y;
}

/* adds an integer x to a float y and returns a float */
signed int add_x_n(int x , int y)
{
	return x + cnv_t_fp(y);
}

/* subtracts an float from a float */
signed int sub(signed int x , signed int y )
{
	return x-y;
}

/* multiplies a float by a float */
signed int mul(signed int x ,  int y )
{
	return (((int64_t) x) * y) / (f);
}

/* divides a float by a float */
signed int div(signed int x ,  int y )
{
	return (((int64_t) x) * (f)) / (y);
}

/* multiplies a float by an int */
signed int mul_x_n(signed int x ,  int y )
{
 	return x*y;
}

/* divides a float by an int */
signed int div_x_n(signed int x ,  int y )
{
	return x/y;
}
