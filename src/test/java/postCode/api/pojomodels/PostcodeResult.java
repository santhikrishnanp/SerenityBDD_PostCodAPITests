package postCode.api.pojomodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeResult {

    private String postcode;
    private double longitude;
    private double latitude;
    private Codes codes;
    private Double distance;


}
