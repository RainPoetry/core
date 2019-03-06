package concurrent;

import sun.misc.Unsafe;

/**
 * User: chenchong
 * Date: 2019/2/20
 * description:
 */
public class UnsafeDemo {

	private String name;

	public static void main(String[] args) throws NoSuchFieldException {
		Unsafe u = Unsafe.getUnsafe();
		Class c = UnsafeDemo.class;
		System.out.println(u.objectFieldOffset(c.getDeclaredField("name")));
	}
}
