package com.ross.bugtracker.service;

import com.ross.bugtracker.model.*;
import com.ross.bugtracker.repository.BugRepository;
import com.ross.bugtracker.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
public class BugService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BugRepository bugRepository;

    @PostConstruct
    public void createBugs() {
        bugRepository.deleteAll();
        bugRepository.save(Bug.builder().assignedDeveloper("Vlad").title("Bug - 1").priority(Priority.HIGH).finderName("John").description("Quisque lacinia pellentesque libero, in condimentum enim. Proin pellentesque neque at massa fringilla, ac dapibus lorem pulvinar. Ut lobortis volutpat est id posuere. Quisque eleifend odio quis malesuada facilisis. Suspendisse placerat, velit eu rutrum accumsan, urna turpis tempus dolor, ac lobortis lectus dolor ut tortor. Proin ac egestas nibh. Nam non felis quis mauris lobortis sagittis vel vel enim.").build());
        bugRepository.save(Bug.builder().assignedDeveloper("Jane Doe").title("Bug - 2").priority(Priority.MEDIUM).finderName("James").description("Quisque lacinia pellentesque libero, in condimentum enim. Proin pellentesque neque at massa fringilla, ac dapibus lorem pulvinar. Ut lobortis volutpat est id posuere. Quisque eleifend odio quis malesuada facilisis. Suspendisse placerat, velit eu rutrum accumsan, urna turpis tempus dolor, ac lobortis lectus dolor ut tortor. Proin ac egestas nibh. Nam non felis quis mauris lobortis sagittis vel vel enim.").build());

        Bug bug = Bug.builder().title("Bug - 3").priority(Priority.LOW).finderName("Bob").assignedDeveloper("John Smith").description("Quisque lacinia pellentesque libero, in condimentum enim. Proin pellentesque neque at massa fringilla, ac dapibus lorem pulvinar. Ut lobortis volutpat est id posuere. Quisque eleifend odio quis malesuada facilisis. Suspendisse placerat, velit eu rutrum accumsan, urna turpis tempus dolor, ac lobortis lectus dolor ut tortor. Proin ac egestas nibh. Nam non felis quis mauris lobortis sagittis vel vel enim.").build();
        Comment comment = Comment.builder().author("Ross MacLean").comment("This is a good story, I like it.").creationDate(Date.from(Instant.now())).build();
        Comment comment2 = Comment.builder().author("John Doe").comment("Remember to take the scope if the project into account when sizing this.").creationDate(Date.from(Instant.now())).build();
        bug.setComments(Arrays.asList(comment, comment2));
        bugRepository.save(bug);

        userCredentialRepository.deleteAll();
//
        userCredentialRepository.save(UserCredentials.builder().fullName("Ross Mac").userName("ross").encryptedPassword(passwordEncoder.encode("pass")).role(Role.ADMIN).build());
    }
}
