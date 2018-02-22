package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux
import java.util.function.Function

fun String.isRude(): Boolean =
    listOf("asshole", "bitch", "cunt", "fuck", "shit", "slut", "whore")
        .find { this.contains(it) } != null

@SpringBootApplication
class App {

  /**
   * echo '["I said, fuck this shit"]' | http :8080
   * echo '["I said","fuck this shit"]' | http :8080
   */
//  /*
  @Bean
  fun up(): Function<Flux<String>, Flux<String>> =
      Function { flux ->
        flux
            .map { it.split("\\s+".toRegex()) }
            .flatMap { Flux.fromIterable(it) }
            .map { it.split(",", ", ") }
            .flatMap { Flux.fromIterable(it) }
            .filter { !it.isRude() }
            .filter { it.isNotBlank() }
            .reduce { first, second -> "$first $second" }
            .flatMapMany { Flux.just(it) }
      }
//  */

  /**
   * http :8080 value=I\ said,\ fuck\ this\ shit!
   * echo '[{"value":"I said, fuck this shit!"}]' | http :8080
   * echo '[{"value":"I said"},{"value":"fuck this shit!"}]' | http :8080
   */
  /*
  @Bean
  fun map(): Function<Map<String, String>, String> =
      Function { map ->
        map.values
            .apply { forEach { println("${it::class.java} debug: $it") } }
            .flatMap { it.split("\\s+".toRegex()) }
            .flatMap { it.split(",", ", ") }
            .filter { !it.isRude() }
            .filter { it.isNotBlank() }
            .onEach { println(it) }
            //.reduce { first, second -> "$first $second" }
            .joinToString(" ")
      }
  */

  /**
   * http :8080 value=I\ said,\ fuck\ this\ shit!
   * echo '[{"value":"I said, fuck this shit!"}]' | http :8080
   * echo '[{"value":"I said"},{"value":"fuck this shit!"}]' | http :8080
   */
  /*
  data class MyClass(val value: String = "")
  @Bean
  fun myClass(): Function<MyClass, MyClass> =
      Function { myClass -> MyClass(myClass.value.toUpperCase()) }
  */

}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
