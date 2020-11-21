package com.todolist

import com.todolist.dao.CaseDao
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
    private val caseDao: CaseDao
) {
    @GetMapping("/")
    fun hello(model: Model): String {
        model["title"] = "hello"
        return "hello"
    }

    @GetMapping("/cases")
    fun cases(modelMap: ModelMap): String {
        modelMap.addAttribute("cases", caseDao.cases.filter { !it.done })
        return "cases"
    }

    @RequestMapping("/updateCase", method = [RequestMethod.POST])
    fun update(@RequestParam("caseId") caseId: Int): String {
        caseDao.done(caseId)
        return "redirect:/cases"
    }
}
