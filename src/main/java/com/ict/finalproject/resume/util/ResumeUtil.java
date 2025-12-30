package com.ict.finalproject.resume.util;

import com.aspose.pdf.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.responses.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ResumeUtil {
    public static List<String> extractImage(MultipartFile file) throws Exception {

        List<String> images = new ArrayList<>();
        Document document = new Document(file.getInputStream());
        for (Page page : document.getPages()) {
            XImageCollection imageCollection = page.getResources().getImages();
            if (imageCollection == null || imageCollection.size() == 0) {
                continue;
            }
            // 페이지 내 이미지 반복
            for (XImage image : imageCollection) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                // 이미지 추출 (PNG 권장)
                image.save(baos,ImageFormat.Png.getValue());

                // Base64 변환
                String base64Image =
                        Base64.getEncoder().encodeToString(baos.toByteArray());

                images.add(base64Image);
            }
        }

        return images;
    }

    //  PDF 텍스트 추출 함수

    public static String extractPdfText(MultipartFile file) throws IOException {
        PDDocument doc = Loader.loadPDF(file.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        doc.close();
        return text;
    }






     // Responses API 공통 outputText 추출기

    private static String extractOutputText(Response response) {
        return response.output().stream()
                .filter(o -> o.message().isPresent())
                .findFirst()
                .orElseThrow()
                .message().orElseThrow()
                .content().get(0)
                .outputText().orElseThrow()
                .text();
    }
}









