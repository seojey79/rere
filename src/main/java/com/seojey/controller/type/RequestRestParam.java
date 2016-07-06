package com.seojey.controller.type;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by kimjun on 16. 5. 25..
 */
@Data
public class RequestRestParam {

    private List<Long> productIds;      // 사용자 제공 상품 리스트
    private Long memberId;                         // 없거나 오류  경우 0L, 0L인 경우 warm up URL 로 파악하여 처리
    @NotNull
    private String uid = "";                             // 사용자 platform ID
    @NotNull
    private String platform;
    /**
     * it use only special case (like keyword recommendation.)
     * must need pre-decision about context key & value
     *
     */
    private String context;            // 사용자 제공 특수 정보
    private int limits;                             // 수신 상품 리스트의 최대 개수

}
