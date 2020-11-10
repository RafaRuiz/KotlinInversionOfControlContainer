# KotlinInversionOfControlContainer
Example of how to make an Inversion of Control Container.

The Container can register Singletons, Factories and Scopes.

### Example (Register - Resolve):

- You can't register a Factory or a Singleton given the simplicity of the container.
- Registering a Factory of a `TestClass` will generate two different `TestClass` instances every single time it's resolved.
- Registering a Singleton of a `TestClass` will generate the same instance every single time it's resolved.
- Registering a Scope is independent of registering a Factory or registering a Singleton. When scoping, you want to maintain some uniqueness according to the scopes. In this case, the uniqueness is given by a `String`. This means that if you register a Scope of a `TestClass` and scope it with `"ABC"`, that instance will be the same to a future scoped instance as `"ABC"` but will be different to a scoped instance as `"DEF"`.

### Example (Dispose):
- When disposing a Factory/Singleton (you can't register both at the same time). That will leave the container in the same state than if it was never registered.
- When disposing a Scope, you can either dispose the rule itself (meaning that you won't be able to scope that `TestClass` anymore). This is the same behaviour than the previous disposing.
- When disposing a scoped instance, you can ask for a previous scoped instance (same unique `String`), and it will result into a new instance.
- When disposing all scoped instances, you will get into the same point than when registering the Scope itself.
