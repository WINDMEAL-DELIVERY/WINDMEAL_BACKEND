package com.windmeal.member.domain;


import com.windmeal.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends MemberBase{

    private String name;
    private String nickname;
    private String department; //학과

    @Builder
    public Member(String email, String password, Authority authority, String name, String nickname, String department) {
        super(email, password, authority);
        this.name = name;
        this.nickname = nickname;
        this.department = department;
    }

}
