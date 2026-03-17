package homeoffice.api.pojomodels;

import lombok.Data;

import java.util.List;

@Data
public class NearestPostcodeResponse {

    private int status;
    private List<PostcodeResult> result;
}
