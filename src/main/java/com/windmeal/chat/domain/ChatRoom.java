package com.windmeal.chat.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    // 1대1 채팅만 지원할 예정. 따라서 2명의 사용자를 필드로 가지고 있겠다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member guest;

    @Builder
    public ChatRoom(Long id, Member owner, Member guest) {
        this.id = id;
        this.owner = owner;
        this.guest = guest;
    }
}
