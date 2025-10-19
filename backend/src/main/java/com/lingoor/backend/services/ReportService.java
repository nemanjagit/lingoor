package com.lingoor.backend.services;

import com.lingoor.backend.dtos.MostLikedPost;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PostRepository postRepository;
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;

    public byte[] generateMostLikedPostsReport() throws JRException {
        List<Post> posts = postRepository.findTop10MostLiked();

        List<MostLikedPost> data = posts.stream()
                .map(p -> new MostLikedPost(
                        p.getWord(),
                        p.getDefinition(),
                        p.getAuthor().getUsername(),
                        p.getLikes().size()
                ))
                .toList();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        try (InputStream template = resourceLoader.getResource("classpath:reports/most_liked_posts.jrxml").getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(template);
            JasperPrint filled = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);
            return JasperExportManager.exportReportToPdf(filled);
        } catch (Exception e) {
            throw new JRException("Failed to generate report", e);
        }
    }

    public byte[] generateMostFollowedUsersReport() throws JRException {
        String sql = """
        SELECT 
            u.username AS username,
            COUNT(f.id) AS followerCount
        FROM users u
        LEFT JOIN follows f ON u.id = f.followed_id
        GROUP BY u.id
        ORDER BY followerCount DESC
        LIMIT 10
    """;

        try (Connection conn = dataSource.getConnection()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/most_followed_users.jrxml"));
            JRResultSetDataSource dataSource = new JRResultSetDataSource(conn.createStatement().executeQuery(sql));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (SQLException e) {
            throw new JRException("Failed to generate report", e);
        }
    }

    public byte[] generateWordOfTheDayHistoryReport() throws JRException {
        String sql = """
        SELECT dh.date AS date, p.word AS word, p.definition AS definition, u.username AS author
        FROM daily_highlight dh
        JOIN posts p ON dh.post_id = p.id
        JOIN users u ON p.author_id = u.id
        ORDER BY dh.date DESC
    """;

        try (Connection conn = dataSource.getConnection()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/word_of_the_day_history.jrxml"));
            JRResultSetDataSource dataSource = new JRResultSetDataSource(conn.createStatement().executeQuery(sql));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (SQLException e) {
            throw new JRException("Failed to generate Word of the Day report", e);
        }
    }

}
