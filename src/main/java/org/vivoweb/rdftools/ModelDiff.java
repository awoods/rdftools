package org.vivoweb.rdftools;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static org.slf4j.LoggerFactory.getLogger;

public class ModelDiff {

    private static final Logger LOGGER = getLogger(ModelDiff.class);

    private Model modelA;
    private Model modelB;

    public ModelDiff(File rdfA, File rdfB) {
        modelA = RDFDataMgr.loadModel(rdfA.getAbsolutePath());
        modelB = RDFDataMgr.loadModel(rdfB.getAbsolutePath());
    }

    public void difference() throws IOException {
        if (LOGGER.isDebugEnabled()) {
            write("Model A", modelA);
            write("Model B", modelB);
        }

        write("Model A - B", modelA.difference(modelB));
        write("Model B - A", modelB.difference(modelA));
    }

    private void write(String message, Model model) throws IOException {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            streamWriter.write("=========================\n");
            streamWriter.write(message + ":\n");
            model.write(streamWriter, "N-TRIPLE");
            LOGGER.info("{}", outputStream);
        }
    }
}
