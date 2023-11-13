package com.windmeal.chat.repository;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.store.repository.CategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {


}
