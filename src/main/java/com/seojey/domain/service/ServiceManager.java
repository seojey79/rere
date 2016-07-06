package com.seojey.domain.service;

import com.google.common.collect.Maps;
import com.seojey.controller.type.RequestRestParam;
import com.seojey.domain.service.form.RecommendDTO;
import com.seojey.domain.service.form.RequestParam;
import com.seojey.domain.service.type.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by kimjun on 16. 5. 25..
 */
@Service
public class ServiceManager {

    private HashMap<String, RecommendService> serviceCase = Maps.newHashMap();

    public ServiceManager() {
        /**
         * TODO ServiceType class 읽어서 등록하도록 수정 필요
         */
        serviceCase.put(ServiceType.PREFERRED_PRODUCTS_BY_REVIEW.getDesc(), new PreferredProductsService());
        serviceCase.put(ServiceType.PREFERRED_PRODUCTS_BY_REVIEW_WITH_SKIN.getDesc(), new PreferredProductsSkinService());
        serviceCase.put(ServiceType.PREFERRED_PRODUCTS_BY_REVIEW_WITH_STD_CATE.getDesc(), new PreferredProductsStdCateService());
        serviceCase.put(ServiceType.SIMILAR_INGREDIENT_PRODUCTS_WITH_CATE.getDesc(), new SimilarProductsCateService());
        serviceCase.put(ServiceType.SIMILAR_INGREDIENT_PRODUCTS_WITH_SKIN.getDesc(), new SimilarProductsSkinService());
        serviceCase.put(ServiceType.BEST_VIEW_RANKING.getDesc(), new BestViewService());
    }

    public RecommendDTO recommend (String serviceType, RequestRestParam param) {
        RecommendService service = serviceCase.get(serviceType);

        if (service == null) {
            return new RecommendDTO();
        }

        RequestParam requestParam = service.getRecommendParam(serviceType, param);
        return service.getRecommendDTO(requestParam);
    }
}
