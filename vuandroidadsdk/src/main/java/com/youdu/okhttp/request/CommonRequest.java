package com.youdu.okhttp.request;

import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CommonRequest {
  /**
   * create the key-value Request
   */
  public static Request createPostRequest(String url, RequestParams params) {
    return createPostRequest(url, params, null);
  }

  /**
   * 可以带请求头的Post请求
   */
  public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
    FormBody.Builder mFormBodyBuild = new FormBody.Builder();
    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        mFormBodyBuild.add(entry.getKey(), entry.getValue());
      }
    }
    //添加请求�?
    Headers.Builder mHeaderBuild = new Headers.Builder();
    if (headers != null) {
      for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
        mHeaderBuild.add(entry.getKey(), entry.getValue());
      }
    }
    FormBody mFormBody = mFormBodyBuild.build();
    Headers mHeader = mHeaderBuild.build();
    Request request = new Request.Builder().url(url).
        post(mFormBody).headers(mHeader).build();
    return request;
  }

  /**
   * ressemble the params to the url
   */
  public static Request createGetRequest(String url, RequestParams params) {

    return createGetRequest(url, params, null);
  }

  /**
   * 可以带请求头的Get请求
   */
  public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
    StringBuilder urlBuilder = new StringBuilder(url).append("?");
    if (params != null) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
      }
    }
    //添加请求�?
    Headers.Builder mHeaderBuild = new Headers.Builder();
    if (headers != null) {
      for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
        mHeaderBuild.add(entry.getKey(), entry.getValue());
      }
    }
    Headers mHeader = mHeaderBuild.build();
    return new Request.Builder().
        url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().headers(mHeader).build();
  }

  /**
   * @param url
   * @param params
   * @return
   */
  public static Request createMonitorRequest(String url, RequestParams params) {
    StringBuilder urlBuilder = new StringBuilder(url).append("&");
    if (params != null && params.hasParams()) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
      }
    }
    return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1))
        .get()
        .build();
  }

  public static Request createMultiPostRequest(String url, RequestParams params) {
    // 表单提交，有文件
    MultipartBody.Builder multipartBodybuilder =
        new MultipartBody.Builder().setType(MultipartBody.FORM);
    // 拼接键值对
    if (!params.urlParams.isEmpty()) {
      for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
        multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue());
      }
    }
    // 拼接文件
    for (Map.Entry<String, RequestParams.FileWrapper> entry : params.fileParamsMap.entrySet()) {
      RequestBody fileBody =
          RequestBody.create(entry.getValue().contentType, entry.getValue().file);
      multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue().fileName, fileBody);
    }
    // return multipartBodybuilder.build();
    return new Request.Builder().url(url).post(multipartBodybuilder.build()).build();
  }
}
