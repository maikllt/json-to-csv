## small app to convert JSON to CSV

compile and then provide values for the following command instead of 'input.json', 'output.csv', 'json-properties.txt':

```bash
mvn compile 

mvn exec:java -Dexec.args="input.json output.csv property_names.txt"
```

<br>
note:

> to parse a JSON, you should put property names to be parsed in a separate lines of file (last one in the above command 'json-properties.txt')