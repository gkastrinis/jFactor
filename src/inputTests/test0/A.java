import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class A {
	public static void main(String args[]){
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9(10);
		test10(10);
		test11(10);
		test12(10);
		test13();
		test14();
		test15();
		test16(5, 10);
		test17(200);
	}

	static void test1() {
		int a = 1;
		int b = a * 2;
		int c = -b;
		int d = c - 3 * 4;
	}

	static void test2() {
		B b1 = new B(new B());
		B b2 = new B(b1.foobar(123));
	}

	static void test3() {
		B b1 = new B();
		B b8 = b1;
		B b2 = new B("hello world");
		B b3 = new B(1234);
		b3.foobar(10);
		b3.test(-2);
	}

	static void test4() {
		long a = 1234567;
		long b = a << 2;
		long c = b >> 3;
		long d = c >>> 4;
	}

	static void test5() {
		long a = 1234;
		long b = 5678;
		boolean f1 = a > b;
		float c = 1.2F;
		float d = 3.4F;
		boolean f2 = c <= d;
		double e = 1.2;
		double f = 3.4;
		boolean f3 = e == f;
	}

	static void test6() {
		int x = 10;
		int y = 20;
		boolean f1 = x > y;
		if (x > 4) {
			y = 0;
		} else
			y = 1;
		B b1 = new B();
		B b2 = new B();
		boolean f2 = (b1 == b2);
		B b3 = null;
		boolean f3;
		if (b3 == null)
			f3 = true;
		else
			f3 = false;
	}

	static void test7() {
		int i = 0;
		int max = 10;
		int res = 0;
		while (i < max) {
			res += i * i;
			i++;
		}
	}

	static void test8() {
		int error;
		int done = 0;
		try {
			int a = 30, b = 0;
			int c = a / b;
			if (c > 2) {
				c = 100;
				throw new RuntimeException("fail!!!"+c);
			}
			new SimpleDateFormat("MM, dd, yyyy").parse("invalid-date");
			System.out.println ("Result = " + c);
			error = 0;
		}
		catch(ArithmeticException e) {
			System.out.println("Can't 		divide \\\" a \" number by 0");
			test8_inner();
			error = 1;
		}
		catch(ParseException e) {
			error = 2;
		}
		finally {
			done = 10;
		}
		System.out.println(error);
	}

	static void test8_inner() throws ArithmeticException, NullPointerException {
		int a = 10;
		int c = a / 0;
	}

	static int test9(int m) {
		int f0 = 0, f1 = 1, f2 = 0, i;
		if (m <= 1) {
			return m;
		}
		else {
			for (i = 2 ; i <= m ; i++) {
				f2 = f0 + f1;
				f0 = f1;
				f1 = f2;
			}
			return f2;
		}
	}

	static void test10(int n) {
		int k = 0;
		int i = 1;
		int j = 2;
		while (i <= n) {
			k = n;
			j = j + k;
			k = j;
			i = i + 1;
		}
		if (k == 1) {
			System.out.println(j);
		} else {
			i = i - 1;
		}
		j = i;
	}

	static void test11(int n) {
		int x = 1, y = 2;
		if (n > 10) {
			if (n > 100) {
				y = -2;
				return;
			}
			else {
				x = 20;
			}
		} else {
			if (n < 5) {
				return;
			}
			else {
				x = -20;
			}
		}
		y = x;
	}

	static void test12(int n) {
		int x, y;
		if (n > 0) {
			x = 1;
		} else {
			x = 2;
		}
		y = x;
		x = 3;
		if (n % 2 == 0) {
			x = y;
		} else {
			x = 5;
		}
		int z = y;
		int w = x;
	}

	static void test13() {
		int i = 20, j = 0, k =10;
		while (i > 0) {
			i -= 2;
			j++;
			--k;
		}
		System.out.println(i);
		System.out.println(j);
		System.out.println(k);
	}

	static void test14() {
		int a = 10;
		int b = a + 20;
		System.out.println(b);
		int c = test9
				(b);
		Integer d = new Integer(c);
	}

	static void test15() {
//		List<Integer> l = new ArrayList<>();
//		l.add(1);
//		l.add(2);
//		for (Integer i : l) {
//			System.out.println(i);
//		}
//		for (int i = 0 ; i < l.size() ; i++)
//			System.out.println(l.get(i));
//		List<Object> i = new ArrayList<>();
//		i.add("HI");
//		System.out.println(i.get(0));
		int sum = 0;
		for (int i = 0 ; i < 10 ; i++) sum += i;
		double j = 20;
		double i = 10.3;
		System.out.println(sum + " " + i);
	}

	static int test16(int n, int a) {
		int i = 0;
		if (i > n) {
			n = 0;
			i = 100;
		}
		else
			i = -100;
		return i + n + a;
	}

	static int test17(int n) {
		while (n > 0) {
			System.out.println(n);
			n--;
		}
		return n;
	}
}

class B {
	int f1;
	String f2;

	public B() {
		f1 = 1;
	}

	public B(B b) {
		f1 = b.f1;
		f2 = b.f2;
	}

	public B(int x) {
		f1 = x;
		f2 = "dummy";
	}

	public B(String s) {
		f1 = s.length();
		f2 = s;
	}

	public int foobar(double x) {
		double y;
		y = x * 42;
		test(123);
		double c = baz(y, y*2*2);
		System.out.println(y*c);
		return 100;
	}

	public void test(int a) {
		B b = new B(1000);
		System.out.println(b.baz(10, 20.5 * a));
	}

	public double baz(double x, double z) { return x; }
}