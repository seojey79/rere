package com.seojey.domain.db.dynamoDb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import com.google.common.collect.Lists;
import com.seojey.domain.db.model.TableInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kimjun on 16. 6. 1..
 */
@Service
public abstract class DynamoDbDAO <T, K> {
    private Class<T> typeParameterClass;
    private Table table;
    private TableInfo tableInfo;
    private DynamoDBMapper mapper;

    public DynamoDbDAO(DynamoDB db, TableInfo tableInfo) {
        this.tableInfo = tableInfo;
        this.table = db.getTable(tableInfo.getTableName());
    }

    public DynamoDbDAO(Class<T> typeParameterClass, DynamoDBMapper mapper, TableInfo tableInfo) {
        this.typeParameterClass = typeParameterClass;
        this.tableInfo = tableInfo;
        this.mapper = mapper;
    }


    public T getByKey(final K keyVal) {
        /**
         * TODO 필요한 필드만 설정하는 메서드 별도 생성
         */
        return mapper.load(typeParameterClass , keyVal);
    }
    /**
     *
     * @param keyList one key name, multiple key value and return multiple rows
     * @return
     */
    public List<T> getsByKey(final List<T> keyList) {

//        /**
//         * query type
//         */
//        ItemCollection<QueryOutcome> items = table.query(getQuerySpec(keyList, 0));
//
//        Iterator<Item> iterator = items.iterator();
//
//        while (iterator.hasNext()) {
//            Item item = iterator.next();
//            System.out.println(item.getList("preferredProd"));
//            System.out.println(item.getList("preferredProdWithSkin"));
//            System.out.println(item.getList("preferredProdWithStdCate"));
//        }

        /**
         * use mapper
         */

        /**
         * TODO 필요한 필드만 설정하는 메서드 별도 생성
         */
        Map<String, List<Object>> retVal = mapper.batchLoad(keyList);

        return (List<T>) retVal.get(tableInfo.getTableName());

    }


    private KeyAttribute[] getItemSpec(final List<K> keyList) {
        List<KeyAttribute> specKeyList = Lists.newArrayList();

        for (int i = 0 ; i < keyList.size() ; i++) {
            KeyAttribute key = new KeyAttribute(tableInfo.getKeyNameList().get(i), keyList.get(0));
            specKeyList.add(key);
        }
        return (KeyAttribute[]) specKeyList.toArray();
    }

    private QuerySpec getQuerySpec(final List<K> keyList, final int keyIndex) {
        String keyName = tableInfo.getKeyNameList().get(keyIndex);

        HashMap<String, Object> valueMap;
        valueMap = new HashMap<>();
        QuerySpec spec = new QuerySpec()
            .withKeyConditionExpression(getQueryCondition(keyList, keyName, valueMap))
            .withValueMap(valueMap);

        return spec;
    }

    private String getQueryCondition(final List<K> keyList, String keyName, HashMap<String, Object> valueMap) {
        String keyQuery =  "(" + keyName + " = :key_0)";
        valueMap.put(":key_0",  keyList.get(0));

        final String keyPrefix =":key_";
        int i = 1;
        while (i < keyList.size()) {
            String key = keyPrefix + i;
            keyQuery += "or (" + keyName + " = "+ key +")";
            valueMap.put(key,  keyList.get(i));
            i++;
        }

        return keyQuery;
    }

}
