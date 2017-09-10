package org.vsanyc.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.Sequences;
import org.jooq.util.maven.example.tables.Author;
import org.jooq.util.maven.example.tables.records.AuthorRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Created by vano on 11.9.17.
 */
public class Main {
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/jooq-example";
    private static final String NAME_PREFIX = "AuthorName_";

    public static void main(String[] args) {


        try (Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            fetch(conn);
            createAuthor(conn, NAME_PREFIX + System.nanoTime());
        }

        // For the sake of this tutorial, let's keep exception handling simple
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fetch(Connection conn) {
        DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
        Result<Record> result = create.select().from(Author.AUTHOR).fetch();
        for(Record rec : result) {
            System.out.println("author: " + rec.getValue(Author.AUTHOR.NAME));
        }

    }

    private static void createAuthor(Connection conn, String name) {
        DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
        Long id = create.select(Sequences.AUTHOR_SEQ.nextval()).fetchOne().value1();
        AuthorRecord author = create.newRecord(Author.AUTHOR);
        author.setId(id);
        author.setName(name);
        author.store();
    }
}
