package com.lufin.server.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MemberStatus {

    @Column(name = "credit_rating", nullable = false)
    private Integer creditRating;

    /**
     * 신용 상태
     * 0: 정상, 1: 신용 불량자
     */
    @Column(name = "status", nullable = false)
    private Byte status = 0;

    @Column(name = "status_description", columnDefinition = "TEXT")
    private String statusDescription;

    public MemberStatus() {
        this.creditRating = 60;
        this.status = 0;
        this.statusDescription = null;
    }

    public void updateStatus(Byte status, String description) {
        this.status = status;
        this.statusDescription = description;
    }
}
