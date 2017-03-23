package main;

public class Tuple<S, T> {
	private S s;
	private T t;

	public Tuple(S s, T t) {
		this.s = s;
		this.t = t;
	}

	public S getS() {
		return this.s;
	}

	public T getT() {
		return this.t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		try {
			return this.equals((Tuple<? extends S, ? extends T>) obj);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public boolean equals(Tuple<? extends S, ? extends T> o) {
		return this.s.equals(o.s) && this.t.equals(o.t);
	}

	@Override
	public int hashCode() {
		return s.hashCode() * 31 + t.hashCode();
	}

}
