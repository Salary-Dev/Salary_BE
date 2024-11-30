package salary_BE.salary.Listener;

import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Event.UserCreatedEvent;

@Component
public class UserEntityListener {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostPersist
    public void afterUserCreated(User user) {
        System.out.println("UserEntityListener 호출됨 - User ID: " + user.getId());
        eventPublisher.publishEvent(new UserCreatedEvent(user));
    }
}
