package com.orangebox.kit.core.boot

import com.orangebox.kit.core.configuration.ConfigurationService
import io.quarkus.runtime.Startup
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@Startup
@ApplicationScoped
class OKCoreBoot {

    @Inject
    private lateinit var configurationService: ConfigurationService


    @PostConstruct
    fun run() {
        try {
            //http://patorjk.com/software/taag/#p=display&f=Doom&t=OrangeKit%200.1
            val x = "\n" +
                    " _____                            _   ___ _     _____   __  \n" +
                    "|  _  |                          | | / (_) |   / __  \\ /  | \n" +
                    "| | | |_ __ __ _ _ __   __ _  ___| |/ / _| |_  `' / /' `| | \n" +
                    "| | | | '__/ _` | '_ \\ / _` |/ _ \\    \\| | __|   / /    | | \n" +
                    "\\ \\_/ / | | (_| | | | | (_| |  __/ |\\  \\ | |_  ./ /_____| |_\n" +
                    " \\___/|_|  \\__,_|_| |_|\\__, |\\___\\_| \\_/_|\\__| \\_____(_)___/\n" +
                    "                        __/ |                               \n" +
                    "                       |___/                                \n"
            println(x)
            println("🍊")
            println("Powered by")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}