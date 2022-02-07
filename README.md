# Neo4j Toolkit Plugin

A handful of useful functions for use with neo4j.

Current Provides: 
* `diff`: diff two maps and return a new map of changed keys and values

## Usage

### Diff
```cypher
RETURN toolkit.diff({id: 42, key: 'deom'}, {other:50, key: 'demo'}) as diff
// diff is { other: 50, key: 'demo }
```

## Building

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    `mvn clean package`

This will produce a jar-file at`target/toolkit-x.x.x-SNAPSHOT.jar`,
that can be deployed into the `plugin` directory of your Neo4j instance.
