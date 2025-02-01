package hr.algebra.medicalsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@Service
public class FileStorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpointUrl;

    @Autowired
    public FileStorageService(
            @Value("${supabase.s3.endpoint}") String endpointUrl,
            @Value("${supabase.s3.region}") String regionName,
            @Value("${supabase.s3.access-key-id}") String accessKeyId,
            @Value("${supabase.s3.secret-access-key}") String secretAccessKey,
            @Value("${supabase.bucket.name}") String bucketName
    ) {
        this.bucketName = bucketName;
        this.endpointUrl = endpointUrl; // Save the endpoint URL for later use
        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        Region region = Region.of(regionName);

        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(region)
                .endpointOverride(URI.create(endpointUrl))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .putAdvancedOption(SdkAdvancedClientOption.SIGNER, AwsS3V4Signer.create())
                        .build())
                .build();
    }

    public String storeFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String objectKey = "uploads/" + Instant.now().toEpochMilli() + "_" +
                    (originalFilename != null ? originalFilename : "file");

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            // Construct the file URL using the injected endpointUrl
            String fileUrl = String.format("%s/object/public/%s/%s", endpointUrl, bucketName, objectKey);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Could not read the file content: " + e.getMessage(), e);
        } catch (Exception ex) {
            throw new RuntimeException("Error uploading to Supabase S3: " + ex.getMessage(), ex);
        }
    }
}
