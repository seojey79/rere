package com.seojey.domain.service.util;

import com.google.common.collect.Lists;
import com.seojey.domain.db.model.obj.RecommendProduct;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kimjun on 16. 6. 8..
 */
public class ProductUtil {



    public static List<RecommendProduct> sortRecommendProduct(List<RecommendProduct> deals) {
        if (CollectionUtils.isEmpty(deals)) {
            return Lists.newArrayList();
        }
        Collections.sort(deals, PRODUCT_COMPARATOR);

        return deals;
    }

    public static final Comparator<RecommendProduct> PRODUCT_COMPARATOR = new Comparator<RecommendProduct>() {
        @Override
        public int compare(RecommendProduct o1, RecommendProduct o2) {
            if (o1.getScore() > o2.getScore()) {
                return -1;
            }
            if (o1.getScore() < o2.getScore()) {
                return 1;
            }
            return 0;
        }
    };

    public static List<RecommendProduct> removeProduct(List<RecommendProduct> recommendProducts,
                                                          List<Long> productIds) {
        if (CollectionUtils.isEmpty(recommendProducts)) {
            return Lists.newArrayList();
        }

        List<RecommendProduct> deleteProducts = Lists.newArrayList();

        for (RecommendProduct product : recommendProducts) {
            for (Long id : productIds) {
                if (product.getPid().equals(id)) {
                    deleteProducts.add(product);
                }
            }
        }

        if (deleteProducts.size() > 0) {
            recommendProducts.removeAll(deleteProducts);
        }

        return recommendProducts;
    }


    public static List<RecommendProduct> removeDupProduct(List<RecommendProduct> recommendProducts) {

        if (CollectionUtils.isEmpty(recommendProducts)) {
            return Lists.newArrayList();
        }

        List<RecommendProduct> retProducts = Lists.newArrayList();

        for (RecommendProduct product : recommendProducts) {
            Long productId = product.getPid();
            Boolean setFlag = true;

            if (CollectionUtils.isNotEmpty(retProducts)) {
                for (RecommendProduct retProduct : retProducts) {
                    if (productId.equals(retProduct.getPid())) {
                        setFlag = false;
                        break;
                    }
                }
            }

            if (setFlag) {
                retProducts.add(product);
            }
        }
        recommendProducts.clear();
        recommendProducts.addAll(retProducts);

        return recommendProducts;
    }
}
