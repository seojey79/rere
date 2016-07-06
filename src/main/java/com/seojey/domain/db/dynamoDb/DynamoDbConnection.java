package com.seojey.domain.db.dynamoDb;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.seojey.domain.db.dynamoDb.template.ProductRecommendTemplate;
import com.seojey.domain.db.dynamoDb.template.StatisticRecommendTemplate;
import com.seojey.domain.db.dynamoDb.template.UserRecommendTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


/**
 * Created by kimjun on 16. 6. 1..
 */
//@Configuration
public class DynamoDbConnection {

    /**
     * TODO
     * /resources/dynamo-{profile}.properties 읽어서 @Value 필드 세팅
     */
    @Value("${dynamodb['dynamodb.url']}")
    private String dynamoDbUrl = "dynamodb.ap-northeast-1.amazonaws.com";
    @Value("${dynamodb['dynamodb.accessKey']}")
    private String dynamoDbAccessKey ="AKIAIAHNHJG3CYQ4YD5A";
    @Value("${dynamodb['dynamodb.secretKey']}")
    private String dynamoDbSecretKey= "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";

    private DynamoDB dynamoDB;
    private DynamoDBMapper mapper;

    public DynamoDbConnection() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(dynamoDbAccessKey, dynamoDbSecretKey);
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
        client.setEndpoint(dynamoDbUrl);
        dynamoDB = new DynamoDB(client);
        mapper = new DynamoDBMapper(client);

        System.out.println("dynamoDbUrl:" + dynamoDbUrl);
        System.out.println("dynamoDbAccessKey:" + dynamoDbAccessKey);
        System.out.println("dynamoDbSecretKey:" + dynamoDbSecretKey);
    }

    @Bean
    public UserRecommendTemplate getUserRecommendTemplate() {
        return new UserRecommendTemplate(this.mapper);

    }

    @Bean
    public ProductRecommendTemplate getProductRecommendTemplate() {
        return new ProductRecommendTemplate(this.mapper);

    }

    @Bean
    public StatisticRecommendTemplate getStatisticRecommendTemplate() {
        return new StatisticRecommendTemplate(this.mapper);

    }

    public DynamoDBMapper getMapper() {
        return this.mapper;
    }


}
