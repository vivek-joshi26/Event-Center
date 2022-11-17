package com.cmpe275.term.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class SystemReportResponse {
    private Integer totalEvents;
    private Integer totalPaidEvents;
    private Integer totalCancelledEvents;
    private Integer totalMinimumParticipantsInCancelledEvents;
    private Integer totalParticipantRequestForCancelledEvents;
    private Integer totalFinishedEvents;
    private Integer totalParticipantsInFinishedEvents;

}
