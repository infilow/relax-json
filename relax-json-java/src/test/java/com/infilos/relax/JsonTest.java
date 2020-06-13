package com.infilos.relax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author infilos on 2020-06-13.
 */

public class JsonTest {

    static class User {
        String name;

        User() {
        }

        User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("User(%s)", name);
        }

        @Override
        public boolean equals(Object object) {
            if (object==this) return true;
            if (!(object instanceof User)) return false;

            User other = (User) object;

            return Objects.equals(this.name, other.name);
        }
    }

    static class Container<T> {
        List<T> elements;

        Container() {
        }

        Container(List<T> elements) {
            this.elements = elements;
        }

        public void setElements(List<T> elements) {
            this.elements = elements;
        }

        public List<T> getElements() {
            return elements;
        }
    }

    @Test
    public void test() {
        //Json.registerModule(null); //JsonMappers
    }

    @Test
    public void testValidJson() {
        assertTrue(Json.isValidJsonString("{\"name\":\"Anna\"}"));
        assertFalse(Json.isValidJsonString("{\"name\"\"Anna\"}"));
        assertFalse(Json.isValidJsonString("{\"name\":\"Anna}"));
    }

    @Test
    public void testString() {
        Json json = Json.from("{\"name\":\"Anna\"}");
        assertEquals("{\"name\":\"Anna\"}", json.asString());

        Json json1 = Json.from("22");
        assertEquals("22", json1.asString());

        Json json2 = Json.from("[1,2,3]");
        assertEquals("[1,2,3]", json2.asString());

        System.out.println(json2.asPrettyString());
    }

    @Test
    public void testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "A");
        map.put("2", "B");
        map.put("3", "C");

        Json json = Json.from(map);
        assertEquals(map, json.asMap());
    }

    @Test
    public void testNestedMap() {
        Map<String, Integer> inner = new HashMap<>();
        inner.put("A", 1);
        inner.put("B", 2);

        Map<String, Map> outer = new HashMap<>();
        outer.put("1", inner);
        outer.put("2", inner);

        String originString = Json.from(outer).asString();

        Map<String, Object> javaMap = Json.from(outer).asMap();
        String javaString = Json.from(javaMap).asString();

        assertEquals(originString, javaString);
        //assertEquals(originString, Json.Scala.from(javaMap).asString());
        //
        //Map<String,Object> scalaMap = Json.Scala.from(outer).asMap();
        //assertEquals(originString, Json.Scala.from(outer).asString());
        //
        //assertNotEquals(originString, Json.from(scalaMap).asString());
    }

    @Test
    public void testObject() {
        Object user = new User("Anna");
        Json json = Json.from(user);

        assertEquals(user, json.asObject(User.class));
    }

    @Test
    public void testJsonNode() throws IOException {
        JsonNode jsonNode = Json.underMapper().readTree("{\"name\":\"Anna\"}");

        Json json = Json.from("{\"name\":\"Anna\"}");

        assertEquals(jsonNode, json.asJsonNode());
    }

    @Test
    public void testJsonBytes() throws IOException {
        byte[] bytes = Json.underMapper().writeValueAsBytes(
            Json.underMapper().readTree("{\"name\":\"Anna\"}")
        );

        Json json = Json.from("{\"name\":\"Anna\"}");

        assertArrayEquals(bytes, json.asBytes());
    }

    @Test
    public void testEscape() {
        String string = "{\"name\":\"Anna\"}";

        assertEquals(string, Json.unescape(Json.escape(string)));
    }

    @Test
    public void testObjects() {
        java.util.List<User> users = new ArrayList<User>() {{
            add(new User("A"));
            add(new User("B"));
            add(new User("C"));
        }};

        Json json = Json.from(users);

        Collection<User> jsonUsers = json.asType(new TypeReference<Collection<User>>() {
        });

        assertEquals(users, jsonUsers);
    }

    @Test
    public void testType() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "A");
        map.put("2", "B");
        map.put("3", "C");

        Json json = Json.from(map);

        Map<String, String> jsonMap = json.asType(new TypeReference<Map<String, String>>() {
        });

        assertEquals(map, jsonMap);
    }

    @Test
    public void testGeneric() {
        List<User> users = new ArrayList<User>() {{
            add(new User("A"));
            add(new User("B"));
            add(new User("C"));
        }};
        Container<User> container = new Container<>(users);

        Json json = Json.from(container);

        Container<User> jsonContainer = json.asType(Json.typeOfGeneric(Container.class, User.class));

        assertArrayEquals(container.elements.toArray(), jsonContainer.elements.toArray());
    }

    @Test
    public void testCopy() {
        String string = "{\"name\":\"Anna\"}";

        Json json = Json.from(string);

        ObjectNode jsonNode1 = (ObjectNode) json.asJsonNode();
        ObjectNode jsonNode2 = (ObjectNode) json.asJsonNode();

        jsonNode1.put("name", "Bala");
        jsonNode2.put("name", "Sala");

        assertEquals(string, json.asString());
        assertEquals("{\"name\":\"Bala\"}", Json.from(jsonNode1).asString());
        assertEquals("{\"name\":\"Sala\"}", Json.from(jsonNode2).asString());
    }

    @Test
    public void testMerge() {
        String string1 = "{\"name\":\"Anna\"}";
        String string2 = "{\"age\": 22}";

        Json json1 = Json.from(string1);
        Json json2 = Json.from(string2);

        assertEquals(1, json1.merge(json1).asMap().keySet().size());

        Map<String, Object> map = json1.merge(json2).asMap();

        assertEquals(2, json1.merge(json2).asMap().keySet().size());
        assertEquals(map.get("name"), "Anna");
        assertEquals(map.get("age"), 22);
    }

    @Test
    public void testEqual() {
        String string = "{\"name\":\"Anna\"}";

        assertEquals(Json.from(string), Json.from(string));

        assertNotEquals(Json.from(string), Json.from("{\"name\":\"Sala\"}"));
    }
}