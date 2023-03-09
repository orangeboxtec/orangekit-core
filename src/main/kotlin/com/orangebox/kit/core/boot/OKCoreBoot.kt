package com.orangebox.kit.core.boot

import com.orangebox.kit.core.configuration.ConfigurationService
import io.quarkus.runtime.Startup
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@Startup
@ApplicationScoped
class OKCoreBoot {

    @Inject
    private lateinit var configurationService: ConfigurationService


    @PostConstruct
    fun run() {
        try {
            //http://patorjk.com/software/taag/#p=display&f=Doom&t=OrangeKit%200.1
            val x = " _____                            _   ___ _     _____  _____ \n" +
                    "|  _  |                          | | / (_) |   |  _  ||  _  |\n" +
                    "| | | |_ __ __ _ _ __   __ _  ___| |/ / _| |_  | |/' || |/' |\n" +
                    "| | | | '__/ _` | '_ \\ / _` |/ _ \\    \\| | __| |  /| ||  /| |\n" +
                    "\\ \\_/ / | | (_| | | | | (_| |  __/ |\\  \\ | |_  \\ |_/ /\\ |_/ /\n" +
                    " \\___/|_|  \\__,_|_| |_|\\__, |\\___\\_| \\_/_|\\__|  \\___(_)\\___/ \n" +
                    "                        __/ |                                \n" +
                    "                       |___/                                 "
            println(x)
            println("🍊")
            println("Powered by\n\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}