package com.vlg.athletica.data.remote

class UnauthorizedException(message: String) : Exception(message)
class UpdateException : Exception("error update")
class UserAlreadyExistException(message: String) : Exception(message)
class UserNotFoundException(message: String) : Exception(message)