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


    private String token;
    private String name;
    private String nickname;
    private String department;
    private String profileImage;

    @Builder
    public Member(Long id, String email, Authority authority, String name, String nickname, String department, String profileImage) {
        super(id, email, authority);
        this.name = name;
        this.nickname = nickname;
        this.department = department;
        this.profileImage = profileImage;
    }

    @Builder
    public Member(Long id) {
        super(id);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
