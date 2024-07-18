package com.bg.imagehoster.controller.service;

import com.bg.imagehoster.controller.entity.ImageDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.ExecutionException;

import com.google.firebase.cloud.StorageClient;

import java.io.*;
import java.net.URL;

@Service
public class ImageService {
    @Autowired
    ImageCreateResponse imageCreateResponse;

    public ImageCreateResponse createImage(ImageDTO image) throws ExecutionException, InterruptedException, IOException {
        String tempUrl = image.getImageUrl();
        Firestore firestore = FirestoreClient.getFirestore();
        // Create a new document reference with an auto-generated ID
        DocumentReference docReference = firestore.collection("images").document();
        image.setId(docReference.getId());

        // Download image from URL and upload to Firebase Storage
        String imageUrl = image.getImageUrl();
        String storageUrl = uploadImageToFirebaseStorage(imageUrl, image.getId());

        // Set the image URL to the image object
        image.setImageUrl(storageUrl);

        // Insert the image object into Firestore
        ApiFuture<WriteResult> apiFuture = docReference.set(image);
        imageCreateResponse.setId(image.getId());
        imageCreateResponse.setUrl(storageUrl);
        imageCreateResponse.setOldUrl(tempUrl);

        return imageCreateResponse;
    }

    private String uploadImageToFirebaseStorage(String imageUrl, String imageId) throws IOException {
        // Download the image
        InputStream in = new URL(imageUrl).openStream();
        byte[] imageBytes = in.readAllBytes();
        in.close();

        // Define the path in Firebase Storage
        String blobString = "images/" + imageId + "/image.jpg";

        // Upload the image to Firebase Storage
        StorageClient.getInstance().bucket().create(blobString, imageBytes);

        // Return the public URL of the uploaded image
        return "https://storage.googleapis.com/" + StorageClient.getInstance().bucket().getName() + "/" + blobString;
    }
}

