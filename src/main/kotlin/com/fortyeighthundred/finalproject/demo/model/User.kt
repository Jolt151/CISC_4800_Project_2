package com.fortyeighthundred.finalproject.demo.model

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(@Id val username: String, //Primary Key
                val password: String): Serializable