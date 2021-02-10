public class A {
	public static void main(String args[]){
		test1();
		//B b1 = new B(new B());
		//B b8 = b1;
//		B b2 = new B("hello world");
//		B b3 = new B(1234);
//		b3.foobar(10);
//		b3.test(-2);
		//B b4 = new B(b1.foobar(123));
	}

	static void test1() {
		int a = 1;
		int b = a * 2;
		int c = b;
		int d = c - 3 * 4;
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