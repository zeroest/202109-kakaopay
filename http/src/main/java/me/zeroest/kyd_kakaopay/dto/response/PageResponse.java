package me.zeroest.kyd_kakaopay.dto.response;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PageResponse<T> {

    public PageResponse(List<T> list, Long totalCount){
        this.list = list;
        this.totalCount = totalCount;
    }

    private Long totalCount;

    private List<T> list;

}
