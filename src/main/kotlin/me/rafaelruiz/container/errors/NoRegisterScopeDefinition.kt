package me.rafaelruiz.container.errors

import java.lang.RuntimeException

/**
 * When the scope can't be found
 */
class NoRegisterScopeDefinition :
    RuntimeException("No register definition for this class. Did you register it as a Singleton or a Factory?")