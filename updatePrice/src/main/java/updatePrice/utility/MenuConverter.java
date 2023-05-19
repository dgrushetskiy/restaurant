package updatePrice.utility;//package addrestaurant.utility;
//
//import addrestaurant.model.Menu;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class MenuConverter implements DynamoDBTypeConverter<String, List<Menu>> {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public String convert(List<Menu> menus) {
//        try {
//            return objectMapper.writeValueAsString(menus);
//        } catch (Exception e) {
//            // Handle the exception based on your requirements
//            //e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public List<Menu> unconvert(String menusJson) {
//        try {
//           return objectMapper.readValue(menusJson, new TypeReference<List<Menu>>(){});
//        } catch (Exception e) {
//            // Handle the exception based on your requirements
//            //e.printStackTrace();
//            return null;
//        }
//    }
//
//}
