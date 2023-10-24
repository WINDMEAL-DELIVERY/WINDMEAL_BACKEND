package com.windmeal.store.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.time.LocalTime;

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
  private LocalTime openTime;

  private LocalTime closeTime;

  private Point location;

  @OneToMany(mappedBy = "store")
  private List<StoreCategory> storeCategoryList = new ArrayList<>();

  @Builder
  public Store(Member owner, String name, String phoneNumber, String photo, LocalTime openTime,
      LocalTime closeTime, Point location) {
    this.owner = owner;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.photo = photo;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.location = location;
  }

  public void updatePhoto(String updatePhoto) {
    this.photo = updatePhoto;
  }

  public void updateInfo(StoreUpdateRequest updateRequest) {
    this.name = updateRequest.getName();
    this.phoneNumber = updateRequest.getPhoneNumber();
    this.closeTime = updateRequest.getCloseTime();
    this.openTime = updateRequest.getOpenTime();
    this.location = new Point(updateRequest.getLatitude(), updateRequest.getLongitude());

  }
}
