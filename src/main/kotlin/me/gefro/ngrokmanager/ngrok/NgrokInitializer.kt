package ngrok

import kotlinx.coroutines.*
import me.gefro.ngrokmanager.tools.Config
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import tools.printCustom
import tools.saveNgrokUrl

@Service
class NgrokInitializer (
    private val ngrokManager: NgrokManager,
    private val config: Config,
): ApplicationRunner {

    suspend fun initializeNgrokAndTunnels() {
        val httpPortsList = config.getNgrokHttpPortsList()
        val tcpPortsList = config.getNgrokTcpPortsList()

        val portList = mutableListOf<Pair<String, Int>>()

        httpPortsList.forEach {
            portList.add(Pair("http", it))
        }
        tcpPortsList.forEach {
            portList.add(Pair("tcp", it))
        }

        println(portList)

        val deferred = CompletableDeferred<Unit>()
        withContext(Dispatchers.IO) {
            val job = launch {
                var count = 0
                do {
                    delay(1000L)
                    if (ngrokManager.isRunning()) {
                        count = ngrokManager.listTunnels().size
                        if (ngrokManager.listTunnels().isNotEmpty()) {
                            for (i in ngrokManager.listTunnels()) {
                                if (i.name == "command_line") {
                                    ngrokManager.stopTunnel("command_line")
                                    count = ngrokManager.listTunnels().size
                                }
                            }
                        }
                        if (count < portList.size) {
                            for(port in portList){
                                try{
                                    ngrokManager.listTunnels().first{
                                        it.config.addr == "http://localhost:${port.second}"
                                    }
                                }catch (e: NoSuchElementException){
                                    ngrokManager.startTunnel(
                                        port.second,
                                        port.first,
                                        "${port.first}-${port.second}"
                                    )
                                }
                            }
                        }
                    }
                } while (count < httpPortsList.size)

                if (ngrokManager.listTunnels().isNotEmpty()) {
                    for(port in portList){
                        try{
                            val url = ngrokManager.listTunnels().first{
                                it.config.addr == "http://localhost:${port.second}"
                            }.publicUrl
                            saveNgrokUrl(url, "ngrok_url_${port.second}")
                            println("saved ngrok public url: $url")
                        }catch (e: NoSuchElementException){
                            printCustom(e.message)
                        }
                    }
                }
                deferred.complete(Unit)
            }
            deferred.await()
            job.cancel()
        }
    }

    override fun run(args: ApplicationArguments?) {
        runBlocking {
            launch(Dispatchers.IO) {
                initializeNgrokAndTunnels()
            }
        }
    }
}