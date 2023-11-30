package com.windmeal.store.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.model.place.Place;
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


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  @OneToMany(mappedBy = "store")
  private List<StoreCategory> storeCategoryList = new ArrayList<>();



  @Builder
  public Store(Member owner, String name, String phoneNumber, String photo, LocalTime openTime,
      LocalTime closeTime, Place place) {
    this.owner = owner;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.photo = photo;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.place = place;
  }

  @Builder
  public Store(Long id) {
    this.id = id;
  }

  public boolean isOpen(){
    LocalTime now = LocalTime.now();

    if (openTime == null || closeTime == null) {
        return true;
      }
      if (now.isBefore(openTime) || now.isAfter(closeTime)) {
        return false;
      }

      return true;

  }
  public void updatePhoto(String updatePhoto) {
    this.photo = updatePhoto;
  }

  public void updateInfo(StoreUpdateRequest updateRequest,Place place) {
    this.name = updateRequest.getName();
    this.phoneNumber = updateRequest.getPhoneNumber();
    this.closeTime = updateRequest.getCloseTime();
    this.openTime = updateRequest.getOpenTime();
    this.place = place;

  }
}
