package writers;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class PerPersonWriter {
    private Map<Id<Person>, Map<String, Double>> perPersonItems;

    public PerPersonWriter(Map<Id<Person>, Map<String, Double>> perPersonItems) {
        this.perPersonItems = perPersonItems;
    }

    public void write(String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)));

        boolean headerWritten = false;

        for (Id<Person> personId : this.perPersonItems.keySet()) {
            if (!headerWritten) {
                writer.write(formatHeader(this.perPersonItems.get(personId)) + "\n");
                writer.flush();
                headerWritten = true;
            }
            writer.write(formatItem(personId, this.perPersonItems.get(personId)) + "\n");
            writer.flush();
        }

        writer.flush();
        writer.close();

    }

    private String formatHeader(Map<String, Double> attributes) {
        String[] header = new String[attributes.keySet().size() + 1];
        header[0] = "personId";
        int i = 1;
        for (String attribute : attributes.keySet()) {
            header[i] = attribute;
            i++;
        }
        return String.join(";", header);
    }

    private String formatItem(Id<Person> personId, Map<String, Double> attributes) {
        String[] entry = new String[attributes.values().size() + 1];
        entry[0] = personId.toString();
        int i = 1;
        for (double value : attributes.values()) {
            entry[i] = Double.toString(value);
            i++;
        }
        return String.join(";", entry);
    }
}
