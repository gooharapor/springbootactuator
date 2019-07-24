package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ActuatorController {
    @GetMapping("greeting")
    fun greeting(): String {
        return "Hello there."
    }

    @GetMapping
    fun home(): String {
        return "Hello"
    }

    @GetMapping("/show")
    fun show(): String {
        return "Show"
    }
}