package cz.helheim.rpg.diablolike.util;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class Pair<A, B> {
	private final A a;
	private final B b;

	public Pair(final A a, final B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}
}
