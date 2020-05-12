package com.fortyeighthundred.finalproject.demo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Thread(@Id @GeneratedValue val id: Long = 0, //Primary Key
                  val title: String,
                  //One thread to many posts. Necessary for JPA to handle lists.
                  @OneToMany val posts: List<Post>)