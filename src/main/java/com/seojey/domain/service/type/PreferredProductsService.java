package com.seojey.domain.service.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.seojey.domain.db.dynamoDb.template.UserRecommendTemplate;
import com.seojey.domain.db.model.obj.RecommendProduct;
import com.seojey.domain.db.model.table.UserRecommend;
import com.seojey.domain.service.form.RecommendDTO;
import com.seojey.domain.service.form.RequestParam;
import com.seojey.domain.service.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kimjun on 16. 5. 25..
 */
@Service
public class PreferredProductsService extends RecommendService {

    @Autowired
    private UserRecommendTemplate tmp;
    public PreferredProductsService() {
        serviceName = ServiceType.PREFERRED_PRODUCTS_BY_REVIEW.getDesc();
    }

    @Override
    public RecommendDTO getRecommendDTO(RequestParam param) {

        List<RecommendProduct> preferredProducts = Lists.newArrayList();
        if (param.getMemberId()!= null) {
            UserRecommend rec = tmp.getByKey(param.getMemberId());

            if (rec.getPreferredProducts() != null) {
                preferredProducts.addAll(rec.getPreferredProducts());
            }
            /**
             * TODO else 이면 default 인 top 100 가져오는 로직 필요.
             */

        }

        ProductUtil.removeProduct(preferredProducts, param.getProductIds());
        ProductUtil.sortRecommendProduct(preferredProducts);

        return new RecommendDTO(new HashMap<>(
                ImmutableMap.of(serviceName, preferredProducts)));
    }
}
