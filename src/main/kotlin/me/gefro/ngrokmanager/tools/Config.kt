package me.gefro.ngrokmanager.tools

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Config {

    @Value("\${app.ngrok.http.ports}")
    private lateinit var ngrokHttpPortsList: String

    fun getNgrokHttpPortsList(): List<Int> {
        return ngrokHttpPortsList.split(",").map { it.trim().toInt() }
    }

    @Value("\${app.ngrok.tcp.ports}")
    private lateinit var ngrokTcpPortsList: String

    fun getNgrokTcpPortsList(): List<Int> {
        return ngrokTcpPortsList.split(",").map { it.trim().toInt() }
    }

}