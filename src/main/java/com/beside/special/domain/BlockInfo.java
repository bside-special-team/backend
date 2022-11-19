package com.beside.special.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Getter
@Setter
public class BlockInfo {
    private String userId;
    private String targetId;
    private BlockType type;
}