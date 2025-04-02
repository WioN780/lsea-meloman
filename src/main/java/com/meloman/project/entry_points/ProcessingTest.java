package com.meloman.project.entry_points;
import com.meloman.project.data_model.Album;
import com.meloman.project.utils.DataLoader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProcessingTest {
    public static void main(String[] args) throws IOException {

            try {
            DataLoader loader = new DataLoader();

            InputStream inputStream = Main.class.getClassLoader()
                    .getResourceAsStream("com/meloman/project/data/DiscoGSdata.csv");

            if (inputStream == null) {
                throw new IOException("Artxiboa ez da aurkitu");
            }

            List<Album> albums = loader.loadAlbums(inputStream);
            System.out.println(albums.size() + " Album kargatuta");

        } catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

}


// polymorphism can be shown by displaying info of mediaitem owned or liked by a user