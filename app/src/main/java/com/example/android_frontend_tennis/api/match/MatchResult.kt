package com.example.android_frontend_tennis.api.match

sealed class MatchResult<T>(val data: T? = null) {
    class Success<T>(data: T? = null): MatchResult<T>(data)
    class Unauthorized<T>: MatchResult<T>()
    class WithError<T>(public var message:String?): MatchResult<T>()
}
