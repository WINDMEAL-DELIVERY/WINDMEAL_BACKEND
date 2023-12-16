package com.windmeal.report.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseTimeEntity;
import com.windmeal.model.event.EventPublisher;
import com.windmeal.order.domain.event.DeliveryMatchEvent;
import com.windmeal.report.domain.event.ReportEvent;
import com.windmeal.report.dto.request.ReportCreateRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id", updatable = false, referencedColumnName = "member_id")
    private Member reporter;

    private String title;
    private String content;


    public static Report place(Member reporter, ReportCreateRequest request) {

        EventPublisher.publish(new ReportEvent(request.getTitle(), reporter.getNickname()));

        return Report.builder()
            .reporter(reporter)
            .title(request.getTitle())
            .content(request.getContent())
            .build();
    }
    @Builder
    public Report(Member reporter, String title, String content) {
        this.reporter = reporter;
        this.title = title;
        this.content = content;
    }
}
