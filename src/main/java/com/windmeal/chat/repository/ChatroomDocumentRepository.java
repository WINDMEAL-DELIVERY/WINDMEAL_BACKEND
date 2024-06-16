package com.windmeal.chat.repository;

import com.windmeal.chat.domain.ChatroomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatroomDocumentRepository extends MongoRepository<ChatroomDocument, String> {

    ChatroomDocument findByOrderIdAndGuestId(Long orderId, Long guestId);
}
