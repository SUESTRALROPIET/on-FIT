package com.ssafy.ssafit.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * ERD에서
 * ChatMessage 테이블 user : varchar -> int
 */

@Getter
@Setter
@Entity
public class ChatMessage extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Club_ID")
    private Club clubId;

    private String message;

    private LocalDateTime time;
}

// QueryDsl(JPA처럼 함수형태) vs JPQL(sql문 명시)
