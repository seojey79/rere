package com.seojey.domain.db.dynamoDb.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.seojey.domain.db.dynamoDb.DynamoDbDAO;
import com.seojey.domain.db.model.TableInfo;
import com.seojey.domain.db.model.table.ProductRecommend;

/**
 * Created by kimjun on 16. 6. 8..
 */
public class ProductRecommendTemplate extends DynamoDbDAO<ProductRecommend, Long> {
    public ProductRecommendTemplate(DynamoDBMapper mapper) {
        super(ProductRecommend.class, mapper, TableInfo.PRODUCT_RECOMMEND);
    }
}
