![](https://github.com/weol/parameters/workflows/Maven%20Deployment/badge.svg)

This is a command-line parsing package for Java. I created it for use in hobby projects a long time ago and I've had multiple different versions of it lying around in various projects. I decided to clean up and document the latest version of it and upload it to here in order to have a consistent version of it and so that I don't need to rummage through my projects folder whenever I need it for a new project.


The package conists of three main components, the various types of flags (parameters), the parameter parser (interpreter), and the parsed parameters (interpretation). The flags denote which parameters the parser looks for and how it parses them. The parsed parameters are then stored in a interpretation that is used to retrieve the parsed parameters or check if they are present. A simple example from one of my projects (a chess robot):
```java
ParameterInterpreter interpreter = new ParameterInterpreter(
    new CollectionFlag<>("blackAgent", "b", "The agent of the black player", agentList, true),
    new CollectionFlag<>("whiteAgent", "w", "The agent of the white player", agentList, true),
    new CollectionFlag<>("blackHeuristic", "bh", "Black agent heuristic", heuristicList),
    new CollectionFlag<>("whiteHeuristic", "wh", "White agent heuristic", heuristicList),
    new FunctionFlag<>("number", "n", "Number of games to play", Integer::parseInt, true),
    new FunctionFlag<>("depthlimit", "dl", "Agent algorithm depth limit", Integer::parseInt)
);
ParameterInterpretation interpretation = interpreter.interpret(args);

Heuristic blackHeuristic = interpretation.get("blackHeuristic", new SimpleHeuristic());
Heuristic whiteHeuristic = interpretation.get("whiteHeuristic", new SimpleHeuristic());

Agent blackAgentHolder = interpretation.get("black");
Agent whiteAgentHolder = interpretation.get("white");

int depthLimit = interpretation.get("depthlimit", 4);
int games = interpretation.get("number");
```
