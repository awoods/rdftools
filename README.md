# RDF Tools

This commandline utility is for comparing any two user-provided RDF files.

## Build

After cloning this project, the tool can be built with the following Maven command:
```
mvn clean install
```

## Usage

```
Usage: rdftools [-hV] [--debug] -a=<rdfA> -b=<rdfB>
Compare two RDF files, serializations inferred from file extensions
  -h, --help                Show this help message and exit.
  -V, --version             Print version information and exit.
  -a, --rdf-file-A=<rdfA>   First RDF file in comparison
  -b, --rdf-file-B=<rdfB>   Second RDF file in comparison
      --debug               Enables debug logging
```

Example execution:
```
java -jar target/rdftools-1.0-SNAPSHOT.jar -a /tmp/rdf/academicDegree.n3 -b /tmp/rdf/academicDegree.rdf
```

## Output

The output of this tool is to the terminal window. Two sets of triples are printed:
- All triples that are in the first RDF model but not in the second are printed, followed by
- All triples that are in the second RDF model but not in the first are printed

If the `--debug` flag is provide, the RDF models associated with the original files are printed.
