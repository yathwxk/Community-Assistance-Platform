package com.helpdesk.config;

import com.helpdesk.entity.*;
import com.helpdesk.repository.RequestRepository;
import com.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default users if none exist
        if (userRepository.count() == 0) {
            // Create diverse community members
            User alice = new User();
            alice.setName("Alice Johnson");
            alice.setEmail("alice@example.com");
            alice.setPassword(passwordEncoder.encode("password"));
            alice.setRole(UserRole.RESIDENT);
            alice.setRating(4.5);

            User bob = new User();
            bob.setName("Bob Smith");
            bob.setEmail("bob@example.com");
            bob.setPassword(passwordEncoder.encode("password"));
            bob.setRole(UserRole.VOLUNTEER);
            bob.setRating(4.8);

            User carol = new User();
            carol.setName("Carol Davis");
            carol.setEmail("carol@example.com");
            carol.setPassword(passwordEncoder.encode("password"));
            carol.setRole(UserRole.VOLUNTEER);
            carol.setRating(4.9);

            User david = new User();
            david.setName("David Wilson");
            david.setEmail("david@example.com");
            david.setPassword(passwordEncoder.encode("password"));
            david.setRole(UserRole.RESIDENT);
            david.setRating(4.2);

            User emma = new User();
            emma.setName("Emma Thompson");
            emma.setEmail("emma@example.com");
            emma.setPassword(passwordEncoder.encode("password"));
            emma.setRole(UserRole.VOLUNTEER);
            emma.setRating(4.7);

            User frank = new User();
            frank.setName("Frank Miller");
            frank.setEmail("frank@example.com");
            frank.setPassword(passwordEncoder.encode("password"));
            frank.setRole(UserRole.RESIDENT);
            frank.setRating(3.8);

            // Save all users
            userRepository.save(alice);
            userRepository.save(bob);
            userRepository.save(carol);
            userRepository.save(david);
            userRepository.save(emma);
            userRepository.save(frank);

            // Create diverse sample requests
            Request request1 = new Request();
            request1.setUser(alice);
            request1.setTitle("Need a ladder for 2 hours");
            request1.setDescription("I need to clean my gutters and don't have a tall enough ladder. Would need it for about 2 hours this weekend.");
            request1.setCategory(RequestCategory.TOOLS);
            request1.setUrgency(RequestUrgency.MEDIUM);
            request1.setStatus(RequestStatus.OPEN);

            Request request2 = new Request();
            request2.setUser(david);
            request2.setTitle("Help with math tutoring for my child");
            request2.setDescription("My 8th grader is struggling with algebra. Looking for someone who can help with homework 2-3 times a week.");
            request2.setCategory(RequestCategory.TUTORING);
            request2.setUrgency(RequestUrgency.HIGH);
            request2.setStatus(RequestStatus.OPEN);

            Request request3 = new Request();
            request3.setUser(frank);
            request3.setTitle("Need help carrying groceries upstairs");
            request3.setDescription("I live on the 3rd floor and have some heavy grocery bags. Would appreciate help carrying them up.");
            request3.setCategory(RequestCategory.HOUSEHOLD);
            request3.setUrgency(RequestUrgency.LOW);
            request3.setStatus(RequestStatus.OPEN);

            Request request4 = new Request();
            request4.setUser(alice);
            request4.setTitle("Computer help - virus removal");
            request4.setDescription("My laptop is running very slowly and I think it might have a virus. Need someone tech-savvy to help clean it up.");
            request4.setCategory(RequestCategory.TECHNOLOGY);
            request4.setUrgency(RequestUrgency.MEDIUM);
            request4.setStatus(RequestStatus.COMPLETED);

            Request request5 = new Request();
            request5.setUser(david);
            request5.setTitle("Garden weeding help needed");
            request5.setDescription("My backyard garden has gotten overgrown with weeds. Looking for someone to help me clear it out this weekend.");
            request5.setCategory(RequestCategory.GARDENING);
            request5.setUrgency(RequestUrgency.LOW);
            request5.setStatus(RequestStatus.ACCEPTED);

            requestRepository.save(request1);
            requestRepository.save(request2);
            requestRepository.save(request3);
            requestRepository.save(request4);
            requestRepository.save(request5);

            System.out.println("Created 6 diverse community members and 5 sample requests for testing");
        }
    }
}
