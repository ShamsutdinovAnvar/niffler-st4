package guru.qa.niffler.api.registration;

import ch.qos.logback.core.model.Model;
import com.github.tomakehurst.wiremock.common.Errors;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import guru.qa.niffler.model.RegistrationModel;

import javax.servlet.http.HttpServletResponse;

public interface RegistrationApi {

    @GET("/register")
    public String getRegisterPage(@Body Model model);

    @POST(value = "/register")
    public String registerUser(@Body RegistrationModel registrationModel,
                               @Body Errors errors,
                               @Body Model model,
                               @Body HttpServletResponse response);
}