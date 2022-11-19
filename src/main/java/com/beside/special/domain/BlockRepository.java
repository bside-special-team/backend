package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlockRepository  extends MongoRepository<BlockInfo, String> {
    List<BlockInfo> findByUserIdAndType(String userId, BlockType type);

    BlockInfo findByUserIdAndTargetId(String userId, String targetId);
}