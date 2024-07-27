package com.bg.imagehoster.service;

import com.bg.imagehoster.model.entity.ImageDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

@Service
public class ImageService {
    @Autowired
    ImageCreateResponse imageCreateResponse;

    public ImageCreateResponse createImage(ImageDTO image) throws ExecutionException, InterruptedException, IOException {
        String tempUrl = image.getImageUrl();
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docReference = firestore.collection("images").document();
        image.setId(docReference.getId());

        String imageUrl = image.getImageUrl();
        String storageUrl = uploadImageToFirebaseStorage(imageUrl, image.getId());

        image.setImageUrl(storageUrl);

        ApiFuture<WriteResult> apiFuture = docReference.set(image);
        imageCreateResponse.setId(image.getId());
        imageCreateResponse.setUrl(storageUrl);
        imageCreateResponse.setOldUrl(tempUrl);

        return imageCreateResponse;
    }

    private String uploadImageToFirebaseStorage(String imageUrl, String imageId) throws IOException {
        InputStream in = new URL(imageUrl).openStream();
        byte[] imageBytes = in.readAllBytes();
        in.close();

        String blobString = "images/" + imageId + "/image.jpg";

        StorageClient.getInstance().bucket().create(blobString, imageBytes);

        return "https://storage.googleapis.com/" + StorageClient.getInstance().bucket().getName() + "/" + blobString;
    }
}

