package com.seojey.domain.db.model.obj;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;

/**
 * Created by kimjun on 16. 6. 7..
 */
@DynamoDBDocument
public class RecommendProduct implements Serializable {

    private Long pid;
    private Double score;


    @DynamoDBAttribute(attributeName = "pid")
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @DynamoDBAttribute(attributeName = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


}
