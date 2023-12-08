package com.windmeal.chat.repository;

import com.windmeal.chat.domain.MessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String>, MessageDocumentCustomRepository {

}
