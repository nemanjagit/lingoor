package com.lingoor.backend.controllers;

import com.lingoor.backend.services.PostService;
import com.lingoor.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PostService postService;
    private final ReportService reportService;

    @PostMapping("/word-of-the-day/{postId}")
    public ResponseEntity<String> setWordOfTheDay(@PathVariable Long postId) {
        postService.setWordOfTheDay(postId);
        return ResponseEntity.ok("Word of the Day set to post with ID: " + postId);
    }

    @GetMapping("/reports/most-liked")
    public ResponseEntity<byte[]> getMostLikedPostsReport() throws JRException {
        byte[] pdf = reportService.generateMostLikedPostsReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_liked_posts.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/reports/most-followed")
    public ResponseEntity<byte[]> getMostFollowedUsersReport() throws JRException {
        byte[] pdf = reportService.generateMostFollowedUsersReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=most_followed_users.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/reports/word-of-the-day-history")
    public ResponseEntity<byte[]> getWordOfTheDayHistoryReport() throws JRException {
        byte[] pdf = reportService.generateWordOfTheDayHistoryReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=word_of_the_day_history.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
