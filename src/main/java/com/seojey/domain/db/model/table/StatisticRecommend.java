package com.seojey.domain.db.model.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.seojey.domain.db.model.TableName;
import com.seojey.domain.db.model.obj.RecommendProduct;

import java.util.List;

/**
 * Created by kimjun on 16. 6. 8..
 */
@DynamoDBTable(tableName = TableName.STATISTICS_RECOMMEND)
public class StatisticRecommend implements TableClass{

    private String key; //key #1

    private List<RecommendProduct> recommendProducts;

    @DynamoDBHashKey(attributeName = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "recommendProducts")
    public List<RecommendProduct> getRecommendProducts() {
        return recommendProducts;
    }

    public void setRecommendProducts(List<RecommendProduct> recommendProducts) {
        this.recommendProducts = recommendProducts;
    }
}
