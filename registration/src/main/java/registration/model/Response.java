package registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data   // @Data generates getter and setter methods for the 'jwt' field.
@AllArgsConstructor // @AllArgsConstructor generates a constructor that accepts a 'jwt' parameter.
@NoArgsConstructor // @NoArgsConstructor generates a default constructor with no parameters.
public class Response implements Serializable {
    private String jwt;
}
