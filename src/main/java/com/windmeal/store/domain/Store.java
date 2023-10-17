package com.windmeal.store.domain;

import com.windmeal.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    private String name;

    private String phoneNumber;

    private String photo;
    private LocalDateTime openTime;

    private LocalDateTime closeTime;
    @Column( columnDefinition = "Point")
    private Point location;


    @Builder

    public Store(Member owner, String name, String phoneNumber, String photo, LocalDateTime openTime, LocalDateTime closeTime, Point location) {
        this.owner = owner;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.location = location;
    }
}
