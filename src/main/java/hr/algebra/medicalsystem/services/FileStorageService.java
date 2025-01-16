package hr.algebra.medicalsystem.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.time.Instant;

@Service
public class FileStorageService {

    @Value("${supabase.s3.endpoint}")
    private String endpointUrl;

    @Value("${supabase.s3.region}")
    private String regionName;

    @Value("${supabase.s3.access-key-id}")
    private String accessKeyId;

    @Value("${supabase.s3.secret-access-key}")
    private String secretAccessKey;

    @Value("${supabase.bucket.name}")
    private String bucketName;

    public String storeFile(MultipartFile file) {
        try {
            // 1. Create S3 client with the custom endpoint
            AwsBasicCredentials creds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

            // region must be a valid string, e.g. "eu-central-1"
            Region region = Region.of(regionName);

            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(creds))
                    .region(region)
                    .endpointOverride(java.net.URI.create(endpointUrl))  // set the custom supabase S3 endpoint
                    .build();

            // 2. Build the object key. e.g., "uploads/1685876490_filename.jpg"
            String originalFilename = file.getOriginalFilename();
            String objectKey = "uploads/" + Instant.now().toEpochMilli() + "_" + (originalFilename != null ? originalFilename : "file");

            // 3. Put the object
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            // 4. If you want a public URL, you can build it manually:
            // https://[endpoint]/[bucket]/[objectKey]
            // but because it's S3, typically you'd define a "public" or "signed" URL approach
            // For demonstration, let's assume the bucket is public:
            String fileUrl = endpointUrl + "/" + bucketName + "/" + objectKey;

            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException("Could not read the file content: " + e.getMessage(), e);
        } catch (Exception ex) {
            throw new RuntimeException("Error uploading to Supabase S3: " + ex.getMessage(), ex);
        }
    }
}
