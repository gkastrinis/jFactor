public class A {
	public static void main(String args[]){
		B b1 = new B(42);
		B b2 = new B("hello world");
		B b3 = new B(1234);
		b3.foobar(10);
		b3.test(-2);
	}
}

class B {
	int f1;
	String f2;

	public B() {
		f2 = "-";
		f1 = 0;
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