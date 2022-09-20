package com.example.android_frontend_tennis.api.auth

import android.content.SharedPreferences
import android.util.Log
import com.example.android_frontend_tennis.api.RetrofitInstance
import retrofit2.HttpException


class AuthService(private val prefs: SharedPreferences): IAuthService {

    override suspend fun signUp(username: String, password: String, repeat:String): AuthResult<Unit> {
        return try {
            RetrofitInstance.authApi.signUp(
                request = AuthRequestSignup(
                    username = username,
                    password = password,
                    repeat = repeat,
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.response()?.errorBody()?.string())
            }
        } catch (e: Exception) {

            AuthResult.UnknownError("Не удалось подключиться к серверу")
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = RetrofitInstance.authApi.signIn(
                request = AuthRequestLogin(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()
        } catch(e: HttpException) {

            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.response()?.errorBody()?.string())
            }
        } catch (e: Exception) {
            Log.i("api","exception")
            AuthResult.UnknownError("Не удалось подключиться к серверу")
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            RetrofitInstance.authApi.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.response()?.errorBody()?.string())
            }
        } catch (e: Exception) {
            AuthResult.UnknownError("Не удалось подключиться к серверу")
        }
    }
}