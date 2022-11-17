package com.cmpe275.term.model;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.EventStatus;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class OrganizerReport {
    private Integer totalEventsCreated;
    private Integer totalPaidEvents;
    private Integer totalCancelledEvents;
    private Integer totalMinimumParticipantsRequiredInCancelledEvents;
    private Integer totalParticipationRequests;
    private Integer totalParticipantsInFinishedEvents;
    private Integer totalFinishedEventsCreatedByThisUser;
    private Integer totalPaidEventThatFinished;
    private Integer totalRevenue;
}
