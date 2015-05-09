package org.wkh.fastblog.services;

import org.apache.avro.Schema;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PostSchemaService {
    private final Schema schema;

    public PostSchemaService() throws Exception {
        final String schemaPath = "post_schema.json";

        final Schema.Parser parser = new Schema.Parser();
        InputStream schemaStream = PostCreationService.class.getClassLoader().getResourceAsStream(schemaPath);
        schema = parser.parse(schemaStream);
    }


    public Schema getSchema() {
        return schema;
    }
}
