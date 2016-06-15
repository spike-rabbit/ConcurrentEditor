package ce.shared;

/***
 * Handels Users
 * @author Florian.Loddenkemper
 *
 */
public class UserAccept {
	private final String userName;

	/***
	 * creates new User
	 * @param userName name given to User
	 */
	public UserAccept(String userName) {
		super();
		this.userName = userName;
	}

	/***
	 * returns username
	 * @return the name of this user
	 */
	public String getUserName() {
		return this.userName;
	}

}
