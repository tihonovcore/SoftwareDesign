package com.todolist.controller

import com.todolist.dao.TodolistDao
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ListsController(
    private val todolistDao: TodolistDao
) {
    @GetMapping("/manageLists")
    fun manageLists(modelMap: ModelMap): String {
        modelMap.addAttribute("lists", todolistDao.lists)
        return "lists"
    }

    @PostMapping("/deleteList")
    fun deleteList(@RequestParam("listId") listId: Int): String {
        todolistDao.deleteList(listId)
        return "redirect:/manageLists"
    }

    @PostMapping("/addList")
    fun addList(@RequestParam("listName") listName: String): String {
        todolistDao.addList(listName)
        return "redirect:/manageLists"
    }
}
