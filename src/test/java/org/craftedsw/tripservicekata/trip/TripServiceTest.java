package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TripServiceTest {
    TripService subject;
    FakeTripDAO tripDAO;
    FakeUserSession fakeUserSession;

    @BeforeEach
    public void setup() {
        tripDAO = new FakeTripDAO();
        subject = new TripService(tripDAO);
        fakeUserSession = new FakeUserSession();
    }
	@Test
    public void findUserTripsWhenLoggedInUserIsFriend() {
        fakeUserSession.loggedUser = new User();

        User user = new User();
        user.addFriend(fakeUserSession.getLoggedUser());

        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        List<Trip> result = subject.getTripsByUser(user, fakeUserSession);

        assertEquals(1, result.size());
        assertSame(trip, result.get(0));
        assertSame(user, tripDAO.passedUser);
    }

    @Test
    public void findNoUserTripsWhenLoggedInUserIsNotFriend() {
        fakeUserSession.loggedUser = new User();

        User user = new User();

        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        List<Trip> result = subject.getTripsByUser(user, fakeUserSession);

        assertEquals(0, result.size());
    }

    @Test
    public void throwsExceptionWhenNoLoggedInUser() {
        User user = new User();
        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        assertThrows(UserNotLoggedInException.class, () -> {
            subject.getTripsByUser(user, fakeUserSession);
        });
    }


}
