package com.example.nutrilens.api;

import com.example.nutrilens.model.NutritionInfo;
import okhttp3.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class LogMealService {

    private static final String LOGMEAL_API_KEY = "0979e77b3ae79ac8923eb1294b22d576c3d8563c";
    private static final String SEGMENTATION_ENDPOINT = "https://api.logmeal.es/v2/image/segmentation/complete/v1.0";
    private static final String NUTRITION_ENDPOINT = "https://api.logmeal.es/v2/nutrition/recipe/nutritionalInfo/v1.0";

    private static final String SPOONACULAR_API_KEY = "20637cb248cc48a894e1e1059bc6cbbf";
    private static final String SPOONACULAR_API_ENDPOINT = "https://api.spoonacular.com/food/ingredients/search";

    private static final OkHttpClient client = new OkHttpClient();

    // Unified method for resizing and compressing image
    public static File processImage(File inputFile, int targetWidth, float compressionQuality) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputFile);
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Resize while maintaining aspect ratio
        double aspectRatio = (double) originalHeight / originalWidth;
        int targetHeight = (int) (targetWidth * aspectRatio);

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // Compress the resized image
        File compressedFile = new File("compressed_temp.jpg");
        try (FileOutputStream fos = new FileOutputStream(compressedFile)) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(compressionQuality); // 0.0 (low) to 1.0 (high)
            }

            writer.write(null, new IIOImage(resizedImage, null, null), param);
            ios.close();
            writer.dispose();
        }

        return compressedFile;
    }

    /**
     * Detects multiple food items in an image (LogMeal Segmentation API).
     */
    public static String segmentMultipleFoods(File imageFile) throws IOException {
        File processedImage = processImage(imageFile, 640, 0.7f);  // Resize to 640px width and compress to 70% quality
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", processedImage.getName(),
                        RequestBody.create(processedImage, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(SEGMENTATION_ENDPOINT)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + LOGMEAL_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Segmentation Failed: " + response);
            return response.body().string();
        }
    }

    /**
     * Gets nutrition for all dishes in an image (LogMeal Nutrition Estimation API).
     */
    public static String getNutritionForFoods(File imageFile) throws IOException {
        File processedImage = processImage(imageFile, 640, 0.7f);  // Resize to 640px width and compress to 70% quality
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", processedImage.getName(),
                        RequestBody.create(processedImage, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(NUTRITION_ENDPOINT)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + LOGMEAL_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Nutrition Info Failed: " + response);
            return response.body().string();
        }
    }

    /**
     * Fallback: Get nutrition info from Spoonacular by food name.
     */
    public static NutritionInfo fetchNutritionInfoFromSpoonacular(String foodName) throws IOException {
        HttpUrl url = HttpUrl.parse(SPOONACULAR_API_ENDPOINT).newBuilder()
                .addQueryParameter("query", foodName)
                .addQueryParameter("apiKey", SPOONACULAR_API_KEY)
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Spoonacular failed: " + response);
            return JsonParserHelper.parseSpoonacularResponse(response.body().string());
        }
    }

    /**
     * Extracts food IDs from the segmentation response.
     */
    public static List<Integer> extractFoodIds(String jsonResponse) {
        return JsonParserHelper.extractFoodIdsFromSegmentation(jsonResponse);
    }

    /**
     * Converts LogMeal nutrition response JSON into NutritionInfo model.
     */
    public static NutritionInfo parseLogMealNutrition(String jsonResponse) {
        return JsonParserHelper.parseLogMealNutrition(jsonResponse);
    }
}
