package com.fortyeighthundred.finalproject.demo.web

import com.fortyeighthundred.finalproject.demo.db.PostRepository
import com.fortyeighthundred.finalproject.demo.db.UserRepository
import com.fortyeighthundred.finalproject.demo.model.User
import com.fortyeighthundred.finalproject.demo.web.webmodels.UpdatePassword
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpSession
import kotlin.math.log

@Controller
class SiteController {
    private val logger = KotlinLogging.logger {  }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @GetMapping("/")
    fun index(httpSession: HttpSession): String {
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
    fun getLogin(httpSession: HttpSession): String {
        httpSession.id
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
        logger.info { httpSession.attributeNames.toList().toString()}
        logger.info { httpSession.id }
        return "loginSuccess"
    }

    @GetMapping("/account")
    fun accountSettings(model: Model, httpSession: HttpSession): String {
        val user = httpSession.getAttribute("user") as User?
        logger.info { user }
        user?.let { user ->
            model["user"] = user
        } ?: return "redirect:/login"

        return "account"

    }

    @PostMapping("/updatePassword")
    fun updatePassword(updatePassword: UpdatePassword, httpSession: HttpSession): String {
        val user = httpSession.getAttribute("user") as User?
        logger.info { user }
        user?.let {
            if (BCryptPasswordEncoder().matches(updatePassword.old, user.password)) {
                //update password
                val updatedUser = User(user.username, BCryptPasswordEncoder().encode(updatePassword.new))
                userRepository.save(updatedUser)
                httpSession.setAttribute("user", updatedUser)
                return "redirect:/account"
            } else {
                return "redirect:/account"
            }
        } ?: return "redirect:/login"

    }

    @PostMapping("/deleteAccount")
    fun deleteAccount(httpSession: HttpSession): String {
        val user = httpSession.getAttribute("user") as User?
        user?.let {  user ->
            //replace user account posts with deleted account
            val posts = postRepository.findAll().toList()

            //it is impossible to login as this account, since a BCrypt hash cannot generate this password
            val deletedUser = User("deletedUser", "unreachablePassword")
            userRepository.save(deletedUser)

            //replace all posts from that user to be from deletedUser
            postRepository.saveAll(posts.map { it.copy(author = deletedUser) })

            //now we can delete the user
            userRepository.delete(user)
            return "redirect:/login"
        } ?: return "redirect:/login"
    }

    @GetMapping("/logout")
    fun logout(httpSession: HttpSession): String {
        httpSession.invalidate()
        return "redirect:/login"
    }
}