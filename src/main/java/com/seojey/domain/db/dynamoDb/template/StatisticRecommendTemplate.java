package com.seojey.domain.db.dynamoDb.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.seojey.domain.db.dynamoDb.DynamoDbDAO;
import com.seojey.domain.db.model.TableInfo;
import com.seojey.domain.db.model.table.StatisticRecommend;

/**
 * Created by kimjun on 16. 6. 8..
 */
public class StatisticRecommendTemplate extends DynamoDbDAO<StatisticRecommend, String> {
    public StatisticRecommendTemplate(DynamoDBMapper mapper) {
        super(StatisticRecommend.class, mapper, TableInfo.STATISTICS_RECOMMEND);
    }

}
