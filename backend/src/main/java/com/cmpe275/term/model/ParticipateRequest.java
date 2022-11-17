package com.cmpe275.term.model;

import com.cmpe275.term.entity.ApprovalStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateRequest {

    private Long userId;
    private Long eventId;
    private ApprovalStatus approvalStatus;
    private String sysDate;

}
