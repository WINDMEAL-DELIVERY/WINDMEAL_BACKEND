package com.windmeal.chat.repository;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.store.repository.CategoryCustomRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {

    /*
        아이디로 조회할때, 사용자 엔티티의 지연로딩으로 인한 N+1을 방지하기 위한 쿼리
        findById에 @EntityGraph를 붙여서 쓰면 되는것 아닌가? -> 그럼 앞으로 기본키로 채팅방을 조회할 경우 반드시 멤버들을 조인해서 같이 들고온다.
        n+1이 발생할 수 있는 상황에서 선택적으로 사용할 수 있도록 별도로 jpql 작성
     */

    @Query(value = "SELECT c FROM ChatRoom c JOIN FETCH c.owner JOIN FETCH c.guest where c.id = :id ")
    ChatRoom findByIdWithFetchJoin(@Param("id") Long id);
}
