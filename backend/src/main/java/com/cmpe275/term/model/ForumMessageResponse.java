package com.cmpe275.term.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumMessageResponse {
    private Long id;
    private String sentBy;
    private String message;
    private boolean eventOwner;
    private String imageURL;
    private String messageDateTime;

}
