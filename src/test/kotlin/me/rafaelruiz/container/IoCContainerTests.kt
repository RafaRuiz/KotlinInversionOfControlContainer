package me.rafaelruiz.container

import me.rafaelruiz.container.errors.ClashFactoryHavingSingletonException
import me.rafaelruiz.container.errors.ClashSingletonHavingFactoryException
import me.rafaelruiz.container.errors.NoRegisterNoScopeDefinition
import me.rafaelruiz.container.errors.NoRegisterScopeDefinition
import me.rafaelruiz.container.testclasses.TestClass
import org.junit.Before
import org.junit.Test

class IoCContainerTests {

    lateinit var container: IoCContainer

    @Before
    fun setUp() {
        container = IoCContainer()
    }

    @Test
    fun `Singleton is the same when resolved twice`() {
        container.registerSingleton { TestClass() }

        val resolved1 = container.resolve<TestClass>()
        val resolved2 = container.resolve<TestClass>()

        assert(resolved1 == resolved2)
    }

    @Test
    fun `Factory is not the same when resolved twice`() {
        container.registerFactory { TestClass() }

        val resolved1 = container.resolve<TestClass>()
        val resolved2 = container.resolve<TestClass>()

        assert(resolved1 != resolved2)
    }

    @Test
    fun `Scope is the same when resolved scoping "abc"`() {
        container.registerScope { TestClass() }

        val scope = "abc"
        val resolved1 = container.resolve<TestClass>(scope)
        val resolved2 = container.resolve<TestClass>(scope)

        assert(resolved1 == resolved2)
    }

    @Test
    fun `Scope is not the same when resolved scoping "abc" and "def"`() {
        container.registerScope { TestClass() }

        val scope1 = "abc"
        val scope2 = "def"
        val resolved1 = container.resolve<TestClass>(scope1)
        val resolved2 = container.resolve<TestClass>(scope2)

        assert(resolved1 != resolved2)
    }

    @Test(expected = ClashSingletonHavingFactoryException::class)
    fun `Singleton can't be registered if Factory is registered`() {
        container.registerFactory { TestClass() }

        container.registerSingleton { TestClass() }
    }

    @Test(expected = ClashFactoryHavingSingletonException::class)
    fun `Factory can't be registered if Singleton is registered`() {
        container.registerSingleton { TestClass() }

        container.registerFactory { TestClass() }
    }

    @Test
    fun `Scope can be registered no matter if Factory is registered`() {
        container.registerScope { TestClass() }

        container.registerFactory { TestClass() }
    }

    @Test
    fun `Scope can be registered no matter if Singleton is registered`() {
        container.registerScope { TestClass() }

        container.registerSingleton { TestClass() }
    }

    @Test
    fun `Factory can be registered no matter if Scope is registered`() {
        container.registerFactory { TestClass() }

        container.registerScope { TestClass() }
    }

    @Test
    fun `Singleton can be registered no matter if Scope is registered`() {
        container.registerSingleton { TestClass() }

        container.registerScope { TestClass() }
    }

    @Test(expected = NoRegisterNoScopeDefinition::class)
    fun `Resolving crashes if it is not registered`() {
        container.resolve<TestClass>()
    }

    @Test(expected = NoRegisterScopeDefinition::class)
    fun `Resolving scope crashes if it is not registered`() {
        container.resolve<TestClass>("abc")
    }

    @Test(expected = NoRegisterNoScopeDefinition::class)
    fun `registering Factory, disposing it and resolving it will crash`() {
        container.registerFactory { TestClass() }

        container.dispose<TestClass>()

        container.resolve<TestClass>()
    }

    @Test(expected = NoRegisterNoScopeDefinition::class)
    fun `registering Singleton, disposing it and resolving it will crash`() {
        container.registerSingleton { TestClass() }

        container.dispose<TestClass>()

        container.resolve<TestClass>()
    }

    @Test(expected = NoRegisterScopeDefinition::class)
    fun `registering Scope, disposing it and resolving it will crash`() {
        container.registerScope { TestClass() }

        container.dispose<TestClass>()

        container.resolve<TestClass>("abc")
    }

    @Test
    fun `registering Scope, scoping one and disposing that specific one will bring a new one`() {
        val scopeDefinition = "abc"
        container.registerScope { TestClass() }

        val scoped1 = container.resolve<TestClass>(scopeDefinition)

        container.disposeScoped<TestClass>(scopeDefinition)

        val scoped2 = container.resolve<TestClass>(scopeDefinition)

        assert(scoped1 != scoped2)
    }

    @Test
    fun `registering Scope and scoping a few, then disposing them will dispose them all`() {
        val scopeDefinition1 = "abc"
        val scopeDefinition2 = "def"
        container.registerScope { TestClass() }

        val scoped1 = container.resolve<TestClass>(scopeDefinition1)
        val scoped2 = container.resolve<TestClass>(scopeDefinition2)

        container.disposeAllScoped<TestClass>()

        val scopedAfter1 = container.resolve<TestClass>(scopeDefinition1)
        val scopedAfter2 = container.resolve<TestClass>(scopeDefinition2)

        assert(scoped1 != scopedAfter1)
        assert(scoped2 != scopedAfter2)
    }
}