package com.cmpe275.term.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumMessageRequest {

    private String message;
    private String sysDate;
    private String imageURL;
    private Long userId;
    private Long eventId;


}
