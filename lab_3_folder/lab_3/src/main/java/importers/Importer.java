package importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import masters.ReactorsHolder;
/**
 *
 * @author vanya
 */
public abstract class Importer {    
    protected Importer next;

    public void setNext(Importer next) {
        this.next = next;
    }
    
    protected abstract boolean isTrueType(File file);

    public abstract void importFile(File file, ReactorsHolder reactorMap) throws IOException;
}