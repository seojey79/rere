package com.seojey.domain.db.model;

import com.google.common.collect.Lists;
import com.seojey.domain.managing.exception.DbException;
import lombok.Getter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Created by kimjun on 16. 6. 1..
 */
@Getter
public enum TableInfo {
    USER_RECOMMEND(TableName.USER_RECOMMEND, DataBaseType.DYNAMO_DB, "memberId"),
    PRODUCT_RECOMMEND(TableName.PRODUCT_RECOMMEND, DataBaseType.DYNAMO_DB, "productId"),
    STATISTICS_RECOMMEND(TableName.STATISTICS_RECOMMEND, DataBaseType.DYNAMO_DB, "key");

    private String tableName;
    private DataBaseType dbType;
    private List<String> keyNameList = Lists.newArrayList();

    private TableInfo(String tableName, DataBaseType dbType, String... keyName) {
        this.tableName = tableName;
        this.dbType = dbType;

        if (keyName == null || keyName.length <= 0) {
            throw new DbException("key list are null");
        }

        for (String key : keyName) {

            if (StringUtils.isNotEmpty(key)) {
                keyNameList.add(key);
            } else {
                throw new DbException("key list have empty");
            }
        }
    }

}
