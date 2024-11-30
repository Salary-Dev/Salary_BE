package salary_BE.salary.Event;

import salary_BE.salary.Domain.User;

public class UserCreatedEvent {

    private final User user;

    public UserCreatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}