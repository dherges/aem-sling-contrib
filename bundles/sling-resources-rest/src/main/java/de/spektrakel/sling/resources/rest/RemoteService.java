/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.resources.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RemoteService {

    @GET("/var/{name}.json")
    Call<ResponseBody> read(@Path(value = "name") String name);

}
