package com.cmpe275.term.service;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.model.EventRequest;
import com.cmpe275.term.model.ParticipantApprovalRequest;
import com.cmpe275.term.model.ParticipateRequest;
import org.springframework.http.ResponseEntity;

public interface ParticipantService {

    public ResponseEntity registerParticipant(ParticipateRequest participateRequest) throws Exception;

    public ResponseEntity updateApprovalStatus(ParticipantApprovalRequest request) throws Exception;

    public ResponseEntity findPendingApproval(Long eventId, ApprovalStatus approvalStatus) throws Exception;

}
