package com.ross.bugtracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Bug {

    @Id
    private String id;
    private String title;
    private Date creationDate = Date.from(Instant.now());
    private String finderName;
    private String assignedDeveloper;
    private Priority priority;
    private Status status = Status.OPEN;
    private String description;
    private List<Comment> comments;
}
