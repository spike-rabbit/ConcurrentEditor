package ce.shared;

import java.io.Serializable;

/***
 * Handels Users
 *
 * @author Florian.Loddenkemper
 *
 */
public class UserAccept implements Serializable {

	private static final long serialVersionUID = 8067381376390806081L;
	private final String userName;

	/***
	 * creates new User
	 *
	 * @param userName
	 *            name given to User
	 */
	public UserAccept(String userName) {
		super();
		this.userName = userName;
	}

	/***
	 * returns username
	 *
	 * @return the name of this user
	 */
	public String getUserName() {
		return this.userName;
	}

}
