package com.example.android_frontend_tennis.api.match

import android.content.SharedPreferences
import android.util.Log
import com.example.android_frontend_tennis.MatchCard
import com.example.android_frontend_tennis.PenaltyClass
import com.example.android_frontend_tennis.api.RetrofitInstance
import com.example.android_frontend_tennis.api.auth.AuthResult
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import retrofit2.HttpException
import java.time.ZoneId


class MatchService(): IMatchService {

    override suspend fun insertOrUpdate(match:MatchCard): MatchResult<Any> {
        return try {
            Log.e("MATCH id", match.id.toString())
            val response = RetrofitInstance.matchApi.insertOrUpdate(
                request = MatchRequestInsertOrUpdate(
                    id = match.id.toString(),
                    created = match.created.toString(),
                    setCount = match.setCount,
                    endType = match.endType,
                    whoServiceFirst = match.whoServiceFirst,
                            firstPlayerName = match.firstPlayerName,
                    secondPlayerName = match.secondPlayerName,
                    penalties = match.penalties,
                    sets = match.sets,
                    points = match.points
            ));
            Log.i("Response",response.toString())
            MatchResult.Success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                MatchResult.Unauthorized()
            } else {
                MatchResult.WithError(e.response()?.errorBody()?.string())
            }
        } catch (e: Exception) {

            MatchResult.WithError("Не удалось подключиться к серверу")
        }
    }

    override suspend fun getAll(): MatchResult<Any> {
        return try {
            val response = RetrofitInstance.matchApi.getAll()
            Log.e("Response",response.toString())
            MatchResult.Success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                MatchResult.Unauthorized()
            } else {
                MatchResult.WithError(e.response()?.errorBody()?.string())
            }
        } catch (e: Exception) {

            MatchResult.WithError("Не удалось подключиться к серверу")
        }
    }

//    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
//        return try {
//            val response = RetrofitInstance.authApi.signIn(
//                request = AuthRequestLogin(
//                    username = username,
//                    password = password
//                )
//            )
//            prefs.edit()
//                .putString("jwt", response.token)
//                .apply()
//            AuthResult.Authorized()
//        } catch(e: HttpException) {
//
//            if(e.code() == 401) {
//                AuthResult.Unauthorized()
//            } else {
//                AuthResult.UnknownError(e.response()?.errorBody()?.string())
//            }
//        } catch (e: Exception) {
//            Log.i("api","exception")
//            AuthResult.UnknownError("Не удалось подключиться к серверу")
//        }
//    }
//
//    override suspend fun authenticate(): AuthResult<Unit> {
//        return try {
//            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
//            RetrofitInstance.authApi.authenticate("Bearer $token")
//            AuthResult.Authorized()
//        } catch(e: HttpException) {
//            if(e.code() == 401) {
//                AuthResult.Unauthorized()
//            } else {
//                AuthResult.UnknownError(e.response()?.errorBody()?.string())
//            }
//        } catch (e: Exception) {
//            AuthResult.UnknownError("Не удалось подключиться к серверу")
//        }
//    }
}