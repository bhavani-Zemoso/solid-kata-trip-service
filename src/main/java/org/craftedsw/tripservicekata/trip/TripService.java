package org.craftedsw.tripservicekata.trip;

import java.util.ArrayList;
import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.IUserSession;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;

public class TripService {

	@Autowired
	public ITripDAO tripDAO;

	public TripService(ITripDAO tripDAO) {
		this.tripDAO = tripDAO;
	}

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		UserSession userSession = UserSession.getInstance();
		return getTripsByUser(user, userSession);
	}

	List<Trip> getTripsByUser(User user, IUserSession userSession) {
		User loggedUser = userSession.getLoggedUser();
		if (loggedUser != null) {
			if (isFriend(user, loggedUser)) {
				return tripDAO.findTripsByUser(user);
			}
			return new ArrayList<>();
		} else {
			throw new UserNotLoggedInException();
		}
	}

	private boolean isFriend(User user, User loggedUser) {
		for (User friend : user.getFriends()) {
			if (friend.equals(loggedUser))
				return true;
		}
		return false;
	}

}
