package com.seojey.domain.service.type;

import com.seojey.controller.type.RequestRestParam;
import com.seojey.domain.db.dynamoDb.template.StatisticRecommendTemplate;
import com.seojey.domain.service.form.RecommendDTO;
import com.seojey.domain.service.form.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by kimjun on 16. 5. 25..
 */
@Service
public abstract class RecommendService {

    @Autowired
    StatisticRecommendTemplate template;
    String serviceName = ServiceType.BEST_VIEW_RANKING.getDesc();

    public final RequestParam getRecommendParam(String serviceType, RequestRestParam param) {
        return new RequestParam(serviceType, param);
    }

    public abstract RecommendDTO getRecommendDTO(RequestParam param);


}
