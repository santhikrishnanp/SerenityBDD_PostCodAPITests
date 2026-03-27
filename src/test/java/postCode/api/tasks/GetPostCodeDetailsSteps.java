package postCode.api.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetPostCodeDetailsSteps implements Task {

    private final String postcode;

    public GetPostCodeDetailsSteps(String postcode) {
        this.postcode = postcode;
    }

    public static GetPostCodeDetailsSteps forPostCode(String postcode){
        return instrumented(GetPostCodeDetailsSteps.class,postcode);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String encoded = postcode.replace(" ","");
        actor.attemptsTo(Get.resource("postcodes/"+encoded.trim())
                .with(request->request.header("Accept","application/json")));

    }
}
