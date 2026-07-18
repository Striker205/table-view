import java.io.File;
import java.io.IOException;

public class App {

    public static void CreateFile() throws IOException {
        try {
            String path = System.getProperty("user.home") + File.separator + "TableW Data"; 
            File file = new File(path+"/data.txt");
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public static void main(String[] args) throws Exception {

        CreateFile();
        MainFrame frame = new MainFrame(); 
        frame.checkWindow();  
        frame.initialize();
    }
}