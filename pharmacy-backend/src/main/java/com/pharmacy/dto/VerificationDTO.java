package com.pharmacy.dto;

import lombok.Data;

@Data
public class VerificationDTO {
    private Long pharmacistId;
    private String rejectionReason;
}
