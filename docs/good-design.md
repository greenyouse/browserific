# Learnable Programming
The environment should allow the learner to:

    read the vocabulary -- what do these words mean?
    follow the flow -- what happens when?
    see the state -- what is the computer thinking?
    create by reacting -- start somewhere, then sculpt
    create by abstracting -- start concrete, then generalize

The language should provide:

    identity and metaphor -- how can I relate the computer's world to my own?
    decomposition -- how do I break down my thoughts into mind-sized pieces?
    recomposition -- how do I glue pieces together?
    readability -- what do these words mean?
    
# Smalltalk Ideas
## General

    Personal Mastery: If a system is to serve the creative spirit,
    it must be entirely comprehensible to a single individual.

    Good Design: A system should be built with a minimum set of
    unchangeable parts; those parts should be as general as possible;
    and all parts of the system should be held in a uniform framework.

## Language

    Purpose of Language: To provide a framework for communication.

    Scope: The design of a language for using computers must deal with
    internal models, external media, and the interaction between these
    in both the human and the computer.

## Comunicating Objects

    Objects: A computer language should support the concept of "object"
    and provide a uniform means for referring to the objects in its
    universe.

    Storage Management: To be truly "object-oriented", a computer
    system must provide automatic storage management.

    Messages: Computing should be viewed as an intrinsic capability of
    objects that can be uniformly invoked by sending messages.

    Uniform Metaphor: A language should be designed around a powerful
    metaphor that can be uniformly applied in all areas.

## Organization

    Modularity: No component in a complex system should depend on the
    internal details of any other component.

    Classification: A language must provide a means for classifying
    similar objects, and for adding new classes of objects on equal
    footing with the kernel classes of the system.

    Polymorphism: A program should specify only the behavior of
    objects, not their representation.

    Factoring: Each independent component in a system would appear in
    only one place.

    Leverage: When a system is well factored, great leverage is
    available to users and implementers alike.

    Virtual Machine: A virtual machine specification establishes a
    framework for the application of technology.

## UI

    Reactive Principle: Every component accessible to the user should
    be able to present itself in a meaningful way for observation and
    manipulation.

    Operating System: An operating system is a collection of things
    that don't fit into a language. There shouldn't be one.

# Mindstorm

```
"We are in the process of digging ourselves into an anachronism by
preserving practices that have no rational basis beyond their
historical roots in an earlier period of technological and theoretical
development."
```

Ex: BASIC was the go-to language for teaching programming to newbies
even though there were much easier languages that could offer greater
intellectual benefits. It was initially used because it didn't require
much computing power. Once BASIC became commonplace to teach, an
irrational cycle of teaching BASIC was formed, even though
technological advancements had done away with the original computational
limitiations that caused BASIC to be used in the first place.


Modern extension/app development should be packaged into a uniform build
process and apps should be data-driven, easy to build, built in pieces
(swappable widgets/modules), feature complete (ability to call _all_ native
features, not just a least common denominator of various systems, using a 
protocol similar to Common LISP feature expressions), abstract (don't program
like C or JS, go higher-level and leave the details to the build tool), 
simple debugging (build errors and source maps?),

Having an extensible build tool would be nice (add your own plugins that
hook into the build process to do things like offering new ways to build
widgets or process data) but this is hard to reason about how to implement.
People will just need to conform to my library API and send suggestions on
ways to change it for now. What I mean by extensible build tool is something
similar to LISP macros, Smalltalk objects, or Cordova/PhoneGap plugins.
