package postCode.api.questions;

import postCode.api.pojomodels.NearestPostcodeResponse;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class FindNearestPostcode implements Question<String> {
    @Override
    public String answeredBy(Actor actor) {
        NearestPostcodeResponse response =
                SerenityRest.lastResponse().as(NearestPostcodeResponse.class);
        return response.getResult().get(0).getPostcode();
    }

    public static FindNearestPostcode fromResponse(){
        return new FindNearestPostcode();
    }
}
