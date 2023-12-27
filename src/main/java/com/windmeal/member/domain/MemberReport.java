package com.windmeal.member.domain;

import com.windmeal.member.dto.request.MemberReportCreateRequest;
import com.windmeal.model.BaseTimeEntity;
import com.windmeal.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id", updatable = false, referencedColumnName = "member_id")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id", updatable = false, referencedColumnName = "member_id")
    private Member reported;

    private String title;
    private String content;


    public static MemberReport place(MemberReportCreateRequest request, Member reported, Member reporter){
        return new MemberReport(reporter,reported,request.getTitle(), request.getContent());
    }
    private MemberReport(Member reporter, Member reported, String title, String content) {
        this.reporter = reporter;
        this.reported = reported;
        this.title = title;
        this.content = content;
    }
}
