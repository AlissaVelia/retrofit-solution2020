package id.putraprima.retrofit.api.models;

import java.io.IOException;
import java.lang.annotation.Annotation;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorResponse {

    public static API_Error parseError(Response<?> response) {
        Converter<ResponseBody, API_Error> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(API_Error.class, new Annotation[0]);

        API_Error error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new API_Error();
        }

        return error;
    }
}