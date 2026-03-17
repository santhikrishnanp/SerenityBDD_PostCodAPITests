package homeoffice.api.questions;

import homeoffice.api.pojomodels.NearestPostcodeResponse;
import homeoffice.api.pojomodels.SinglePostcodeResponse;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ExtractCoordinates implements Question<double[]> {
    @Override
    public double[] answeredBy(Actor actor) {
        SinglePostcodeResponse response = SerenityRest.lastResponse().as(SinglePostcodeResponse.class);

        double longitude = response.getResult().getLongitude();
        double latitude = response.getResult().getLatitude();


//      2nd chnaged not worked  NearestPostcodeResponse response = SerenityRest.lastResponse().as(NearestPostcodeResponse.class);
//        double longitude = response.getResult().get(0).getLongitude();
//        double latitude = response.getResult().get(0).getLatitude();

//      1st chnage double longitude = SerenityRest.lastResponse().jsonPath().getDouble(("result.longitude"));
//       double latitude = SerenityRest.lastResponse().jsonPath().getDouble("result.latitude");
       return new double[]{longitude,latitude};
    }

    public static ExtractCoordinates fromResponse(){
        return new ExtractCoordinates();
    }
}
