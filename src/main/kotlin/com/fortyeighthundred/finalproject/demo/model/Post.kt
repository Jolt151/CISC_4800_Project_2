package com.fortyeighthundred.finalproject.demo.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Post(@Id @GeneratedValue val id: Long = 0,
                @OneToOne val author: User,
                val timestamp: Date,
                val message: String
                )