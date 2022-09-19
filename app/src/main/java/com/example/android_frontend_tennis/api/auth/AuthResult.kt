package com.example.android_frontend_tennis.api.auth

sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class UnknownError<T>(public var message:String?): AuthResult<T>()
}
