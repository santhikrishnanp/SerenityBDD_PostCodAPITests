package homeoffice.api.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetNearestPostcodesSteps implements Task {
    private final double longitude;
    private final double latitude;

    public GetNearestPostcodesSteps(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static GetNearestPostcodesSteps usingCoordinates(double longitude,double latitude){
        return instrumented(GetNearestPostcodesSteps.class,longitude,latitude);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource("postcodes").with(request->request
                .queryParam("longitude",longitude).queryParam("latitude",latitude).header("Accept","application/json")));

    }
}
