package learn_rest.example.tell_me_this_will_work.firebaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import learn_rest.example.tell_me_this_will_work.TellMeThisWillWorkApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FirebaseConfig {

    public static FirebaseApp initFirebase() throws FileStorageException, FailedToSetCredentialsException {
        FileInputStream serviceAccount;
        try {
           serviceAccount = generateServiceAccount();
        } catch (FileNotFoundException e) {
            throw new FileStorageException(e.toString());
        }
        FirebaseOptions options;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).setStorageBucket("srproject-18d93.appspot.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailedToSetCredentialsException(e.toString());
        }

        return FirebaseApp.initializeApp(options);
    }

     public String uploadFileToStorage(MultipartFile file) throws IOException {
         String imageName = generateFileName(file.getOriginalFilename());
         Map<String, String> map = new HashMap<>();
         map.put("firebaseStorageDownloadTokens", imageName);
         BlobId blobId = BlobId.of("srproject-18d93.appspot.com", imageName);
         BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                 .setMetadata(map)
                 .setContentType(file.getContentType())
                 .build();
         StorageClient.getInstance().bucket().create(String.valueOf(blobInfo),file.getInputStream());
         return imageName;
     }

    private static FileInputStream generateServiceAccount() throws FileNotFoundException {
        ClassLoader classLoader = TellMeThisWillWorkApplication.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        FileInputStream serviceAccount =
                new FileInputStream(file);
        return serviceAccount;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
