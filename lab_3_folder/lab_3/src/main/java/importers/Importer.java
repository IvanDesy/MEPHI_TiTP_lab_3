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
    
    public void importFile(File file, ReactorsHolder reactorMap) throws IOException{
        if (isTrueType(file)) {
         inputCode(file, reactorMap);   
        } else if (next != null) {
            next.importFile(file, reactorMap);
        } else {
            System.out.println("Unsupported file format");
        }
    }
    
    protected abstract void inputCode(File file, ReactorsHolder reactorMap);
    
    protected abstract boolean isTrueType(File file);
}