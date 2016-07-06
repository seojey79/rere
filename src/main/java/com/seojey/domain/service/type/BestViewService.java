package com.seojey.domain.service.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.seojey.domain.db.dynamoDb.template.StatisticRecommendTemplate;
import com.seojey.domain.db.model.obj.RecommendProduct;
import com.seojey.domain.db.model.table.StatisticRecommend;
import com.seojey.domain.service.form.RecommendDTO;
import com.seojey.domain.service.form.RequestParam;
import com.seojey.domain.service.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kimjun on 16. 6. 8..
 */

@Service
public class BestViewService extends RecommendService {
    @Autowired
    private StatisticRecommendTemplate tmp;
    private static final String keyName = ServiceType.BEST_VIEW_RANKING.getDesc();

    public BestViewService() {
        serviceName = ServiceType.BEST_VIEW_RANKING.getDesc();
    }

    /**
     *
     * TODO 해당 뷰의 단의 별로 어떤 데이터를 가져다 쓸것인지 context 의 내용으로 설정해서 가져옴.
     * @param param
     * @return
     */
    @Override
    public RecommendDTO getRecommendDTO(RequestParam param) {

        List<RecommendProduct> preferredProducts = Lists.newArrayList();

        StatisticRecommend rec = tmp.getByKey(keyName);

        if (rec.getRecommendProducts() != null) {
            preferredProducts.addAll(rec.getRecommendProducts());
        }

        ProductUtil.removeProduct(preferredProducts, param.getProductIds());
        ProductUtil.sortRecommendProduct(preferredProducts);

        return new RecommendDTO(new HashMap<>(
                ImmutableMap.of(serviceName, preferredProducts)));
    }
}
