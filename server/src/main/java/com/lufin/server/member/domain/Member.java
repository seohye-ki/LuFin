package com.lufin.server.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "certification_number")
    private String certificationNumber;

    @Embedded
    private MemberAuth auth;

    @Embedded
    private MemberStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public static Member createStudent(String email, String name, String password, String secondary_password) {
        return create(email, name, password, secondary_password, MemberRole.STUDENT);
    }

    public static Member createTeacher(String email, String name, String password, String secondary_password) {
        return create(email, name, password, secondary_password, MemberRole.TEACHER);
    }

    private static Member create(String email, String name, String password, String secondary_password, MemberRole role) {
        Member member = new Member();
        member.email = email;
        member.name = name;
        member.memberRole = role;
        member.auth = new MemberAuth(password, secondary_password);
        member.status = new MemberStatus();
        return member;
    }

    public void updateStatus(Byte status, String description) {
        this.status.updateStatus(status, description);
    }

    public void updateLastLogin() {
        this.auth.updateLastLogin();
    }
}
