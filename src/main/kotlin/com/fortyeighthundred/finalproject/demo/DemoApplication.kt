package com.fortyeighthundred.finalproject.demo

import com.fortyeighthundred.finalproject.demo.db.UserRepository
import com.fortyeighthundred.finalproject.demo.model.User
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@SpringBootApplication
class DemoApplication {

    private val logger = KotlinLogging.logger {}


    @Bean
    fun demo(userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {args ->
            userRepository.save(User("testUser", "123456"))
            userRepository.save(User("testUser2", "qwerty"))
            userRepository.save(User("anotherUser", "hunter2"))

            logger.info { "Loading first user..." }
            val user1 = userRepository.findByUsername("testUser")
            logger.info { "user1: ${user1.username}/${user1.password}" }

            logger.info { "Loading second user..." }
            val user2 = userRepository.findByUsername("testUser2")
            logger.info { "user1: ${user2.username}/${user2.password}" }
        }
    }

}
