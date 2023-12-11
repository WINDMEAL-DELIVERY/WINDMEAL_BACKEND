//package com.windmeal.chat.domain;
//
//import com.windmeal.member.domain.Member;
//import com.windmeal.model.BaseEntity;
//import com.windmeal.order.domain.Order;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ChatRoom extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chatroom_id")
//    private Long id;
//    // 1대1 채팅만 지원할 예정. 따라서 2명의 사용자를 필드로 가지고 있겠다.
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "owner_id")
//    private Member owner;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "guest_id")
//    private Member guest;
//
//    // 어떤 주문(게시글)에 대한 채팅방인가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="order_id")
//    private Order order;
//
//    private boolean isDeletedByOwner = false;
//    private boolean isDeletedByGuest = false;
//
//    @Builder
//    public ChatRoom(Long id, Member owner, Member guest, Order order, boolean isDeletedByOwner, boolean isDeletedByGuest) {
//        this.id = id;
//        this.owner = owner;
//        this.guest = guest;
//        this.order = order;
//        this.isDeletedByOwner = isDeletedByOwner;
//        this.isDeletedByGuest = isDeletedByGuest;
//    }
//
//    public void updateDeletedByOwner(boolean isDeletedByOwner) {
//        this.isDeletedByOwner = isDeletedByOwner;
//    }
//
//    public void updateDeletedByGuest(boolean isDeletedByGuest) {
//        this.isDeletedByGuest = isDeletedByGuest;
//    }
//}
