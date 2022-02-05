package com.ssafy.ssafit.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

/**
 * ExerciseLog 테이블도 PK 존재하게 만들었음.
 */

@Getter
@Setter
@Entity
public class ExerciseLog extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private User user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Exercise_ID")
    private Exercise ex_id;

    private int ex_count;   // 해당 운동 총 갯수

    private int ex_time; // 해당 운동 총 시간
}