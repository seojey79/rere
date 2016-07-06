package com.seojey.domain.service.type;

import org.springframework.util.StringUtils;

/**
 * Created by kimjun on 16. 5. 20..
 *  - Service Classify
 *  - format {return value}_{build criteria} (call name)
 */
public enum ServiceType {

    PREFERRED_PRODUCTS_BY_REVIEW("your_favor_products"),
    PREFERRED_PRODUCTS_BY_REVIEW_WITH_STD_CATE("your_favor_products(cate)"),
    PREFERRED_PRODUCTS_BY_REVIEW_WITH_SKIN("your_favor_products(skin)"),

    SIMILAR_INGREDIENT_PRODUCTS_WITH_CATE("similar_ingredient_products(cate)"),
    SIMILAR_INGREDIENT_PRODUCTS_WITH_SKIN("similar_ingredient_products(skin)"),

    BEST_VIEW_RANKING("best_view"),

    UNDEFINED("UNDEFINED");

    private String desc;

    /**
     * @param desc  추천 서비스 명 정의, 서비스 작성 시에 확인.
     */

    private ServiceType(String desc) {
        this.desc = desc;
    }

    public static ServiceType getType(String desc) {
        if (StringUtils.isEmpty(desc)) {
            return UNDEFINED;
        }

        for (ServiceType type : ServiceType.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }

        return UNDEFINED;
    }


    public String getDesc() {
        return this.desc;
    }

}
