package com.fortyeighthundred.finalproject.demo.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Post(@Id @GeneratedValue val id: Long = 0, //PrimaryKey
                //One Post to One User. Necessary for JPA to handle the User type.
                @OneToOne val author: User,
                val timestamp: Date,
                val message: String
                )