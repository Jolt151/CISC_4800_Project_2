package com.fortyeighthundred.finalproject.demo.db

import org.springframework.data.repository.CrudRepository
import com.fortyeighthundred.finalproject.demo.model.Thread

interface ThreadRepository : CrudRepository<Thread, Long> {
}