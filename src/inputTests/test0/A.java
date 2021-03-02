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
		test9();
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
		boolean error;
		int done = 0;
		try {
			int a = 30, b = 0;
			int c = a / b;
			if (c > 2)
				throw new RuntimeException("fail!!!");
			//System.out.println ("Result = " + c);
			error = false;
		}
		catch(ArithmeticException e) {
			System.out.println("Can't 		divide \\\" a \" number by 0");
			test8_inner();
			error = true;
		}
		finally {
			done = 10;
		}
	}

	static void test8_inner() throws ArithmeticException, NullPointerException {
		int a = 10;
		int c = a / 0;
	}

	static void test9() {
		int res = test9_inner1(4);
		System.out.println(res);
		test9_inner2(5);
		test9_inner3(5);
	}

	static int test9_inner1(int m) {
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

	static void test9_inner2(int n) {
		int k = 0;
		int i = 1;
		int j = 2;
		while (i <= n) {
			j = j * 2;
			k = 1;
			i = i + 1;//i += 1;//i--;
		}
		if (k == 1) {
			System.out.println(j);
		} else {
			i = i - 1;
		}
		j = i;
	}

	static void test9_inner3(int n) {
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