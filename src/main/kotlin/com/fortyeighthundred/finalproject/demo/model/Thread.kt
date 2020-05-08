package com.fortyeighthundred.finalproject.demo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Thread(@Id @GeneratedValue val id: Long = 0,
                  val title: String,
                  @OneToMany val posts: List<Post>)