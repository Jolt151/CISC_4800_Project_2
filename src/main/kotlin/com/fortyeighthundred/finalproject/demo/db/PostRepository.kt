package com.fortyeighthundred.finalproject.demo.db

import com.fortyeighthundred.finalproject.demo.model.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Long>