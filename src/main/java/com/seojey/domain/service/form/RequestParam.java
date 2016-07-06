package com.seojey.domain.service.form;

import com.seojey.controller.type.RequestRestParam;
import com.seojey.domain.service.type.ServiceType;
import com.seojey.domain.type.Platform;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
/**
 * Created by kimjun on 2016. 5. 16..
 */
@Data
public class RequestParam {

    private final static int MAX_LIMITS = 50;

    @NotNull
    private ServiceType service = ServiceType.UNDEFINED;                     // 추천 Service
    private List<Long> productIds;      // 사용자 제공 상품 리스트
    private Long memberId;                         // 없거나 오류  경우 0L, 0L인 경우 warm up URL 로 파악하여 처리
    @NotNull
    private String uid = "";                             // 사용자 platform ID
    @NotNull
    private Platform platform;
    /**
     * it use only special case (like keyword recommendation.)
     * must need pre-decision about context key & value
     *
     */
    private Map<String, String> context;            // 사용자 제공 특수 정보
    private int limits;                             // 수신 상품 리스트의 최대 개수

    public RequestParam(String serviceName,
                        RequestRestParam param) {
        if (!StringUtils.isEmpty(serviceName)) {
            this.service = ServiceType.getType(serviceName);
        }

        this.productIds = param.getProductIds();
        this.memberId = param.getMemberId();
        if (!StringUtils.isEmpty(param.getUid())) {
            this.uid = param.getUid();
        }

        this.platform = Platform.getPlatform(param.getPlatform());
        if (limits < 0 || limits > 50) {
            this.limits = MAX_LIMITS;
        } else {
            this.limits = param.getLimits();
        }

    }



}
