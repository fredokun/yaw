<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
**Table of Contents**

- [The problem](#the-problem)
    - [Reactive loop](#reactive-loop)
        - [Components](#components)
        - [State store(s)](#state-stores)
    - [Game loop](#game-loop)
- [The proposals](#the-proposals)
    - [Approach A : the Unity style](#approach-a--the-unity-style)
        - [Unity's way to do frame update](#unitys-way-to-do-frame-update)
        - [How this would work for us](#how-this-would-work-for-us)
    - [Approach B : Subscribers](#approach-b--subscribers)
    - [Common elements](#common-elements)
        - [Components](#components-1)
        - [Events](#events)

<!-- markdown-toc end -->

# The problem

## Reactive loop

Our current aim is to recreate a reactive loop. One very much like [React](https://reactjs.org/)'s.

### Components

React itself has two flavors: its `components` can either be pure functions, or classes, though, the classes have the lifecycle features we are interested in. In both cases, the core of a `component` is a rendering function taking in any kind and number of properties, and outputing html.

```
            +-----------+
[props]---->| COMPONENT +---->[html]
            +-----------+
```

These functions are composed into one mega component, that ends up being *the* app's rendering function.

Now, the interesting thing is when we want an actual responsive *app* where the input `props` evolve. This is where the component's state comes in, in the class flavor of React. Properties aren't actually passed to the render function, then, but accessed through `this.props`. Those are **initial values** passed as *"arguments"* to the component. The **state** is modified and accessed through `this.state`, and thus you would render html code with handlers to html classic event (button click, selection, text input, etc) that would modify `this.state` (or delegate to the containing component). The next call to *the* render function (triggered whenever an event is fired), will output another DOM, which will replace the current html tree.

This is very html specific and cannot be applied to our case, and in most React apps, it is not used much either: *what if a component needs to change according to an event, but its container has no idea about it?*

This is where [Flux](https://facebook.github.io/flux/), [Redux](https://redux.js.org/), or [MobX](https://mobx.js.org/), interest us.

### State store(s)

Redux and the gang are state management libraries that all follow *to their core* the same data flow.

```
              [state]
                 |
                 v
            +--------+                        +--------+
[event]---->| ACTION +----[updated state]---->| RENDER |---->[html]
   ^        +--------+                        +--------+       |
   |                                                           |
   +------------------------[dispatch]-------------------------+
```

Some of these libraries are designed with multiple state stores in mind, some handle one giant store with functions to combine and split it for each component.

Regardless of the pattern used to dispatch events to components, there is always the idea that components *subscribe* to *events* and perform transformative *actions* on the(ir) state accordingly.

The rendered html code has ways to trigger events through DOM event handlers, using *some dispatch function* either local or global depending on the state management of the library. The DOM event handlers roughly look like `onClick={(e) => dispatch(EVENT)}` where `EVENT` is an object describing what happened, to be later dispatched to the action handler that will modify the state.

## Game loop

The other problem we have is that we want the reactive loop to include some way to regularly update the state, and render the scene.

# The proposals

Regardless of implementation, we will have user defined components provided with user rendering functions, user state transformative functions, and we will have event handlers and dispatchers.

## Approach A : the Unity style

### Unity's way to do frame update

[Unity3D's lifecycle](https://docs.unity3d.com/Manual/ExecutionOrder.html) has the user components implement an `update` method that will itself check for the state of everything through a lot of helper functions to find things in the scene or the game engine state.

In this `update` method, the components can check what keys are pressed, where the mouse is, whether the local collider has a hit or not, and more infos.

### How this would work for us

Each component would have one single transformation function that would be the frame update function.

There would be a world-wide event collector that would collect all dispatched events, which would then be accessible to each components' update function as a list of active events.

The update function would also have access to a delta-time info: the elapsed time since the last frame update.

## Approach B : Subscribers

Another way of updating the frame is to have the render function called at regular intervals, and have the component do its updating whenever it has to.

There would be a world-wide event dispatcher that would immediately (upon some event.dispatch call) call subscribers handlers to update their state and do what they need to do.

There would be some built-in `FrameUpdate` event, loaded with the same delta-time as before, that self-moving objects could subscribe to if they need to. The other events would be triggered by OpenGL click or keypress handlers.

## Common elements

### Components

Components would not be classes/objects like in Unity and React's cases, but keyword-maps of the form

```clojure
{ :render (fn [user-state] {$$( hi-level 3d scene object with info about GLevents handling )$$})
  :update $$(update functions)$$
}
```

The specifications of the `:update` value depend on how we decide to do frame update.

I must admit I don't really know how the subscribers pattern would affect this, actually.

### Events

Regardless of dispatch and update implementation, it is ideal to have some event objects representing user-defined semantics about what just happened.

We could go for simple namespaced keywords like `:move/jump`, but that prevents the existence of more contextual events like object selection.

This use-case is perfect for abstract data types like ocaml's enums, but we don't have that, so we could settle for, once again, a simple spec-less record:

```clojure
{ :type :selection/change
  :target some-user-defined-id
}
```

This would be easy to destructure with `{t :type & payload}`
