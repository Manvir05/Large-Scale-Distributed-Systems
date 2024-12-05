package edu.upf.uploader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.List;

public class S3Uploader implements Uploader {
    private final AmazonS3 s3;
    private final String BucketName;
    private final String Prefix;


    public S3Uploader(String BucketName, String Prefix, String Credentials){
        this.BucketName = BucketName;
        this.Prefix = Prefix;

        this.s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new ProfileCredentialsProvider(Credentials).getCredentials()))
                .withRegion("us-east-1") // Specify the region your bucket is in
                .build();
    }
    @Override
    public void upload(List<String> files) {
        for (String filePath : files) {
            File file = new File(filePath);
            String key = Prefix + file.getName();
            try {
                System.out.println("Uploading " + filePath + " to S3 bucket " + BucketName + "...");
                s3.putObject(new PutObjectRequest(BucketName, key, file));
                System.out.println("Upload successful.");
            } catch (Exception e) {
                System.err.println("Error uploading " + filePath + ": " + e.getMessage());
            }
        }
    }
}
