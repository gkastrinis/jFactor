public class A {
	public static void main(String args[]){
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
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
		boolean f2 = c > d;
		double e = 1.2;
		double f = 3.4;
		boolean f3 = e > f;
		if (b > 4) {
			a = 0;
		} else
			a = 1;
	}

	static void test6() {
		int x = 10;
		int y = 20;
		boolean f4 = x > y;
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