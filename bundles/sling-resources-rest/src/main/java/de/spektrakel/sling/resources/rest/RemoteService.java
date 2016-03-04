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
import retrofit2.http.*;

/**
 * Most simple CRUD service once can think of..
 */
public interface RemoteService {

    @GET
    Call<ResponseBody> read(@Url String url);

    @POST
    Call<ResponseBody> update(@Url String url);

    @PUT
    Call<ResponseBody> create(@Url String url);

    @DELETE
    Call<ResponseBody> delete(@Url String url);

}
