package com.fortyeighthundred.finalproject.demo.web

import com.fortyeighthundred.finalproject.demo.db.PostRepository
import com.fortyeighthundred.finalproject.demo.db.ThreadRepository
import com.fortyeighthundred.finalproject.demo.model.Post
import com.fortyeighthundred.finalproject.demo.model.Thread
import com.fortyeighthundred.finalproject.demo.model.User
import com.fortyeighthundred.finalproject.demo.web.webmodels.NewMessage
import com.fortyeighthundred.finalproject.demo.web.webmodels.NewThread
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException
import java.util.*
import javax.servlet.http.HttpSession

@Controller
class ForumController {
    private val logger = KotlinLogging.logger {  }

    @Autowired
    lateinit var threadRepository: ThreadRepository

    @Autowired
    lateinit var postRepository: PostRepository

    @GetMapping("/forum")
    fun forumHome(model: Model): String {
        val threads = threadRepository.findAll()

        logger.info { threads }
        model.addAttribute("threads", threads)

        return "forum"
    }

    @GetMapping("/forum/{id}")
    fun getDiscussion(@PathVariable id: Long, model: Model): String {
        val thread = threadRepository.findByIdOrNull(id)
        thread?.let {thread ->
            model.addAttribute("thread", thread)
        } ?: throw NotFoundException()

        return "thread"
    }

    @PostMapping("/forum/new")
    fun newDiscussion(httpSession: HttpSession, model: Model, newThread: NewThread): String {
        (httpSession.getAttribute("user") as? User)?.let { user ->
            val post = postRepository.save(Post(author = user, timestamp = Date(), message = newThread.message))

            val thread = threadRepository.save(Thread(title = newThread.title, posts = listOf(post)))

            return "redirect:/forum/${thread.id}"

        } ?: return "redirect:/login"

    }

    @PostMapping("/forum/{id}/new")
    fun newPost(@PathVariable id: Long, httpSession: HttpSession, model: Model, newMessage: NewMessage): String {
        (httpSession.getAttribute("user") as? User)?.let {user ->
            val thread = threadRepository.findByIdOrNull(id) ?: throw NotFoundException()
            val post = postRepository.save(Post(author = user, timestamp = Date(), message = newMessage.message))

            threadRepository.save(Thread(thread.id, thread.title, thread.posts.plus(post)))

            return "redirect:/forum/$id"


        } ?: return "redirect:/login"
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException: RuntimeException() {

}