package com.example.pas.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente Retrofit configurado para aceder à API de receitas.
 * Usamos Singleton para garantir que só existe uma instância.
 * 
 * NOTA: Para emulador Android, use "10.0.2.2" para aceder ao localhost.
 * Para dispositivo físico na mesma rede, use o IP da máquina.
 */
public class ApiClient {

    // URL base da API Laravel (emulador Android)
    // Muda para o IP da máquina se usar dispositivo físico
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    /**
     * Obtém a instância do Retrofit (Singleton).
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().build())
                    .build();
        }
        return retrofit;
    }

    /**
     * Obtém a instância do ApiService (Singleton).
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }
}
