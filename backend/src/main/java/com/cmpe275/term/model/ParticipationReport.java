package com.cmpe275.term.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ParticipationReport {
    private Integer totalSignupEvents;
    private Integer totalApprovals;
    private Integer totalRejects;
    private Integer totalFinishedEvents;

}
