package com.seojey.domain.service.form;

import com.google.common.collect.Maps;
import com.seojey.domain.db.model.obj.RecommendProduct;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by kimjun on 2016. 5. 16..
 */
@Data
public class RecommendDTO {

    @NotNull
    private Map<String, List<RecommendProduct>> data; // String : 서비스 이름, List : 해당 서비스 별 추천 상품 리스트
    private String tagContext; //tagContext="alg=개인탑100기본"
    @NotNull
    private Boolean cachePossibleFlag = Boolean.FALSE;  //cache 의 가능 여부
    private List <String> cacheKeys;                    // cachePossibleFlag ; false 시 N/A


    public RecommendDTO() {
        this(Maps.<String, List<RecommendProduct>>newHashMap(), "");
    }

    public RecommendDTO(Map<String, List<RecommendProduct>> data) {
        this(data, "");
    }

    public RecommendDTO(Map<String, List<RecommendProduct>> data, String tagContext) {
        this.data = data;
        this.tagContext = tagContext;
    }
}
