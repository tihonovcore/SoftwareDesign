package com.todolist

import com.todolist.dao.TodolistDao
import com.todolist.model.Case
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HtmlController(
    private val todolistDao: TodolistDao
) {
    @GetMapping("/")
    fun hello(model: Model): String {
        model["title"] = "hello"
        return "hello"
    }

    @GetMapping("/cases")
    fun cases(modelMap: ModelMap): String {
        modelMap.addAttribute("cases", todolistDao.currentList.cases.filter { !it.done })
        modelMap.addAttribute("currList", todolistDao.currentList)
        modelMap.addAttribute("lists", todolistDao.lists)
        return "cases"
    }

    @RequestMapping("/updateCase", method = [RequestMethod.POST])
    fun update(@RequestParam("caseId") caseId: Int): String {
        todolistDao.done(caseId)
        return "redirect:/cases"
    }

    @RequestMapping("/addCase", method = [RequestMethod.POST])
    fun add(@RequestParam("description") description: String): String {
        todolistDao.addCase(Case(todolistDao.currentList.cases.size, description))
        return "redirect:/cases"
    }

    @RequestMapping("/chooseList", method = [RequestMethod.POST])
    fun chooseList(@RequestParam("listId") listId: Int): String {
        todolistDao.currentList = todolistDao.lists.find { it.id == listId }!!
        return "redirect:/cases"
    }
}
