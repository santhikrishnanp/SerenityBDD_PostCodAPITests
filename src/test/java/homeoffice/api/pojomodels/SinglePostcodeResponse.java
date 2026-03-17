package homeoffice.api.pojomodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SinglePostcodeResponse {
    private int Status;
    private PostcodeResult result;
}
