package com.windmeal.chat.repository;

import com.windmeal.chat.domain.ChatroomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatroomDocumentRepository extends MongoRepository<ChatroomDocument, String>,
    QuerydslPredicateExecutor<ChatroomDocument> {

}
