package com.cmpe275.term.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ReportRequest {
    private Long userId;
    private String sysDate;
}
