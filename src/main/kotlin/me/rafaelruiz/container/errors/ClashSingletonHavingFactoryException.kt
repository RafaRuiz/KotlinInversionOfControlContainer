package me.rafaelruiz.container.errors

import java.lang.RuntimeException

class ClashSingletonHavingFactoryException : RuntimeException(
    "Impossible to register this Factory. You've got a Singleton with the same definition."
)