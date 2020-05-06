package com.fortyeighthundred.finalproject.demo.web

import com.fortyeighthundred.finalproject.demo.db.UserRepository
import com.fortyeighthundred.finalproject.demo.model.User
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpSession

@Controller
class SiteController {
    private val logger = KotlinLogging.logger {  }

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/register")
    fun register(): String {
        return "register"
    }

    @PostMapping("/register")
    fun tryRegister(@ModelAttribute user: User): String {
        logger.info { user }

        try{
            val hashedPassword = BCryptPasswordEncoder().encode(user.password)
            val updatedUser = User(user.username, hashedPassword)

            userRepository.save(updatedUser)
            return "registerSuccess"
        } catch (e: Exception) {
            return "registerFail"
        }
    }

    @GetMapping("/login")
    fun getLogin(): String {
        return "login"
    }

    @PostMapping("/login")
    fun tryLogin(@ModelAttribute user: User, httpSession: HttpSession): String {
        logger.info { user }

        val foundUser = userRepository.findByUsername(user.username)
        if (!BCryptPasswordEncoder().matches(user.password, foundUser.password)) {
            return "loginFail"
        }
        //set user as session variable
        httpSession.setAttribute("user", foundUser)
        return "loginSuccess"
    }
}