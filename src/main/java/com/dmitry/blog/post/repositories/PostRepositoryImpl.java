package com.dmitry.blog.post.repositories;

import com.dmitry.blog.post.models.Post;
import com.dmitry.blog.post.models.SortDirection;
import com.dmitry.blog.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostRepositoryImpl implements PostRepository {

    private static final Logger log = LoggerFactory.getLogger(PostRepositoryImpl.class);

    private static final String POST_TABLE_CREATE_QUERY = """
            CREATE TABLE IF NOT EXISTS posts (
                id UUID PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                text TEXT NOT NULL,
                author_id UUID NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

    private static final String CREATE_QUERY = """
            INSERT INTO posts (id, title, text, author_id, created_at, updated_at) 
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_QUERY = """
            UPDATE posts 
            SET title = ?, text = ?, updated_at = ? 
            WHERE id = ?;
            """;

    private static final String DELETE_QUERY = """
            DELETE FROM posts 
            WHERE id = ?;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM posts 
            WHERE id = ?;
            """;

    private static final String COUNT_QUERY = """
            SELECT COUNT(*) FROM posts;
            """;

    private static final String FIND_ALL_PAGINATED_QUERY = """
            SELECT * FROM posts 
            ORDER BY created_at %s 
            LIMIT ? OFFSET ?;
            """;

    private final Connection connection;

    public PostRepositoryImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
            initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize PostRepository", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(POST_TABLE_CREATE_QUERY);
        }
    }

    @Override
    public void create(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setObject(1, post.getId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getText());
            statement.setObject(4, post.getAuthorId());
            statement.setTimestamp(5, Timestamp.valueOf(post.getCreatedAt()));
            statement.setTimestamp(6, Timestamp.valueOf(post.getUpdatedAt()));

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to create post with title: {}", post.getTitle(), e);
        }
    }

    @Override
    public void update(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getText());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setObject(4, post.getId());

            statement.executeUpdate();
            log.info("Update post with ID: {}", post.getId());
        } catch (SQLException e) {
            log.error("Failed to update post with ID: {}", post.getId(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to delete post with ID: {}", id, e);
        }
    }

    @Override
    public Optional<Post> findById(UUID id) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSetToPost(resultSet));
            }

        } catch (SQLException e) {
            log.error("Failed to find post by ID: {}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public int count() {
        try (PreparedStatement statement = connection.prepareStatement(COUNT_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            log.error("Failed to count posts", e);
            return 0;
        }
    }

    @Override
    public List<Post> findAllPaginated(int offset, int limit, SortDirection sortDirection) {
        String sql = FIND_ALL_PAGINATED_QUERY.formatted(sortDirection.name());
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Post post = resultSetToPost(resultSet);
                posts.add(post);
            }

        } catch (SQLException e) {
            log.error("Failed to find paginated posts. Offset: {}, Limit: {}", offset, limit, e);
        }

        return posts;
    }

    private Post resultSetToPost(ResultSet rs) throws SQLException {
        return new Post(
                (UUID) rs.getObject("id"),
                rs.getString("title"),
                rs.getString("text"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                (UUID) rs.getObject("author_id")
        );
    }
}