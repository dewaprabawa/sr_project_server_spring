package learn_rest.example.tell_me_this_will_work.firebaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import learn_rest.example.tell_me_this_will_work.TellMeThisWillWorkApplication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseConfig implements IImageService{

   static public FirebaseApp initFirebase() throws FileStorageException, FailedToSetCredentialsException {
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

    @Override
    public String getImageUrl(String name) {
        return String.format("https://firebasestorage.googleapis.com/srproject-18d93.appspot.com/%s", name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }

    /*
    public String uploadImageToDB(MultipartFile multipartFile) throws IOException {

        String imageName = generateFileName(multipartFile.getOriginalFilename());
        File file = this.convertToFile(multipartFile, imageName);                      // to convert multipartFile to File
        var TEMP_URL = this.uploadFile(file, imageName);                                   // to get uploaded file link
        file.delete();
        return TEMP_URL;
    }

     public String uploadFile(File file, String filename) throws IOException {

         Map<String, String> map = new HashMap<>();
         map.put("firebaseStorageDownloadTokens", filename);
         BlobId blobId = BlobId.of("srproject-18d93.appspot.com", filename);
         BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map)
                 .setContentType("media")
                 .build();
         Credentials credentials = GoogleCredentials.fromStream(generateServiceAccount());
         StorageClient.getInstance().bucket().create(String.valueOf(blobInfo), Files.readAllBytes(file.toPath()));
         return String.format("https://firebasestorage.googleapis.com/srproject-18d93.appspot.com/%s", URLEncoder.encode(filename, StandardCharsets.UTF_8));
     }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }*/

    private static FileInputStream generateServiceAccount() throws FileNotFoundException {
        ClassLoader classLoader = TellMeThisWillWorkApplication.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        FileInputStream serviceAccount =
                new FileInputStream(file);
        return serviceAccount;
    }



    @Override
    public byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {
        return IImageService.super.getByteArrays(bufferedImage, format);
    }



}
