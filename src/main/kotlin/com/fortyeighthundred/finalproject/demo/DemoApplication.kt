package com.fortyeighthundred.finalproject.demo

import com.fortyeighthundred.finalproject.demo.db.PostRepository
import com.fortyeighthundred.finalproject.demo.db.ThreadRepository
import com.fortyeighthundred.finalproject.demo.db.UserRepository
import com.fortyeighthundred.finalproject.demo.model.Post
import com.fortyeighthundred.finalproject.demo.model.Thread
import com.fortyeighthundred.finalproject.demo.model.User
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@SpringBootApplication
class DemoApplication {

    private val logger = KotlinLogging.logger {}


    @Bean
    fun demo(userRepository: UserRepository, threadRepository: ThreadRepository, postRepository: PostRepository): CommandLineRunner {
        return CommandLineRunner {args ->
            userRepository.save(User("testUser", BCryptPasswordEncoder().encode("123456")))
            userRepository.save(User("testUser2", BCryptPasswordEncoder().encode("qwerty")))
            userRepository.save(User("anotherUser", BCryptPasswordEncoder().encode("hunter2")))

            logger.info { "Loading first user..." }
            val user1 = userRepository.findByUsername("testUser")
            logger.info { "user1: ${user1.username}/${user1.password}" }

            logger.info { "Loading second user..." }
            val user2 = userRepository.findByUsername("testUser2")
            logger.info { "user1: ${user2.username}/${user2.password}" }

            val post1 = postRepository.save(Post(author =  userRepository.findByUsername("testUser"),
                    timestamp = Date(), message = "Hey everyone! Welcome to the forum!"))



            threadRepository.save(Thread( title = "First discussion!",
                    posts = listOf(post1)))
        }
    }

}
