# DesignPatterns_Draw
Case study to demonstrate interactions among a number of core design patterns

This repository contains a number of staged evolutions of a system, designed to showcase specific design patterns in context. Often design patterns are introduced to clean up some badly-written code. Here the initial design starts with the intention to make the system extensible. As you will see, this isn't just matter of creating some abstract classes. During the earliest stage of design in a system, you can already envision how a specific design pattern will be used.

## Entity-Boundary-ontroller

The entire system is architected to conform to a style of programming called Entity-Boundary-Controller. While this is similar to the more commonly named Model-View-Controller, it differs in a number of fundamental ways. If you are interested, review two additional github repositories:

* Tangram [https://github.com/heineman/tangram] - this case study shows a number of staged evolutions of a small application designed to support Tangrams, a two-dimensional dissection puzzle. Once the initial application is rapidly constructed, I extend the system systematically by applying design patterns to resolve coding problems encountered.
* EBC [https://github.com/heineman/ebc-example] - this repository contains several tiny EBC examples, together with a more fleshed out example for an application that gives users the ability to solve a game consisting of sliding wooden pieces.

## draw.0

The first iteration includes the following design patterns

* Singleton - `draw.tools.Tools` maintains the collection of available tools, and records which one is currently selected.
* Adapter - `draw.controller.handle.ActiveToolHandler` adapts low-level mouse events into application-domain method invocations, to simplify how to handle user interactions
* Chain of Responsibility - `draw.controller.handler.Handler` builds up a chain of event handlers that respond to the user interactions based on the active tool maintained by `Tools`.

Based on the Code Line Counting Tool [https://github.com/AlDanial/cloc] the initial system consists of

| Version    | Files     | Blank  |  Comment | Code     |
| ---------- |:---------:| ------:| --------:| --------:|
| draw.0     | 21        |  280   |  470     |  950     |

## draw.1

The second iteration includes the following design patterns

* Composite - `draw.model.Group` represents a group of Element objects that is to be treated as a single Element.
* Command - `draw.controller.command` represents the Command objects that will execute the user actions from the GUI

| Version    | Files     | Blank  |  Comment | Code     |
| ---------- |:---------:| ------:| --------:| --------:|
| draw.1     | 24        |  319   |  543     | 1104     |

## draw.2

The third iteration includes the following design patterns

* Visitor - `draw.controller.visitors` contain the Visitor classes that encapsulate new operations without requiring changes to existing classes

| Version    | Files     | Blank  |  Comment | Code     |
| ---------- |:---------:| ------:| --------:| --------:|
| draw.2     | 30        |  408   |  656     | 1436     |

