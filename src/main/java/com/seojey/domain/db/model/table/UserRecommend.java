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
 * Created by kimjun on 16. 6. 1..
 * description of Table Field List
 */

@DynamoDBTable(tableName = TableName.USER_RECOMMEND)
public class UserRecommend implements TableClass {

    private long memberId; //key #1
    private List<RecommendProduct> preferredProducts;
    private List<RecommendProduct> preferredProductsWithStdCate;
    private List<RecommendProduct> preferredProductsWithSkin;


    @DynamoDBHashKey(attributeName = "memberId")
    public long getMemberId() { return memberId;}
    public void setMemberId(long memberId) {this.memberId = memberId;}

    @DynamoDBAttribute(attributeName = "preferredProd")
    public List<RecommendProduct> getPreferredProducts() {
        return preferredProducts;
    }
    public void setPreferredProducts(List<RecommendProduct> preferredProducts) {
        this.preferredProducts = preferredProducts;
    }

    @DynamoDBAttribute(attributeName = "preferredProdWithStdCate")
    public List<RecommendProduct> getPreferredProductsWithStdCate() {
        return preferredProductsWithStdCate;
    }
    public void setPreferredProductsWithStdCate(List<RecommendProduct> preferredProductsWithStdCate) {
        this.preferredProductsWithStdCate = preferredProductsWithStdCate;
    }


    @DynamoDBAttribute(attributeName = "preferredProdWithSkin")
    public List<RecommendProduct> getPreferredProductsWithSkin() {
        return preferredProductsWithSkin;
    }
    public void setPreferredProductsWithSkin(List<RecommendProduct> preferredProductsWithSkin) {
        this.preferredProductsWithSkin = preferredProductsWithSkin;
    }

    public static List<UserRecommend> getKeyObjList(List<Integer> productIds) {
        List<UserRecommend> keyObjList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(productIds)) {
            for (Integer productId : productIds) {
                UserRecommend keyObj = new UserRecommend();
                keyObj.setMemberId(productId);
                keyObjList.add(keyObj);
            }
        }
        return keyObjList;

    }
}
