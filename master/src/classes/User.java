package classes;

public abstract class User<T> extends Person<T> {

	private final String username;
	private final String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", this.getUsername(), this.getPassword());
	}

}
