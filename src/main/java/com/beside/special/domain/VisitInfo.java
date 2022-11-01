package com.beside.special.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
// User Entity contains Place ID
// Place Entity contains User ID
public class VisitInfo{
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime visitedAt;

    @Override
    public boolean equals(Object obj){
        if(getClass() != obj.getClass()){
            return false;
        }
        return ( ((VisitInfo) obj).id.equals(this.id));
    }

    @Override
    public int hashCode(){
        return (id).hashCode();
    }
}
