package me.rafaelruiz.container.errors

import java.lang.RuntimeException

/**
 * When the Singleton or Factory can't be found
 */
class NoRegisterNoScopeDefinition :
    RuntimeException("No register definition for this class. Did you register it as a Singleton or a Factory?")