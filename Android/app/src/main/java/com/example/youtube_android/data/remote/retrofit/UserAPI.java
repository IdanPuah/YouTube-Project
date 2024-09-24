package com.example.youtube_android.data.remote.retrofit;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube_android.R;
import com.example.youtube_android.data.remote.api.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private static final int UPDATE = 1;
    private static final int NOT_UPDATE_ERROR = 0;
    private static final int SAME_NAME = 2;
    private static final int SAME_MAIL = 3;
    private static final int SAME_PASSWORD = 4;
    private ApiService apiService;
    private Retrofit retrofit;
    private Context context;

    public UserAPI(Context context) {
        this.context = context;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void login(String email, String password, MutableLiveData<LoginResponse> loginResponseLiveData) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<ResponseBody> call = apiService.login(loginRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        BufferedSource source = Okio.buffer(Okio.source(inputStream));
                        String responseBody = source.readUtf8();
                        source.close();

                        Gson gson = new Gson();
                        JsonObject userJson = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("user");
                        String token = gson.fromJson(responseBody, JsonObject.class).get("token").getAsString();

                        LoginResponse loginResponse = new LoginResponse(LoginResponse.Status.SUCCESS, userJson, token);
                        loginResponseLiveData.postValue(loginResponse);
                    } else {
                        LoginResponse loginResponse = new LoginResponse(LoginResponse.Status.FAILURE_INVALID_CREDENTIALS);
                        loginResponseLiveData.postValue(loginResponse);
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    LoginResponse loginResponse = new LoginResponse(LoginResponse.Status.FAILURE_OUT_OF_MEMORY);
                    loginResponseLiveData.postValue(loginResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    LoginResponse loginResponse = new LoginResponse(LoginResponse.Status.FAILURE_OTHER);
                    loginResponseLiveData.postValue(loginResponse);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                LoginResponse loginResponse = new LoginResponse(LoginResponse.Status.FAILURE_NETWORK);
                loginResponseLiveData.postValue(loginResponse);
            }
        });
    }

    public void signUp(String username, String email, String password, Uri imageUri, MutableLiveData<SignUpResponse> signUpResponseLiveData) {
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);

        MultipartBody.Part body = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                byte[] inputData = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(imageUri)), inputData);
                body = MultipartBody.Part.createFormData("photo", "profile_image", requestFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OTHER);
            signUpResponseLiveData.postValue(signUpResponse);
            return;
        }

        Call<ResponseBody> call = apiService.signUp(usernameBody, emailBody, passwordBody, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        BufferedSource source = Okio.buffer(Okio.source(inputStream));
                        String responseBody = source.readUtf8();
                        source.close();

                        Gson gson = new Gson();
                        Log.d("Response Body", responseBody);
                        JsonObject userJson = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("user");
                        String token = gson.fromJson(responseBody, JsonObject.class).get("token").getAsString();

                        SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.SUCCESS, userJson, token);
                        signUpResponseLiveData.postValue(signUpResponse);
                    } else {
                        if (response.errorBody() != null) {
                            JsonObject errorObject = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
                            String errorMessage = errorObject.get("message").getAsString();

                            if (errorMessage.equals("Email already taken")) {
                                SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_EMAIL_EXISTS);
                                signUpResponseLiveData.postValue(signUpResponse);
                            } else if (errorMessage.equals("Username already taken")) {
                                SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_USER_EXISTS);
                                signUpResponseLiveData.postValue(signUpResponse);
                            } else {
                                SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OTHER);
                                signUpResponseLiveData.postValue(signUpResponse);
                            }
                        } else {
                            SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OTHER);
                            signUpResponseLiveData.postValue(signUpResponse);
                        }
                    }
                } catch (OutOfMemoryError outOfMemoryError) {
                    outOfMemoryError.printStackTrace();
                    SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OUT_OF_MEMORY);
                    signUpResponseLiveData.postValue(signUpResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OTHER);
                    signUpResponseLiveData.postValue(signUpResponse);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                SignUpResponse signUpResponse = new SignUpResponse(SignUpResponse.Status.FAILURE_OTHER);
                signUpResponseLiveData.postValue(signUpResponse);
            }
        });
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void updateUser(String token, String updateUserID, String newUserName, String newMail, String newPassword, MutableLiveData<Integer> isUpdated) {
        UpdateUserRequest request = new UpdateUserRequest(newUserName, newMail, newPassword);

        Call<ResponseMessage> call = apiService.updateUser(token, updateUserID, request);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && "User updated successfully".equals(response.body().getMessage())) {
                        isUpdated.postValue(UPDATE);
                    } else {
                        isUpdated.postValue(NOT_UPDATE_ERROR);
                    }
                } else {
                    String errorBody = "Unknown error";
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : errorBody;
                    } catch (IOException e) {
                        Log.e("API Error", "Error parsing error body", e);
                    }

                    int statusCode = response.code();
                    switch (statusCode) {
                        case 400:
                            if (errorBody.contains("Username is already in use")) {
                                Log.d("user api", "user name in use");
                                isUpdated.postValue(SAME_NAME);
                            } else if (errorBody.contains("Email is already in use")) {
                                isUpdated.postValue(SAME_MAIL);
                            } else if (errorBody.contains("Password does not meet the criteria")) {
                                isUpdated.postValue(SAME_PASSWORD);
                            }
                            break;
                        case 401:
                        case 404:
                        case 500:
                            isUpdated.postValue(NOT_UPDATE_ERROR);
                            break;
                        default:
                            Log.e("API Error", "Error code: " + statusCode + ", Error message: " + errorBody);
                            break;
                    }
                    isUpdated.postValue(NOT_UPDATE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage(), t);
                isUpdated.postValue(NOT_UPDATE_ERROR);
            }
        });
    }

    public void deleteUser(String token, String userId, MutableLiveData<Boolean> userDelete) {
        Call<ResponseMessage> call = apiService.deleteUser(token, userId);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    userDelete.postValue(true);
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage != null) {
                        System.out.println("User deleted successfully: " + responseMessage.getMessage());
                    } else {
                        System.out.println("Response body is null");
                    }
                } else {
                    System.out.println("Server responded with error: " + response.code());
                    try {
                        String errorMessage = response.errorBody().string();
                        System.out.println("Error message: " + errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userDelete.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                System.out.println("Request failed: " + t.getMessage());
                t.printStackTrace();
                userDelete.postValue(false);
            }
        });
    }


}