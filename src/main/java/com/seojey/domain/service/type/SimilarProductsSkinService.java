package com.seojey.domain.service.type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.seojey.domain.db.dynamoDb.template.ProductRecommendTemplate;
import com.seojey.domain.db.model.obj.RecommendProduct;
import com.seojey.domain.db.model.table.ProductRecommend;
import com.seojey.domain.service.form.RecommendDTO;
import com.seojey.domain.service.form.RequestParam;
import com.seojey.domain.service.util.ProductUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kimjun on 16. 6. 8..
 */
@Service
public class SimilarProductsSkinService extends RecommendService{

    @Autowired
    private ProductRecommendTemplate tmp;

    public SimilarProductsSkinService() {
        serviceName = ServiceType.SIMILAR_INGREDIENT_PRODUCTS_WITH_SKIN.getDesc();
    }

    @Override
    public RecommendDTO getRecommendDTO(RequestParam param) {

        List<RecommendProduct> preferredProducts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(param.getProductIds())) {

            List<ProductRecommend> recList = tmp.getsByKey(ProductRecommend.getObjectsByKey(param.getProductIds()));

            if (CollectionUtils.isNotEmpty(recList)) {
                for (ProductRecommend rec : recList) {
                    if (rec.getSimilarProdCate() != null) {
                        preferredProducts.addAll(rec.getSimilarProdCate());
                    }
                }
            }

            /**
             * TODO else 이면 default 인 top 100 가져오는 로직 필요.
             */

        }

        ProductUtil.removeDupProduct(preferredProducts);
        ProductUtil.removeProduct(preferredProducts, param.getProductIds());
        ProductUtil.sortRecommendProduct(preferredProducts);

        return new RecommendDTO(new HashMap<>(
                ImmutableMap.of(serviceName, preferredProducts)));
    }
}
