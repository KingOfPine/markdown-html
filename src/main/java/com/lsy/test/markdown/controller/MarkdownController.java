package com.lsy.test.markdown.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by liangsongying on 2017/12/13.
 */
@Controller
public class MarkdownController {
    @ResponseBody
    @RequestMapping(value = "")
    public String test1() {
        return "hahhahhah";
    }

    @ResponseBody
    @RequestMapping("markdown")
    public ModelAndView testMarkdown() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;

    }
}
