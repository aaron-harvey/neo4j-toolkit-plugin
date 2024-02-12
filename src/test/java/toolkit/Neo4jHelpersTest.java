package toolkit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jHelpersTest {

    private static final Config driverConfig = Config.builder().withoutEncryption().build();
    private Neo4j embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withFunction(Neo4jHelpers.class)
                .build();
    }

    @Test
    void mapDifference() {

        try (Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);
             Session session = driver.session()) {

            /* key changed */
            String result = session.run("RETURN toolkit.diff({id:'test', key:41}, {id:'test', key:42}) AS result").single().get("result").toString();
            assertThat(result).isEqualTo("{key: 42}");

            /* key added */
            result = session.run("RETURN toolkit.diff({id:'test'}, {id:'test', newKey:42}) AS result").single().get("result").toString();
            assertThat(result).isEqualTo("{newKey: 42}");

            /* key removed */
            result = session.run("RETURN toolkit.diff({oldKey: '1', id:'test'}, {id:'test'}) AS result").single().get("result").toString();
            assertThat(result).isEqualTo("{oldKey: NULL}");

            /* no change */
            result = session.run("RETURN toolkit.diff({id:'test', key:42}, {id:'test', key:42}) AS result").single().get("result").toString();
            assertThat(result).isEqualTo("{}");
        }
    }
}