package com.cmpe275.term.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserReportResponse {
    private ParticipationReport participationReport;
    private OrganizerReport organizerReport;
}
