package postCode.api.questions;

import postCode.api.pojomodels.SinglePostcodeResponse;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ExtractCoordinates implements Question<double[]> {
    @Override
    public double[] answeredBy(Actor actor) {
        SinglePostcodeResponse response = SerenityRest.lastResponse().as(SinglePostcodeResponse.class);

        double longitude = response.getResult().getLongitude();
        double latitude = response.getResult().getLatitude();
        return new double[]{longitude,latitude};
    }

    public static ExtractCoordinates fromResponse(){
        return new ExtractCoordinates();
    }
}
