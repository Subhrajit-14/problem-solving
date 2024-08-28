import java.util.ArrayList;
import java.util.List;

class Delta {
    enum Type { MODIFY, INSERT, DELETE }
    
    Type type;
    int start;        // Starting position
    int length;       // Number of characters affected
    String content;   // Content for INSERT and MODIFY

    Delta(Type type, int start, int length, String content) {
        this.type = type;
        this.start = start;
        this.length = length;
        this.content = content;
    }
}

class VersionedFile {
    private String baseVersion;
    private List<List<Delta>> deltas;

    public VersionedFile(String baseVersion) {
        this.baseVersion = baseVersion;
        this.deltas = new ArrayList<>();
    }

    public void addVersion(List<Delta> delta) {
        deltas.add(delta);
    }

    public String getVersion(int versionNumber) {
        if (versionNumber == 0) return baseVersion;
        
        String currentVersion = baseVersion;
        for (int i = 0; i < versionNumber; i++) {
            currentVersion = applyDeltas(currentVersion, deltas.get(i));
        }
        return currentVersion;
    }

    private String applyDeltas(String currentVersion, List<Delta> delta) {
        StringBuilder modifiedVersion = new StringBuilder(currentVersion);
        for (Delta d : delta) {
            switch (d.type) {
                case MODIFY:
                    modifiedVersion.replace(d.start, d.start + d.length, d.content);
                    break;
                case INSERT:
                    modifiedVersion.insert(d.start, d.content);
                    break;
                case DELETE:
                    modifiedVersion.delete(d.start, d.start + d.length);
                    break;
            }
        }
        return modifiedVersion.toString();
    }

    public static void main(String[] args) {
        // Example usage
        String baseVersion = "Hello, World!";
        VersionedFile versionedFile = new VersionedFile(baseVersion);

        // Version 1: Change "World" to "Java"
        List<Delta> delta1 = new ArrayList<>();
        delta1.add(new Delta(Delta.Type.MODIFY, 7, 5, "Java"));
        versionedFile.addVersion(delta1);

        // Version 2: Insert "Awesome " at the beginning
        List<Delta> delta2 = new ArrayList<>();
        delta2.add(new Delta(Delta.Type.INSERT, 0, 0, "Awesome "));
        versionedFile.addVersion(delta2);

        // Version 3: Delete "Java"
        List<Delta> delta3 = new ArrayList<>();
        delta3.add(new Delta(Delta.Type.DELETE, 7, 4, ""));
        versionedFile.addVersion(delta3);

        // Get different versions
        System.out.println("Version 0: " + versionedFile.getVersion(0)); // "Hello, World!"
        System.out.println("Version 1: " + versionedFile.getVersion(1)); // "Hello, Java!"
        System.out.println("Version 2: " + versionedFile.getVersion(2)); // "Awesome Hello, Java!"
        System.out.println("Version 3: " + versionedFile.getVersion(3)); // "Awesome Hello, !"
    }
}
