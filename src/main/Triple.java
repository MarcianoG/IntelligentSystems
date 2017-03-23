package main;

public class Triple<S, T, U> {
	private S s;
	private T t;
	private U u;

	public Triple(S s, T t, U u) {
		this.s = s;
		this.t = t;
		this.u = u;
	}

	public S getS() {
		return this.s;
	}

	public T getT() {
		return this.t;
	}

	public U getU() {
		return this.u;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		try {
			return this.equals((Triple<? extends S, ? extends T, ? extends U>) obj);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public boolean equals(Triple<? extends S, ? extends T, ? extends U> o) {
		return this.s.equals(o.s) && this.t.equals(o.t) && this.u.equals(o.u);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + this.s.hashCode();
		hash = hash * 31 + this.t.hashCode();
		hash = hash * 31 + this.u.hashCode();
		return hash;
	}

}
