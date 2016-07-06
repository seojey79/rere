package com.seojey.domain.db.dynamoDb.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.seojey.domain.db.dynamoDb.DynamoDbDAO;
import com.seojey.domain.db.model.TableInfo;
import com.seojey.domain.db.model.table.UserRecommend;
import org.springframework.stereotype.Service;

/**
 * Created by kimjun on 16. 6. 2..
 */
@Service
public class UserRecommendTemplate extends DynamoDbDAO<UserRecommend, Long> {
    public UserRecommendTemplate(DynamoDBMapper mapper) {
        super(UserRecommend.class, mapper, TableInfo.USER_RECOMMEND);
    }
}
