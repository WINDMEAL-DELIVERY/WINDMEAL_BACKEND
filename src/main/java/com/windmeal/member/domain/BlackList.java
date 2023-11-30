package com.windmeal.member.domain;

import com.windmeal.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlackList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "black_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id", updatable = false, referencedColumnName = "member_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "black_member_id", updatable = false, referencedColumnName = "member_id")
    private Member blacked;

    public static BlackList place(Member requester, Member blacked) {
        return new BlackList(requester,blacked);
    }

    private BlackList(Member requester, Member blacked) {
        this.requester = requester;
        this.blacked = blacked;
    }
}
