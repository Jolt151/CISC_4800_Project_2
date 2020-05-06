package com.fortyeighthundred.finalproject.demo.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(@Id val username: String,
                val hashedPassword: String)