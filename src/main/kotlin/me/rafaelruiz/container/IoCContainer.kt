package me.rafaelruiz.container

import me.rafaelruiz.container.errors.ClashFactoryHavingSingletonException
import me.rafaelruiz.container.errors.ClashSingletonHavingFactoryException
import me.rafaelruiz.container.errors.NoRegisterNoScopeDefinition
import me.rafaelruiz.container.errors.NoRegisterScopeDefinition
import kotlin.reflect.KClass

class IoCContainer {
    
    /**
     * REGISTERS
     */
    
    /**
     * Registers a singleton.
     * We also check if there's a Factory of this class.
     * Since we're keeping this very simple, we will throw
     * an error if there's also a Factory.
     * This way we will force the developer having a Factory
     * OR a Singleton
     */
    inline fun <reified T : Any> registerSingleton(noinline create: () -> T) {
        if (factoryRules.containsKey(T::class)) {
            throw ClashSingletonHavingFactoryException()
        }
        singletonRules[T::class] = lazy { return@lazy create() }
    }

    /**
     * Register a factory
     * We also check if there's a Singleton of this class.
     * Since we're keeping this very simple, we will throw
     * an error if there's also a Singleton.
     * This way we will force the developer having a Factory
     * OR a Singleton
     */
    inline fun <reified T : Any> registerFactory(noinline create: () -> T) {
        if (singletonRules.containsKey(T::class)) {
            throw ClashFactoryHavingSingletonException()
        }
        factoryRules[T::class] = create
    }

    /**
     * Registers a scope.
     * @param create contains a String to pass in order to provide the ID of the scope.
     * Example:
     * - scoping "A" will bring Instance1
     * - scoping "B" will bring Instance2
     * - scoping again "A" will bring you Instance1
     */
    inline fun <reified T : Any> registerScope(noinline create: () -> T) {
        scopeRules[T::class] = create
    }

    /**
     * RESOLVERS
     */

    /**
     * Will resolve a Singleton, a Factory
     */
    @Suppress("UNCHECKED_CAST")
    // We've checked this when adding it into the rules
    inline fun <reified T> resolve(): T {
        if (factoryRules.containsKey(T::class)) {
            val creator: () -> T = (factoryRules[T::class] as? () -> T)!!
            return creator()
        }

        if (singletonRules.containsKey(T::class)) {
            return singletonRules[T::class]!!.value as T
        }

        throw NoRegisterNoScopeDefinition()
    }

    /**
     * Will resolve a scope
     */
    @Suppress("UNCHECKED_CAST")
    // We've checked this when adding it into the rules
    inline fun <reified T> resolve(scope: String): T {
        val existingReference = scopedDefinitions[T::class to scope]
        return if (existingReference != null) {
            existingReference as T
        } else {
            val rule = scopeRules[T::class] ?: throw NoRegisterScopeDefinition()

            val newReference = (rule.invoke() as T)!!
            scopedDefinitions[T::class to scope] = newReference
            newReference
        }
    }

    /**
     * DISPOSERS
     */

    /**
     * Disposes a Factory definition, a Singleton lazy stored value,
     * or a Scope rule.
     */
    inline fun <reified T> dispose() {
        when {
            factoryRules.containsKey(T::class) -> factoryRules.remove(T::class)
            singletonRules.containsKey(T::class) -> singletonRules.remove(T::class)
            scopeRules.containsKey(T::class) -> scopeRules.remove(T::class)
        }
    }


    /**
     * Disposes one scoped param.
     * @param scope will delete the scopes given the class T for this scope
     */
    inline fun <reified T> disposeScoped(scope: String) {
        scopedDefinitions
            .keys
            .removeAll { it.first == T::class && it.second == scope }
    }

    /**
     * Disposes all the scopes given a class
     */
    inline fun <reified T> disposeAllScoped() {
        scopedDefinitions
            .keys
            .removeAll { it.first == T::class }
    }

    /**
     * Internal and helpers
     */
    
    val singletonRules = hashMapOf<KClass<*>, Lazy<Any>>()
    val factoryRules = hashMapOf<KClass<*>, () -> Any>()
    val scopeRules = hashMapOf<KClass<*>, () -> Any>()

    val scopedDefinitions = hashMapOf<Pair<KClass<*>, String>, Any>()

}