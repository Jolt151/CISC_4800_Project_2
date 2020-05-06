package com.fortyeighthundred.finalproject.demo.db

import com.fortyeighthundred.finalproject.demo.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String): User
    fun findByUsernameAndPassword(username: String, password: String): User?


    //Implicit save method inherited from CrudRepository
}