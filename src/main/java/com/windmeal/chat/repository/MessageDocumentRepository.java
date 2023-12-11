package com.windmeal.chat.repository;

import com.windmeal.chat.domain.MessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String>,
    MessageDocumentCustomRepository, QuerydslPredicateExecutor<MessageDocument> {


  Slice<MessageDocument> findByChatroomId(Long chatroomId, Pageable pageable);
}
