package mudmap2.backend.WorldFileReader.current;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mudmap2.backend.PlaceGroup;
import mudmap2.backend.Layer;
import mudmap2.backend.Path;
import mudmap2.backend.Place;
import mudmap2.backend.InformationColor;
import mudmap2.backend.World;
import mudmap2.backend.WorldCoordinate;
import mudmap2.backend.WorldFileReader.WorldFile;
import mudmap2.backend.WorldFileReader.Exception.WorldFileInvalidTypeException;
import mudmap2.backend.WorldFileReader.Exception.WorldFileReadError;
import mudmap2.backend.WorldFileReader.WorldFileType;
import org.json.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static mudmap2.backend.WorldFileReader.current.WorldFileJSON.*;

public class WorldFileJSONBlockTest {

    @Test
    public void testLine353() throws Exception {
        Place place = new Place("foo", 1, 2, new Layer(new World()));
        JSONObject jPlace = new JSONObject().put("co", new JSONArray().put("First line").put("Second line"));
        JSONArray comments = jPlace.getJSONArray("co");
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("separator");
        if (separator == null || separator.isEmpty()) {
            separator = "\r\n";
        }
        for (int c = 0; c < comments.length(); ++c) {
            if (c > 0) {
                builder.append(separator);
            }
            builder.append(comments.getString(c));
        }
        place.setComments(builder.toString());
        assertEquals("First line\r\nSecond line", place.getComments());
    }

    @Test
    public void testLine355() throws Exception {
        Place place = new Place("foo", 1, 2, new Layer(new World()));
        JSONObject jPlace = new JSONObject().put("co", new JSONArray().put("First line"));
        JSONArray comments = jPlace.getJSONArray("co");
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("separator");
        if (separator == null || separator.isEmpty()) {
            separator = "\r\n";
        }
        for (int c = 0; c < comments.length(); ++c) {
            if (c > 0) {
                builder.append(separator);
            }
            builder.append(comments.getString(c));
        }
        place.setComments(builder.toString());
        assertEquals("First line", place.getComments());
    }

    @Test
    public void testLine357() throws Exception {
        Place place = new Place("foo", 1, 2, new Layer(new World()));
        JSONObject jPlace = new JSONObject().put("co", new JSONArray().put("First line").put("Second line"));
        System.setProperty("separator", "|");
        JSONArray comments = jPlace.getJSONArray("co");
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("separator");
        if (separator == null || separator.isEmpty()) {
            separator = "\r\n";
        }
        for (int c = 0; c < comments.length(); ++c) {
            if (c > 0) {
                builder.append(separator);
            }
            builder.append(comments.getString(c));
        }
        place.setComments(builder.toString());
        assertEquals("First line|Second line", place.getComments());
    }

    @Test
    public void testLine361() throws Exception {
        Place place = new Place("foo", 1, 2, new Layer(new World()));
        JSONObject jPlace = new JSONObject().put("co", new JSONArray().put("First line").put("Second line"));
        System.setProperty("separator", "");
        JSONArray comments = jPlace.getJSONArray("co");
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("separator");
        if (separator == null || separator.isEmpty()) {
            separator = "\r\n";
        }
        for (int c = 0; c < comments.length(); ++c) {
            if (c > 0) {
                builder.append(separator);
            }
            builder.append(comments.getString(c));
        }
        place.setComments(builder.toString());
        assertEquals("First line\r\nSecond line", place.getComments());
    }

    @Test
    public void testLine363() throws Exception {
        Place place = new Place("foo", 1, 2, new Layer(new World()));
        JSONObject jPlace = new JSONObject().put("co", new JSONArray());
        JSONArray comments = jPlace.getJSONArray("co");
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("separator");
        if (separator == null || separator.isEmpty()) {
            separator = "\r\n";
        }
        for (int c = 0; c < comments.length(); ++c) {
            if (c > 0) {
                builder.append(separator);
            }
            builder.append(comments.getString(c));
        }
        place.setComments(builder.toString());
        assertEquals("", place.getComments());
    }
}
