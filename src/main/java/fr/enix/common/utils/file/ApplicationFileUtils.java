package fr.enix.common.utils.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static java.nio.file.Files.readString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationFileUtils {

    public static String getStringFileContentFromResources(final String filepath) throws IOException, URISyntaxException {
        return readString( Paths.get( ClassLoader.getSystemResource(filepath).toURI() ) );
    }
}
