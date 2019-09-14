package App;

import Base.Decree;

public class FileOperation implements Decree {
    private String operation;

    public FileOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
