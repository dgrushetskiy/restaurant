//package dataload.service;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import dataload.model.Restaurant;
//import org.springframework.batch.item.ItemWriter;
//
//import java.util.List;
//
//public class DynamoDBItemWriter implements ItemWriter<Restaurant> {
//    private DynamoDBMapper dynamoDBMapper;
//
//    public DynamoDBItemWriter(AmazonDynamoDB amazonDynamoDB) {
//        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
//    }
//
//    @Override
//    public void write(List<? extends Restaurant> items) throws Exception {
//        dynamoDBMapper.batchSave(items);
//    }
//}