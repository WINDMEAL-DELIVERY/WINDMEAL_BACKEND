package com.windmeal.order.service;

import com.windmeal.generic.domain.Money;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.BlackListRepository;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderDeleteRequest;
import com.windmeal.order.dto.response.OrderCreateResponse;
import com.windmeal.order.dto.response.OrderDetailResponse;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.order.dto.response.OwnOrderListResponse;
import com.windmeal.order.exception.InvalidPlaceNameException;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.exception.OrdererMissMatchException;
import com.windmeal.order.mapper.OrderRequestMapper;
import com.windmeal.order.repository.order.OrderRepository;
import com.windmeal.order.validator.OrderValidator;
import com.windmeal.store.domain.Store;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.StoreRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderValidator orderValidator;
  private final MemberRepository memberRepository;
  private final OrderRepository orderRepository;
  private final OrderRequestMapper orderRequestMapper;
  private final PlaceRepository placeRepository;
  private final StoreRepository storeRepository;

  private final BlackListRepository blackListRepository;
  @CacheEvict(value = "Orders", allEntries = true, cacheManager = "contentCacheManager") //데이터 삭제
  @Transactional
  public OrderCreateResponse createOrder(OrderCreateRequest request) {
    memberRepository.findById(request.getMemberId())
        .orElseThrow(MemberNotFoundException::new);

    orderValidator.validate(request);
    // 이름으로만 검색하도록 변경 : 도착지로만 쓰는거니까 상관이 없다.
//    Place place = placeRepository.findByNameAndLongitudeAndLatitude(request.getPlaceName(),request.getLongitude(),request.getLatitude())
//        .orElseGet(() -> placeRepository.save(request.toPlaceEntity()));
    if(placeRepository.existsByName(request.getPlaceName())) {
      throw new InvalidPlaceNameException();
    }
    Place place = placeRepository.findByName(request.getPlaceName())
        .orElseGet(() -> placeRepository.save(request.toPlaceEntity()));
    Order order = orderRequestMapper.mapFrom(request);
    Money totalPrice = calculateTotalPrice(request);
    String summary = getSummary(totalPrice, request.getMenus().get(0).getName(), calculateTotalSize(request));
    order.place(totalPrice,summary,place);
    Order savedOrder = orderRepository.save(order);
    return OrderCreateResponse.toResponse(savedOrder,place);
  }


  // 배달이 성사되지 않은 ORDERED 상태의 주문 요청만 삭제 가능.
  @Transactional
  public void deleteOrder(OrderDeleteRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException());

    if(!order.getOrderStatus().equals(OrderStatus.ORDERED)){
      throw new OrderAlreadyMatchedException("이미 배달 요청이 성사된 주문은 삭제할 수 없습니다.");
    }

    if(!order.getOrderer_id().equals(request.getMemberId())){
      throw new OrdererMissMatchException();
    }

    orderRepository.deleteById(request.getOrderId());
  }


//  @Cacheable(value = "Orders", key = "#pageable.pageNumber", cacheManager = "contentCacheManager",
//      condition = "#storeId == null&&#pageable.pageNumber==0"
//          + "&&#eta==null&&#storeCategory==null&&#placeId==null")
  public RestSlice<OrderListResponse> getAllOrder(Pageable pageable, Long storeId, String eta, String storeCategory,
      Long placeId, Long memberId) {
    return orderRepository.getOrderList(pageable,storeId,eta,storeCategory,placeId,memberId);
  }

  /**
   * 지도에 보여줄 데이터 조회
   * 주문이 추가된 경우, 배달이 성사된 경우 캐시 초기화
   * @param storeId
   * @param eta
   * @param storeCategory
   * @param placeId
   * @return
   */
  @Cacheable(value = "Orders", key = "0",cacheManager = "contentCacheManager",
            condition = "#storeId == null&&#eta==null&&#storeCategory==null&&#placeId==null")
  public List<OrderMapListResponse> getAllOrdersForMap(Long storeId, String eta, String storeCategory,
      Long placeId, OrderStatus orderStatus) {
    return orderRepository.getOrderMapList(storeId,eta,storeCategory,placeId,orderStatus);
  }
  /**
   * 주문 상세 내용 조회
   * @param orderId
   * @return
   */
  public OrderDetailResponse getOrderDetail(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(OrderNotFoundException::new);
    Store store = storeRepository.findById(order.getStore_id())
        .orElseThrow(StoreNotFoundException::new);

    return OrderDetailResponse.from(order, store);
  }


  public int calculateTotalSize(OrderCreateRequest request) {
    return request.getMenus().stream().mapToInt(menus->menus.getCount()).sum();
  }

  public String getSummary(Money totalPrice, String menuName, int menuCount) {
    return menuCount == 1 ?
        String.format("%s %s원", menuName, totalPrice.wons()) :
        String.format("%s 외 %s개 %s원", menuName, menuCount - 1, totalPrice.wons());
  }

  public Money calculateTotalPrice(OrderCreateRequest request) {
      return Money.sum(request.getMenus(), OrderMenuRequest::calculatePrice);
  }



  private static Long getMenuCount(OrderCreateRequest request) {
    return request.getMenus().stream().count();
  }


  public RestSlice<OrderListResponse> removeBlackMemberOrder(
      RestSlice<OrderListResponse> allOrder, Long memberId) {

    List<Long> requesterIds = allOrder.getContent().stream().map(OrderListResponse::getMemberId)
        .collect(Collectors.toList());

    List<Long> blackOrderIds = blackListRepository.getBlackListByBlackMemberAndRequesterIn(
        memberId, requesterIds);

    for (Long blackOrderId : blackOrderIds) {
      System.out.println("blackOrderId = " + blackOrderId);
    }
    List<OrderListResponse> result = allOrder.getContent().stream()
        .filter(orderListResponse -> !blackOrderIds.contains(orderListResponse.getMemberId())).collect(
            Collectors.toList());

    return new RestSlice(result, allOrder.getNumber(), allOrder.getSize());
  }

  public int getOwnOrderedTotalPrice(Long memberId) {
    return orderRepository.getOwnOrderedTotalPrice(memberId);
  }

  public Slice<OwnOrderListResponse> getOwnOrdered(Long memberId, Pageable pageable, LocalDate startDate,
      LocalDate endDate, String storeCategory) {
    return orderRepository.getOwnOrdered(memberId,pageable,startDate,endDate,storeCategory);
  }
}
