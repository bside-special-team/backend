package com.beside.special.service.dto;

import com.beside.special.domain.BlockType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlockDto {
    private String targetId;
    private BlockType type;
}
