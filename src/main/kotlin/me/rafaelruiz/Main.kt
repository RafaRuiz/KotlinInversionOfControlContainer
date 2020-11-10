package me.rafaelruiz

import me.rafaelruiz.container.IoCContainer
import me.rafaelruiz.container.exampleclasses.ExampleClass1
import me.rafaelruiz.container.exampleclasses.ExampleClass2
import me.rafaelruiz.container.exampleclasses.ExampleClass3

fun main() {
    val container = IoCContainer.getInstance()

    // Demo of registering and resolving singletons
    println("Registering a Singleton of ${ExampleClass1::class}...")
    container.registerSingleton { ExampleClass1("example class 1") }

    println("Get the hashcode for ${ExampleClass1::class} when resolving it (1): ")
    println(
        container.resolve<ExampleClass1>().hashCode()
    )
    println("Get the hashcode for ${ExampleClass1::class} when resolving it (2): ")
    println(
        container.resolve<ExampleClass1>().hashCode()
    )

    // Demo of registering and resolving factories
    println("Registering a Factory of ${ExampleClass2::class}...")
    container.registerFactory { ExampleClass2("example class 2") }

    println("Get the hashcode for ${ExampleClass2::class} when resolving it (1): ")
    println(
        container.resolve<ExampleClass2>().hashCode()
    )
    println("Get the hashcode for ${ExampleClass2::class} when resolving it (2): ")
    println(
        container.resolve<ExampleClass2>().hashCode()
    )

    // Demo of registering and resolving scopes
    println("Registering a Scope of ${ExampleClass3::class}...")
    container.registerScope { ExampleClass3("example class 3") }

    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'abc': ")
    println(
        container.resolve<ExampleClass3>("abc").hashCode()
    )
    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'abc': ")
    println(
        container.resolve<ExampleClass3>("abc").hashCode()
    )
    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'def': ")
    println(
        container.resolve<ExampleClass3>("def").hashCode()
    )

    // Demo of disposing scopes
    println("Disposing the 'def' scope of ${ExampleClass3::class}...")
    container.disposeScoped<ExampleClass3>("def")
    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'def': ")
    println(
        container.resolve<ExampleClass3>("def").hashCode()
    )
    println("Disposing all scopes of ${ExampleClass3::class}...")
    container.disposeAllScoped<ExampleClass3>()
    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'abc': ")
    println(
        container.resolve<ExampleClass3>("abc").hashCode()
    )
    println("Get the hashcode for ${ExampleClass3::class} when resolving it for scope 'def': ")
    println(
        container.resolve<ExampleClass3>("def").hashCode()
    )
}