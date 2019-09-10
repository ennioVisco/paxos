package App;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemAppTest {

    @Test
    void run() {
        FileSystemApp.main(new String[] {"-role","s"});
    }
}