package com.seojey.domain.db.model.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.common.collect.Lists;
import com.seojey.domain.db.model.TableName;
import com.seojey.domain.db.model.obj.RecommendProduct;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by kimjun on 16. 6. 8..
 */
@DynamoDBTable(tableName = TableName.PRODUCT_RECOMMEND)
public class ProductRecommend implements TableClass {

    private long productId; //key #1

    private List<RecommendProduct> similarProdCate;
    private List<RecommendProduct> similarProdSkin;

    @DynamoDBHashKey(attributeName = "productId")
    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }

    @DynamoDBAttribute(attributeName = "similarProdCate")
    public List<RecommendProduct> getSimilarProdCate() {
        return similarProdCate;
    }
    public void setSimilarProdCate(List<RecommendProduct> similarProdCate) {
        this.similarProdCate = similarProdCate;
    }

    @DynamoDBAttribute(attributeName = "similarProdSkin")
    public List<RecommendProduct> getSimilarProdSkin() {
        return similarProdSkin;
    }
    public void setSimilarProdSkin(List<RecommendProduct> similarProdSkin) {
        this.similarProdSkin = similarProdSkin;
    }


    public static List<ProductRecommend> getObjectsByKey(List<Long> productIds) {
        List<ProductRecommend> retValList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(productIds)) {
            for (Long productId : productIds) {
                ProductRecommend rec = new ProductRecommend();
                rec.setProductId(productId);
                retValList.add(rec);
            }
        }

        return retValList;

    }
}
