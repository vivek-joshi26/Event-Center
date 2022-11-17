package com.cmpe275.term.model;

import com.cmpe275.term.entity.ApprovalStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantApprovalRequest {
    //private Long eventId;
    private Long participantId;
    private ApprovalStatus approvalStatus;
    private String sysDate;
}
